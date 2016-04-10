@Grab('org.apache.maven:maven-model:3.3.3')
@Grab('org.eclipse.aether:aether-api:1.0.2.v20150114')

import org.apache.maven.model.Model
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.repository.LocalRepository
import groovy.text.GStringTemplateEngine

/**
 *
 *
 * @author Duncan Dickinson
 */
class Main {

    static main(args) {
        String scope = 'compile'
        Resolver resolver = new Resolver(null, new LocalRepository('local-repo'))
        def engine = new GStringTemplateEngine()

        def nodes = [ ]

        args.each { gav ->
            //println "GAV: $gav"
            Artifact artifact = resolver.getArtifact(gav)

            nodes << resolver.getDependencyNode(artifact, scope)

            Model effectiveModel = resolver.getEffectiveModel(artifact)
            //println "$effectiveModel.name version $effectiveModel.version"
            //println "  Available versions: ${resolver.getVersions(artifact)}"

            println engine.createTemplate(new File('pomtemplate.html'))
                    .make([model: effectiveModel,
                           nodes: resolver.getDependencyNode(artifact, scope)])
                    .toString()

        }
    }
}
