/*
 * Copyright 2014 Duncan Dickinson <duncan at dickinson.name>.
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
 */

package demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.stereotype.Component

/**
 * A basic demonstration of Spring Integration in a Spring Boot project.
 * Copies files from one directory to another.
 * <p>
 * The standard Spring Boot handling of parameters is used. 
 * See the <a href="http://docs.spring.io/spring-boot/docs/1.1.8.RELEASE/reference/htmlsingle/#boot-features-external-config">
 * Spring Boot documentation</a>
 * <p>
 * Useful command-line parameters:
 * <ul>
 *   <li>--inputDir=<i>dir</i></li>
 *   <li>--inputFilePattern=<i>ant pattern</i></li>
 *   <li>--outputDir=<i>dir</i></li>
 * </ul>
 */
@Configuration
@EnableAutoConfiguration
@Component
@ImportResource("classpath:integration.xml")
public class Application {
    
    /** The directory being monitered */
    @Value('${inputDir}')
    String inputDir = ''

    /** The file naming pattern for filtering files.  */
    @Value('${inputFilePattern}')
    String inputFilePattern = '*'
    
    /** The  */
    @Value('${outputDir}')
    String outputDir = ''
    
    public static void main(args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args)
    }
}

