package com.example.plugin

import org.gradle.api.Project
import org.kohsuke.github.GitHub

class GitHubPluginExtension {
    private final Project project

    String organization
    String repository

    GitHubPluginExtension(Project project) {
        this.project = project
    }
}
