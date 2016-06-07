package com.example.plugin

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHPersonSet
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHTag
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.kohsuke.github.PagedIterable

@Slf4j
class GitHubPlugin implements Plugin<Project> {

    GitHub gh

    @Override
    void apply(Project project) {
        GitHubPluginExtension extension = project.extensions.create('github', GitHubPluginExtension, project)
        Task getGitHubDetails = createGitHubDetailsTask(project, extension)
    }

    void connect() {
        if (!gh)
            gh = GitHub.connectAnonymously()
    }

    Task createGitHubDetailsTask(Project project, GitHubPluginExtension extension) {
        Task task = project.tasks.create('getGitHubDetails')
        task.with {
            description 'Access project details from GitHub'
            group 'github'
            doLast {
                connect()
                GHOrganization org = gh.getOrganization(extension.organization)
                GHRepository repo = gh.getRepository("$extension.organization/$extension.repository")
                // Need to auth?
                // GHPersonSet<GHUser> collaborators = repo.getCollaborators()

                println "Organization: $org.name - ($org.createdAt)"
                println "Repo: $repo.name ($repo.createdAt)"

                PagedIterable<GHTag> tags = repo.listTags()

                println 'Tags:'
                tags.asList().each {GHTag tag ->
                    "  - $tag.name ($tag.commit)"
                }

                //println repo.readme.read().text
                /*
                println "Collaborators: "
                collaborators.each {
                    println "  - $it.name"
                }
                */
            }
        }
    }
}
