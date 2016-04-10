import com.thoughtworks.xstream.converters.basic.StringBufferConverter
@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
@Grab('software.betamax:betamax-junit:2.0.0-alpha-1')
@Grab('org.glassfish.jersey.core:jersey-client:2.22.1')
import org.junit.Rule
import software.betamax.ProxyConfiguration
import software.betamax.TapeMode
import software.betamax.junit.Betamax
import software.betamax.junit.RecorderRule
import spock.lang.Specification

import javax.net.ssl.HostnameVerifier
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType

class BetamaxTest extends Specification {
    static final query = 'groovy*'

    @Rule
    RecorderRule recorderRule = new RecorderRule(ProxyConfiguration.builder()
            .sslEnabled(true)
            .build())

    @Betamax(tape = 'Jersey.tape', mode = TapeMode.WRITE_ONLY)
    def "Test search using Jersey"() {
        given:
        def result = Searcher.search(query)
        expect:
        println result
    }
    class Searcher {
        static String baseUrl = 'https://api.bintray.com/search/packages/maven/'

        static search(qry) {
            ClientBuilder.newClient().
                    target('https://api.bintray.com/search/packages/maven/'.toURI()).
                    queryParam('q', qry).
                    request(MediaType.APPLICATION_JSON_TYPE).
                    get(String)
        }
    }
}
