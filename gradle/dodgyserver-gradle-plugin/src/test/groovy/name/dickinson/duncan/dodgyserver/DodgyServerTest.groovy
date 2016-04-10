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

import spock.lang.Specification
import spock.util.concurrent.PollingConditions


class DodgyServerTest extends Specification {

    PollingConditions conditions = new PollingConditions(timeout: 10)

    def "test starting and stopping a dodgy server"() {
        given: "a new server"
        DodgyServer server

        def started = false
        def stopped = false

        ServerOptions options = new ServerOptions(documentRoot: "src/test/resources/site/", events: {
            println it
            switch (it) {
                case DodgyServer.State.STARTED:
                    server.control(DodgyServer.ServiceCommand.STOP)
                    started = true
                    break
                case DodgyServer.State.STOPPED:
                    stopped = true
                    break
            }
        })

        server = new DodgyServer(options)

        expect: "the server is running and is then shut down"
        server.control(DodgyServer.ServiceCommand.START)
        conditions.eventually {
            assert started
            assert stopped
        }
    }
}
