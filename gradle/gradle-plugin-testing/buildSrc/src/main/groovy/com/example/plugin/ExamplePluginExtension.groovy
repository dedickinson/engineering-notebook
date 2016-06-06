package com.example.plugin

import org.gradle.api.Project

class ExamplePluginExtension {
    private final Project project

    String cheeseName = 'Cheddar'
    String wineName = 'Cheteau ver de Fleur'

    ExamplePluginExtension(Project project) {
        this.project = project
    }
}
