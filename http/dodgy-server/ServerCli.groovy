import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Log

@Grab('org.apache.httpcomponents:httpcore:4.4.1')
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.HttpServerConnection
import org.apache.http.entity.AbstractHttpEntity
import org.apache.http.entity.ContentType
import org.apache.http.entity.FileEntity
import org.apache.http.entity.StringEntity
import org.apache.http.impl.DefaultBHttpServerConnectionFactory
import org.apache.http.protocol.*

import java.nio.file.Files
import java.nio.file.NotDirectoryException
import java.nio.file.Path
import java.nio.file.Paths

import static groovy.io.FileType.FILES
import static org.apache.http.HttpStatus.*

def cli = new CliBuilder(usage: 'groovy http.groovy [options]')

cli.with {
    h longOpt: 'help', 'Show usage information'
    p longOpt: 'port', args: 1, 'Set ServerCli port. Default is 9999.'
    d longOpt: 'dir', args: 1, 'Sets the web documentRoot dir. Default is the current directory (.)'
}

def config = [ documentRoot  : '.',
               port          : 9999,
               directoryIndex: [ 'index.html', 'index.htm' ] ]

def options = cli.parse(args)

if (options.h) {
    cli.usage()
    return
}
if (options.p) {
    config.port = options.p.toInteger()
}
if (options.d) {
    config.documentRoot = options.d
}
new Server(config).start()

@Log
@EqualsAndHashCode(includes = ['directoryIndex', 'documentRoot', 'serverSocket'],
        cache = true)
class Server {
    final List<String> directoryIndex
    final ServerSocket serverSocket
    final Path documentRoot
    private Boolean running = false

    Server(Map config) {
        this((String) config.documentRoot, (Integer) config.port, (List<String>) config.directoryIndex)
    }

    Server(String documentRoot, int serverPort, List<String> directoryIndex) throws NotDirectoryException {
        this.directoryIndex = directoryIndex
        serverSocket = new ServerSocket(serverPort)
        this.documentRoot = Paths.get(documentRoot).toAbsolutePath().normalize()
        if (!Files.isDirectory(this.documentRoot)) {
            throw new NotDirectoryException("Not a directory: $documentRoot")
        }
    }

    synchronized Server start() {
        // Set up request handlers
        final UriHttpRequestHandlerMapper registry = new UriHttpRequestHandlerMapper()
        registry.register('*') {
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context ->
                response.setEntity(handleRequest(request, response, context))
        } as HttpRequestHandler

        final HttpService httpService = new HttpService(HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("Test/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl()).build(),
                registry)

        Thread.start 'Server thread', {
            log.info "Listening on port ${serverSocket.localPort} with documentRoot $documentRoot"
            running = true
            while (!Thread.interrupted() && running) {
                try {
                    HttpServerConnection conn = DefaultBHttpServerConnectionFactory.INSTANCE.createConnection(serverSocket.accept())
                    Thread.start 'Worker thread', {
                        HttpContext context = new BasicHttpContext()
                        try {
                            while (!Thread.interrupted() && conn.isOpen()) {
                                httpService.handleRequest(conn, context)
                            }
                        } catch (any) {
                            log.warning "${any.class.name}: ${any.message}"
                        } finally {
                            conn.shutdown()
                        }
                    }
                } catch (any) {
                    log.warning "${any.class.name}: ${any.message}"
                    break
                }
            }
            log.info 'Shutting down Server'
            running = false
        }
        this
    }

    AbstractHttpEntity handleRequest(HttpRequest request, HttpResponse response, HttpContext context) {
        Path documentRoot = documentRoot
        List<String> directoryIndex = directoryIndex
        if (request.getRequestLine().getMethod() == 'GET') {
            Path p = checkForFile(request.getRequestLine().getUri(), documentRoot, directoryIndex)
            if (Files.isRegularFile(p)) {
                response.setStatusCode(SC_OK)
                return new FileEntity(p.toFile(), ContentType.create(Files.probeContentType(p)))
            } else {
                return displayScreen(request, response, SC_NOT_FOUND, "Does not exist: ${p.toString()}")
            }
        } else {
            return displayScreen(request, response, SC_METHOD_NOT_ALLOWED)
        }
    }

    private Path checkForFile(String uri, Path documentRoot, List<String> directoryIndex = [ ]) {
        Path path
        if (uri[-1] != '/') {
            path = documentRoot.resolve(uri.substring(1))
        } else {
            //Path is a directory so check for indexes
            def listing = [ ]
            documentRoot.resolve(uri.substring(1)).eachFile(FILES) { listing << it }
            for (Path p in listing) {
                if (p.fileName.toString() in directoryIndex) {
                    path = p
                }
            }
        }
        if (path.startsWith(documentRoot)) {
            return path
        }
        throw new FileNotFoundException("Couldn't determine a file for $uri")
    }

    AbstractHttpEntity displayScreen(HttpRequest request, HttpResponse response, int status = SC_OK, String message = '(no message)') {
        response.setStatusCode(status)
        if (status != SC_OK) {
            log.warning message
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

Server info:
   Server documentRoot: ${documentRoot}
   Server port: ${serverSocket.localPort}""",
                ContentType.TEXT_PLAIN)
    }
}
