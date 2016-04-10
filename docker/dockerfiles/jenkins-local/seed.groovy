import java.nio.file.Paths
import jenkins.model.*

//See: http://javadoc.jenkins-ci.org/?jenkins/init/Initializer.html

Project project

Paths.get('/SeedJob.xml').withReader { reader ->
    project = jenkins.instance.createProjectFromXML('SeedJob', reader)
}

project.doBuild(new org.kohsuke.stapler.StaplerRequest(),
        new org.kohsuke.stapler.StaplerResponse(),
        new jenkins.util.TimeDuration(0))

