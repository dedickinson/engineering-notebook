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

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Task to start a new server
 */
@Slf4j
class StartTask extends DefaultTask {

    /** List of all known running SERVERS */
    static final Map<Integer, DodgyServer> SERVERS = [:]

    /** The server httpPort */
    Integer httpPort

    /** The base local directory */
    String documentRoot

    /** A list of possible index filenames */
    List<String> directoryIndex

    Closure events

    /**
     * Starts a new server if one isn't already running on the requests httpPort
     * @return
     */
    @TaskAction
    start() {
        if (SERVERS.containsKey(httpPort)) {
            log.info "DodgyServer already running on httpPort ${httpPort}"
            return
        }

        DodgyServer server = new DodgyServer(new ServerOptions(
                documentRoot: documentRoot,
                httpPort: httpPort,
                directoryIndex: directoryIndex,
                events: events))
        server.control(DodgyServer.ServiceCommand.START)

        SERVERS.put(httpPort, s)
        log.info "DodgyServer running on httpPort ${httpPort}"
    }
}
