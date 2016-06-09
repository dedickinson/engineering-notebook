package com.example.plugin

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import org.kohsuke.github.HttpConnector
import org.yaml.snakeyaml.Yaml

import java.nio.file.Files
import java.nio.file.Paths

@Slf4j
class GitHubPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        GitHubPluginExtension extension = project.extensions.create('github', GitHubPluginExtension, project)
        addGenerateProjectModelTask(project, extension)
    }

    Task addGenerateProjectModelTask(Project project, GitHubPluginExtension extension) {
        Task task = project.tasks.create('generateProjectModel')

        task.with {
            description 'Access project details from GitHub'
            group 'github'
            doLast {
                //GitHub gh = GitHub.connectAnonymously()

                GitHub gh = new GitHubBuilder().withConnector { url ->
                    //Small change to allow for access to the preview api
                    HttpURLConnection conn = url.openConnection() as HttpURLConnection
                    conn.addRequestProperty('Accept', 'application/vnd.github.drax-preview+json')
                    conn
                }.build()

                GHOrganization org = gh.getOrganization(extension.organization)
                GHRepository repo = gh.getRepository("$extension.organization/$extension.repository")

                //GHPersonSet<GHUser> collaborators = repo.getCollaborators()

                Map projectDetails = [name           : repo.name,
                                      fullName       : repo.fullName,
                                      url            : repo.htmlUrl.toString(),
                                      inceptionYear  : Util.convertDate(repo.createdAt).year,
                                      organization   : [name         : org.name,
                                                        inceptionYear: Util.convertDate(org.createdAt).year,
                                                        url          : org.htmlUrl.toString(),
                                                        location     : org.location],
                                      issueManagement: [system: 'GitHub Issues',
                                                        url   : "${repo.htmlUrl}/issues".toString()],
                                      scm            : [url   : repo.svnUrl,
                                                        sshUrl: repo.sshUrl]
                ]

                if (Files.isRegularFile(Paths.get(project.rootDir.toString(), '.travis.yml'))) {
                    projectDetails << [ciManagement: [system: 'travis',
                                                      url   : "https://travis-ci.org/$extension.organization/$extension.repository".toString()]]
                }

                print new Yaml().dump(projectDetails)
            }
        }
    }
}
