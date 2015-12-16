@Grab(group = 'org.slf4j', module = 'slf4j-api', version = '1.7.12')
@Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3')
@Grab(group = 'ch.qos.logback', module = 'logback-core', version = '1.1.3')

def appenderList = ["CONSOLE"]

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%level %logger - %msg%n'
    }
}
root INFO, appenderList
