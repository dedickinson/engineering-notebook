/*
 * Copyright 2016 Duncan Dickinson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Grab('org.apache.maven:maven-model:3.3.3')
import org.apache.maven.model.Model
@Grab('org.eclipse.aether:aether-api:1.0.2.v20150114')
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.graph.DependencyNode
import org.eclipse.aether.repository.LocalRepository

if (!args)
    args = ['org.apache.tika:tika-parsers:1.10', 'compile']

//def coordinates = args[0]
//def scope = args[1]

Resolver resolver = new Resolver(null, new LocalRepository('local-repo'))

def nodeWalker
nodeWalker = { n ->
    n.children.each { child ->
        Model m = resolver.getEffectiveModel(resolver.getArtifact(child.artifact.groupId, child.artifact.artifactId, child.artifact.version))
        //println "$m.name ($m.groupId:$m.artifactId:$m.version)"
        nodeWalker child
    }
}

def buildModelNode = { coords, scope ->
    Artifact artifact = resolver.getArtifact(coords)
    Model model = resolver.getEffectiveModel(artifact)
    DependencyNode dependencyNode = resolver.getDependencyNode(artifact, scope)

    def builder = new NodeBuilder()

    builder."$model.groupId:$model.artifactId:$model.version"(
            group: model.groupId,
            artifact: model.artifactId,
            version: model.version) {
        //model {}

        parent(group: model.parent.groupId,
                artifact: model.parent.artifactId,
                version: model.parent.version) {
            //model {}
        }

        dependencies() {
            dependencyNode.children.each { child ->
                "$child.artifact.groupId:$child.artifact.artifactId:$child.artifact.version"(
                        versionConstraint: child.versionConstraint,
                        aliases: child.aliases
                ) {
                    //model {}
                }
            }
        }
    }

}

def pom = buildModelNode(args[0], args[1])
println pom.dump()
