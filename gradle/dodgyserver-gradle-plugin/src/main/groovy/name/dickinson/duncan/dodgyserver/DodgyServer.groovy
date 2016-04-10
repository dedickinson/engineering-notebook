/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package name.dickinson.duncan.dodgyserver

import groovy.util.logging.Log
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.HttpResponseInterceptor
import org.apache.http.entity.AbstractHttpEntity
import org.apache.http.entity.ContentType
import org.apache.http.entity.FileEntity
import org.apache.http.entity.StringEntity
import org.apache.http.impl.DefaultConnectionReuseStrategy
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.impl.DefaultHttpServerConnection
import org.apache.http.params.HttpParams
import org.apache.http.params.SyncBasicHttpParams
import org.apache.http.protocol.*

import javax.activation.MimetypesFileTypeMap
import java.nio.file.Files
import java.nio.file.NotDirectoryException
import java.nio.file.Path
import java.nio.file.Paths

import static groovy.io.FileType.FILES
import static org.apache.http.HttpStatus.*
import static org.apache.http.params.CoreConnectionPNames.*
import static org.apache.http.params.CoreProtocolPNames.ORIGIN_SERVER

/**
 * A very dodgy GET-only web server for use in checking websites during development.
 * Generally, this server just logs exceptions and keeps trying to run.
 *
 * @author Duncan Dickinson
 * @see <a href="https://hc.apache.org/httpcomponents-core-4.2.x/examples.html">
 *     Apache HTTP Components examples - Basic HTTP Server</a>
 */
@Log
class DodgyServer {
    /** The http port on which this server is running */
    final Integer httpPort

    /** A list of possible directory index filenames */
    final List<String> directoryIndex

    /** The base local path of the server */
    final Path documentRoot

    final Closure events

    private ServerSocket serverSocket

    private Thread serverThread
    private Thread workerThread

    private final HttpService httpService

    static final HttpProcessor HTTP_PROCESSOR = new ImmutableHttpProcessor([
            new ResponseDate(),
            new ResponseServer(),
            new ResponseContent(),
            new ResponseConnControl()
    ] as HttpResponseInterceptor[])

    static final HttpParams HTTP_PARAMS = new SyncBasicHttpParams()
    ; {
        HTTP_PARAMS.with {
            setIntParameter(SO_TIMEOUT, 30000)
            setIntParameter(SOCKET_BUFFER_SIZE, 8 * 1024)
            setBooleanParameter(STALE_CONNECTION_CHECK, false)
            setBooleanParameter(TCP_NODELAY, true)
            setParameter(ORIGIN_SERVER, 'DodgyServer/1.1')
        }
    }

    static final HttpRequestHandlerRegistry REGISTRY = new HttpRequestHandlerRegistry()
    ; {    // Set up request handlers
        REGISTRY.register('*') {
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context ->
                response.setEntity(handleRequest(request, response, context))
        } as HttpRequestHandler
    }

    static final CliBuilder CLI = new CliBuilder(usage: 'groovy http.groovy [options]')

    ; static {
        CLI.with {
            h longOpt: 'help', 'Show usage information'
            p longOpt: 'httpPort', args: 1, 'Set ServerCli httpPort. Default is 9999.'
            d longOpt: 'dir', args: 1, 'Sets the web documentRoot dir. Default is the current directory (.)'
        }
    }

    static void main(args) {
        Integer port = 9999
        String documentRoot = '.'

        println args

        OptionAccessor options = CLI.parse(args)

        if (options.h) {
            CLI.usage()
            return
        }
        if (options.p) {
            port = options.p.toInteger()
        }
        if (options.d) {
            documentRoot = options.d
        }
        log.info 'Thank you for using Dodgy Server. Now starting - please lower your expectations!'
        DodgyServer ds = new DodgyServer(
                new ServerOptions(httpPort: port,
                        documentRoot: documentRoot
                ))
        ds.start()
    }

    DodgyServer(ServerOptions options) throws NotDirectoryException {
        directoryIndex = options.directoryIndex
        httpPort = options.httpPort
        documentRoot = Paths.get(options.documentRoot).toAbsolutePath().normalize()

        if (!Files.isDirectory(documentRoot)) {
            throw new NotDirectoryException("Not a directory: ${documentRoot}")
        }

        if (options.events) {
            events = options.events
        }
        events(state)

        // Set up the HTTP service
        httpService = new HttpService(
                HTTP_PROCESSOR,
                new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory(),
                REGISTRY,
                HTTP_PARAMS)

        serverSocket = new ServerSocket()

        log.info "Server ready to go on httpPort $httpPort"
    }

    enum State {
        NOT_STARTED, STARTING, STARTED, STOPPING, STOPPED
    }

    private synchronized State state = State.NOT_STARTED

    State getState() {
        state
    }

    enum ServiceCommand {
        START, STOP
    }

    Map getServerSocketDetails() {
        [
                localPort         : serverSocket.localPort,
                localSocketAddress: serverSocket.localSocketAddress
        ]
    }

    synchronized State control(ServiceCommand cmd = ServiceCommand.START) {
        switch (cmd) {
            case ServiceCommand.START:
                this.start()
                break
            case ServiceCommand.STOP:
                this.stop()
                break
        }
        state
    }

    /**
     * Stops the dodgy server
     * @return this instance
     */
    private DodgyServer stop() {
        if (state == State.STARTED) {
            events(state = State.STOPPING)
        } else {
            throw new Exception('Let me get started first')
        }
        this
    }

