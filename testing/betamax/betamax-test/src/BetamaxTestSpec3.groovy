@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
@Grab("co.freeside:betamax:1.1.2")
@Grab('org.glassfish.jersey.core:jersey-client:2.22.1')
import groovy.json.JsonSlurper
import org.junit.Rule
import spock.lang.Ignore
import spock.lang.Specification

import co.freeside.betamax.Betamax
import co.freeside.betamax.Recorder
import co.freeside.betamax.TapeMode

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType

class BetamaxTestSpec3 extends Specification {

    @Rule
    Recorder recorder = new Recorder()

    @Betamax(tape = 'mvnKeywordQuery3.tape', mode = TapeMode.WRITE_ONLY)
    def "Test basic keyword query with Maven Central"() {
        given:
        def searcher = new Searcher()
        def result = searcher.searchMavenCentral('groovy')
        expect:
        result
    }

    class Searcher {
        def searchJCenter(qry) {
            new JsonSlurper().parseText ClientBuilder.newClient().
                    target('https://api.bintray.com/search/packages/maven/'.toURI()).
                    queryParam('q', qry).
                    request(MediaType.APPLICATION_JSON_TYPE).get(String)
        }

        def searchMavenCentral(qry) {
            new JsonSlurper().parseText ClientBuilder.newClient().
                    target('http://search.maven.org/solrsearch/select'.toURI()).
                    queryParam('q', qry).
                    queryParam('rows', 20).
                    queryParam('wt', 'json').
                    request().
                    get(String)
        }
    }
}
