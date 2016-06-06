package com.example.plugin

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

@Slf4j
class ExamplePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        ExamplePluginExtension extension = project.extensions.create('dinnerParty', ExamplePluginExtension, project)
        Task cheese = createCheeseTask(project, extension)
        Task wine = createWineTask(project, extension)
        wine.dependsOn(cheese)
    }

    Task createCheeseTask(Project project, ExamplePluginExtension extension) {
        Task task = project.tasks.create('cheese')
        task.with {
            description 'Eat some cheese'
            group 'dinner party'
            doLast {
                println "Let's eat some ${extension.cheeseName}"
                log.info 'Cheese eating in progress'
            }
        }
    }

    Task createWineTask(Project project, ExamplePluginExtension extension) {
        Task task = project.tasks.create('wine')
        task.with {
            description 'Drink some wine'
            group 'dinner party'
            doLast {
                println "Let's drink some ${extension.wineName}"
                log.info 'Wine drinking in progress'
            }
        }
        return task
    }
}
