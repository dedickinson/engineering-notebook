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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class GradlePluginTest extends Specification {

    public static final String PLUGIN_ID = GradlePlugin.NAME
    Project project

    def setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: PLUGIN_ID
    }

    def "should add a #task task"(){

        expect:
        project.tasks[task]

        where:
        task                         || taskClass
        GradlePlugin.SERVER_START    || StartTask
        GradlePlugin.SERVER_STOP     || StopTask
        GradlePlugin.SERVER_STOP_ALL || StopAllTask
        GradlePlugin.SERVER_LIST_ALL || ListAllTask

    }

    def "should add configuration"(){

        expect:
        project.configurations."$PLUGIN_ID"
    }


    def "should use defaults"(){
        when:
        ServerOptions e = new ServerOptions()

        then:
        project."$PLUGIN_ID".with {
            httpPort == e.httpPort
            documentRoot == e.documentRoot
            directoryIndex == e.directoryIndex
        }
    }

    def "start task should be configured by extension"(){
        given:
        def port = 8080
        def root = 'build/jbake'
        def indexes = ['default.htm']

        when:
        project.tasks[GradlePlugin.SERVER_START].with {
             httpPort = port
             documentRoot = root
             directoryIndex = indexes
        }

        then:
        project.tasks[GradlePlugin.SERVER_START].httpPort == port
        project.tasks[GradlePlugin.SERVER_START].documentRoot == root
        project.tasks[GradlePlugin.SERVER_START].directoryIndex == indexes

    }


}
