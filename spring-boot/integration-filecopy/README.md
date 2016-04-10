# Basic Spring Boot-Integration demonstration
This code is a very basic example of using Spring Integration file channels to move a file from
one directory to another. The key point is the ease of implementation and the ability to override 
settings using Spring Boot [externalised configuration](http://docs.spring.io/spring-boot/docs/1.1.8.RELEASE/reference/htmlsingle/#boot-features-external-config).

## Build instructions
The Gradle build manager is used. The Gradle wrapper has been setup to help you so please use `./gradlew` or `griddle.bat`.

- To build the project: `./gradlew clean build`
- To run the system: `./gradlew run`
  - or: `java -jar build/libs/boot-integration-demo-0.1.0.jar`

# Key Project Files

|File|Description|  
| ------	| ------	|  
| `build.gradle`| The configuration for the Gradle build manager|
|`src/main/resources/application.properties`| Contains the default configuration used by the application|  
|`src/main/resources/integration.xml`| Contains the Spring Integration definition|  
|`src/main/groovy/demo/Application.groovy`| This is the main class for the project. It uses Spring Boot to do all the heavy lifting|  
| `src/main/groovy/demo/Application.groovy`|Called by `integration.xml` as a service-activator. Has one method that just logs the fact it's been called. |