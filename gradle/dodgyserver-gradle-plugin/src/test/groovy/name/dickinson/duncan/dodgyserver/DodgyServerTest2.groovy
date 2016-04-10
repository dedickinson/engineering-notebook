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

class DodgyServerTest2 extends Specification {

    def conditions = new PollingConditions(timeout: 10)
/*
    def "test accessing a known page"() {
        given:
        DodgyServer server
        def page = null
        URL url = 'http://127.0.0.1:9999/index.html'.toURL()

        ServerOptions options = new ServerOptions(documentRoot: "src/test/resources/site/", events: {
            if (it == DodgyServer.State.STARTED) {
                println "Server running on ${server.serverSocketDetails.localSocketAddress}"
                try {
                    println "Attempting to access ${url}"
                    page = url.text
                } catch (any) {
                    println 'Exception when trying to access page: ${any.message}'
                    throw any
                }
                println page
            }
        })

        server = new DodgyServer(options)
        server.control(DodgyServer.ServiceCommand.START)

        expect:
        conditions.eventually {
            assert page != null
        }

    }
*/
}
