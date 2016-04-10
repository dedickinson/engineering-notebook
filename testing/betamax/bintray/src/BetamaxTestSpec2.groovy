import co.freeside.betamax.ProxyConfiguration
import co.freeside.betamax.TapeMode
import co.freeside.betamax.junit.Betamax
import co.freeside.betamax.junit.RecorderRule
import groovy.json.JsonSlurper
@Grab('org.spockframework:spock-core:1.0-groovy-2.4')

@GrabResolver(name='jfrog-oss', root='http://oss.jfrog.org/oss-snapshot-local/')
@Grab("co.freeside.betamax:betamax-proxy:2.0-SNAPSHOT")
@Grab("co.freeside.betamax:betamax-junit:2.0-SNAPSHOT")
@Grab('org.glassfish.jersey.core:jersey-client:2.22.1')

import org.junit.Rule

import spock.lang.Specification

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType

class BetamaxTestSpec2 extends Specification {
    @Rule
    RecorderRule recorderRule = new RecorderRule(ProxyConfiguration.builder()
            .sslEnabled(true)
            .build())

    @Betamax(tape = 'jCenterKeywordQuery2.tape', mode = TapeMode.WRITE_ONLY)
    def "Test basic keyword query with JCenter"() {
        given:
        def searcher = new Searcher()
        def result = searcher.searchJCenter('groovy*')
        expect:
        1 == 1
    }

    @Betamax(tape = 'mvnKeywordQuery2.tape', mode = TapeMode.WRITE_ONLY)
    def "Test basic keyword query with Maven Central"() {
        given:
        def searcher = new Searcher()
        def result = searcher.searchMavenCentral('groovy')
        expect:
        1 == 1
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



