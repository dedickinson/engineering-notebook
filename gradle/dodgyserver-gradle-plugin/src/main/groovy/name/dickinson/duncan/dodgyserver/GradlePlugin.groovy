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

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The Gradle plugin class
 */
class GradlePlugin implements Plugin<Project> {
    public static final String NAME = 'name.dickinson.duncan.dodgyserver'
    public static final String SERVER_START = 'startDodgyServer'
    public static final String SERVER_STOP = 'stopDodgyServer'
    public static final String SERVER_STOP_ALL = 'stopAllDodgyServers'
    public static final String SERVER_LIST_ALL = 'listAllDodgyServers'
    public static final String DOCUMENTATION = 'Documentation'

    ServerOptions extension
    Project project

    /**
     * Applies the plugin
     * @param project
     */
    void apply(Project project) {
        this.project = project

        //Configuration configuration = project.configurations.maybeCreate(NAME)
        project.configurations.maybeCreate(NAME)
        extension = project.extensions.create(NAME, ServerOptions)

        project.task(SERVER_START, type: StartTask, group: DOCUMENTATION,
                description: 'Run a dodgy little web server for viewing documentation') {
            conventionMapping.httpPort = { project.dodgyserver.httpPort }
            conventionMapping.documentRoot = { project.dodgyserver.documentRoot }
            conventionMapping.directoryIndex = { project.dodgyserver.directoryIndex }
        }

        project.task(SERVER_STOP, type: StopTask, group: DOCUMENTATION,
                description: 'Stop the dodgy little web server') {
            conventionMapping.httpPort = { project.dodgyserver.httpPort }
        }

        project.task(SERVER_STOP_ALL, type: StopAllTask, group: DOCUMENTATION,
                description: 'Stops all the dodgy little web SERVERS') {
        }

        project.task(SERVER_LIST_ALL, type: ListAllTask, group: DOCUMENTATION,
                description: 'Lists all the dodgy little web SERVERS') {
        }
    }

}
