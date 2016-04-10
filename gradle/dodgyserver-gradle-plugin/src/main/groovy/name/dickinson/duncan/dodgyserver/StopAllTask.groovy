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
 * Stops all known running SERVERS
 */
@Slf4j
class StopAllTask extends DefaultTask {

    @TaskAction
    stop() {
        if (StartTask.SERVERS) {
            StartTask.SERVERS.each { port, server ->
                server.stop()
                StartTask.SERVERS[port] = null
                StartTask.SERVERS.remove(port)
            }
        } else {
            log.info 'No SERVERS running'
        }
    }
}