    /**
     * Starts the dodgy server
     * @return this instance
     */
    private DodgyServer start() {
        events(state = State.STARTING)
        serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), httpPort))
        serverThread = Thread.start('DodgyServer thread') {
            events(state = state.STARTED)
            while (!Thread.interrupted() && state != State.STOPPING) {
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection()
                try {
                    Socket socket = serverSocket.accept()
                    conn.bind(socket, HTTP_PARAMS)
                } catch (IOException e) {
                    log.warning "DodgyServer thread exception: ${e.class.name}: ${e.message}"
                    throw e
                }

                log.info "Listening on httpPort ${serverSocket.localPort} with documentRoot $documentRoot"
                workerThread = Thread.startDaemon('DodgyServer worker thread') {
                    UUID id = UUID.randomUUID()
                    HttpContext context = new BasicHttpContext()

                    log.info "Worker thread $id - running"
                    log.info "Incoming connection from ${conn.remoteAddress}"
                    while (!Thread.interrupted() && conn.isOpen()) {
                        try {
                            httpService.handleRequest(conn, context)
                        } catch (SocketTimeoutException e) {
                            log.info "Worker reported socket timeout (id: $id): ${e.class.name}: ${e.message}"
                        } catch (java.io.IOException | org.apache.http.HttpException e) {
                            log.warning "Worker thread exception (id: $id): ${e.class.name}: ${e.message}"
                        } finally {
                            conn.shutdown()
                            log.info "Worker thread $id - shut down"
                        }
                    }
                }
            }
            serverSocket.close()
            events(state = State.STOPPED)
            log.info 'DodgyServer shut down'
        }
        this
    }

    /**
     * Check if the requested file exists. If passed a directory, check if there is an index file
     * @param uri the URI requested by the client (web browser)
     * @param documentRoot the base local directory for the web server
     * @param directoryIndex a List of possible directory index filenames
     * @throws FileNotFoundException if the requested file or the index files don't exist
     * @return the Path of the local matching file
     */
    static Path checkForFile(String uri, Path documentRoot, List<String> directoryIndex = [ ])
            throws FileNotFoundException {
        Path path = null
        if (uri[-1] == '/') {
            //Path is a directory so check for indexes
            log.info "Checking $uri for an index file (one of ${directoryIndex})"
            List listing = [ ]
            if (uri.length() == 1) {
                documentRoot.eachFile(FILES) { listing << it }
            } else {
                documentRoot.resolve(uri[1..-1]).eachFile(FILES) { listing << it }
            }
            for (Path p in listing) {
                if (p.fileName.toString() in directoryIndex) {
                    path = p
                    log.info "Index file for $uri found: ${path.absolute}"
                }
            }
        } else {
            path = documentRoot.resolve(uri[1..-1])
            log.info "File requested: ${path}"
        }
        if (path?.startsWith(documentRoot)) {
            return path
        }
        throw new FileNotFoundException("Couldn't determine a file for $uri")
    }

    /**
     * Handles an incoming HTTP request
     * @param request
     * @param response
     * @param context
     * @return an HttpEntity for the response
     */
    private AbstractHttpEntity handleRequest(HttpRequest request, HttpResponse response, HttpContext context) {
        String uri = request.requestLine.uri
        try {
            if (request.requestLine.method == 'GET') {
                Path p = checkForFile(uri, documentRoot, directoryIndex)

                if (Files.isRegularFile(p) && Files.isReadable(p)) {
                    log.info "Serving file: $p"
                    response.setStatusCode(SC_OK)
                    try {
                        File f = p.toFile()
                        //Files.probeContentType kept giving me bogus (null) results
                        ContentType type = ContentType.create(new MimetypesFileTypeMap().getContentType(f))

                        return new FileEntity(p.toFile(), type)
                    } catch (any) {
                        log.warning "Failed to load FileEntity $p with type ${Files.probeContentType(p)}"
                        throw any
                    }
                }
                return displayScreen(request, response, SC_NOT_FOUND, "Does not exist: ${p.toString()}")
            }
            return displayScreen(request, response, SC_METHOD_NOT_ALLOWED)
        } catch (any) {
            return displayScreen(request, response, SC_INTERNAL_SERVER_ERROR,
                    "handleRequest failed: $any.message (${any.class})")
        }
    }

    /**
     * A helper screen that handles failed requests
     * @param request the HTTP request object
     * @param response the HTTP response object
     * @param status the HTTP status code
     * @param message an additional message to display in the screen
     * @return an HttpEntity for the response (use org.apache.http.HttpStatus constants)
     */
    private AbstractHttpEntity displayScreen(HttpRequest request, HttpResponse response,
                                             int status = SC_OK, String message = '(no message)') {
        response.setStatusCode(status)
        if (status != SC_OK) {
            log.warn message
        }

        def headers = ""
        request.allHeaders.each {
            headers <<= "  ${it.name}:\t${it.value}\n"
        }

        new StringEntity(
                """${response.statusLine}

Message: $message

Method: ${request.requestLine.method}
URI: ${request.requestLine.uri}
Protocol: ${request.requestLine.protocolVersion.protocol}

Request headers:
$headers

DodgyServer info:
   DodgyServer documentRoot: ${documentRoot}
   DodgyServer httpPort: ${httpPort}\n""",
                ContentType.TEXT_PLAIN)
    }
}
