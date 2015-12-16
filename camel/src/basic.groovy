@GrabConfig(systemClassLoader=true)
import groovy.util.logging.Slf4j
import org.apache.camel.Handler
@Grab('org.apache.camel:camel-core:2.16.0')
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.main.Main

@Grab(group='ch.qos.logback', module='logback-classic', version='1.1.3')

@Slf4j
class MainShell extends Main {
    {
        enableHangupSupport()

        addRouteBuilder new MyRoute()
    }

    void afterStart() {
        log.info 'Started Camel. Use ctrl + c to terminate the JVM.'
    }

    void beforeStop() {
        log.info 'Stopping Camel.'
    }


}

class MyRoute extends RouteBuilder {
    void configure() {
        from("timer:Foo?delay=2000").process({
            log.info "Invoked timer at ${new Date()}"
        }).bean(new Object() {
            @Handler
            void doSomething(body) {
                log.info 'FOO!'
            }
        })
    }
}

new MainShell().run()

