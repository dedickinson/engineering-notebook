package com.example.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

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
            }
        }
        return task
    }
}
