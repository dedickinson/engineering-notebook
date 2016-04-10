import groovy.util.logging.Slf4j
@GrabConfig(systemClassLoader = true)
@Grab('ch.qos.logback:logback-classic:1.1.3')

@Grab('org.apache.activemq:activemq-broker:5.13.2')
@Grab('org.apache.activemq:activemq-kahadb-store:5.13.2')
import org.apache.activemq.broker.BrokerService

@Grab('org.apache.camel:camel-core:2.16.0')
@Grab('org.apache.camel:camel-stream:2.16.0')
@Grab('org.apache.activemq:activemq-camel:5.13.1')
import org.apache.camel.CamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.apache.activemq.camel.component.ActiveMQComponent

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/*
 * The producer just creates new messages every second.
 * The consumer uses TestEndpoint as a bean to process the messages.
 * However, TestEndpoint delays for longer than the time between messages (5s vs 1s)
 * This means that new messages start to queue up. Shutting down (Ctrl+C) will stop Camel
 * and its routes and leave some time for the TestEndpoint threadPool to clear.
 */

/**
 * Provides a static thread pool and a single instance
 * method that makes use of it.
 */
@Slf4j
class TestEndpoint {
    static final ExecutorService threadPool = Executors.newFixedThreadPool(5)
    def doSomething(data) {
        threadPool.submit {
            log.info "Yawn!"
            sleep 5000
            log.info "What? $data"
        }
    }

    //Shutting down will cause the threadPool to block and clear
    //However, we won't wait forever for the queue to clear.
    static shutdown() {
        log.info 'Shutting down the TestEnpoint threadPool'
        threadPool.shutdown()
        threadPool.awaitTermination 10, TimeUnit.SECONDS
    }

    static Boolean isShutdown() {threadPool.isShutdown()}
}

// Create the Camel context and add the routes
CamelContext context = new DefaultCamelContext()
context.addComponent("demo-queue", ActiveMQComponent.activeMQComponent('vm://demo'))

// A basic route that triggers every 1s and posts a basic message to the queue
RouteBuilder producer = new RouteBuilder() {
    void configure() {
        from('timer://demoTimer?period=1s')
        .routeId('Message producer')
                .process { exchange ->
            exchange.out.body = "hello,world ${new Date()}".toString()
        }
        .to('demo-queue:DEMO')
    }
}

// A basic route that consumes messages via a TestEndpoint bean
RouteBuilder consumer = new RouteBuilder(context) {
    void configure() {
        from('demo-queue:DEMO').routeId('Message consumer')
        .bean(TestEndpoint, 'doSomething')
    }
}

context.addRoutes producer
context.addRoutes consumer

println 'Starting camel'
context.start()

def shutdown = {
    println 'Shutting down'
    context.stop()
    TestEndpoint.shutdown()
}

Runtime.runtime.addShutdownHook shutdown
