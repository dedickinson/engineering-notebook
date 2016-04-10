@Grab('org.eclipse.aether:aether-api:1.0.2.v20150114')
@Grab('org.eclipse.aether:aether-util:1.0.2.v20150114')
@Grab('org.eclipse.aether:aether-impl:1.0.2.v20150114')
@Grab('org.eclipse.aether:aether-connector-basic:1.0.2.v20150114')
@Grab('org.eclipse.aether:aether-transport-file:1.0.2.v20150114')
@Grab('org.eclipse.aether:aether-transport-http:1.0.2.v20150114')
@Grab('org.eclipse.aether:aether-transport-wagon:1.0.2.v20150114')
@Grab('org.apache.maven.wagon:wagon-ssh:1.0')
@Grab('org.apache.maven:maven-aether-provider:3.3.3')

import groovy.util.logging.Slf4j
import org.apache.maven.model.Model
import org.apache.maven.model.building.DefaultModelBuilderFactory
import org.apache.maven.model.building.DefaultModelBuildingRequest
import org.apache.maven.model.building.ModelBuilder
import org.apache.maven.model.building.ModelBuildingRequest
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyNode
import org.eclipse.aether.impl.DefaultServiceLocator
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.resolution.VersionRangeRequest
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator

import java.nio.file.Files

/*

See:
 - https://www.eclipse.org/aether/documentation/
 - http://git.eclipse.org/c/aether/aether-demo.git/tree/aether-demo-snippets/src/main/java/
 - http://download.eclipse.org/aether/aether-core/1.0.1/apidocs/
 - http://maven.apache.org/ref/3.3.3/apidocs/index.html
*/

@Slf4j
class Resolver {
    private final LocalRepository localRepository
    private final List<RemoteRepository> remoteRepositories
    private final RepositorySystem repositorySystem
    private final RepositorySystemSession repositorySession

    Resolver(List<RemoteRepository> remoteRepositories = null,
             LocalRepository localRepository = null) {

        this.localRepository = localRepository ? new LocalRepository(localRepository.basedir) :new LocalRepository(Files.createTempDirectory('resolver-').toFile())

        log.info "Local repository: ${this.localRepository.basedir}"

        this.remoteRepositories = remoteRepositories ? remoteRepositories.asImmutable() :[ new RemoteRepository.Builder('jcenter', 'default', 'http://jcenter.bintray.com/').build() ]

        repositorySystem = newRepositorySystem()
        repositorySession = newSession(repositorySystem, this.localRepository)
    }

    static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator()
        locator.with {
            addService RepositoryConnectorFactory, BasicRepositoryConnectorFactory
            addService TransporterFactory, FileTransporterFactory
            addService TransporterFactory, HttpTransporterFactory
        }
        return locator.getService(RepositorySystem)
    }

    static RepositorySystemSession newSession(RepositorySystem system, LocalRepository localRepository) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession()
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepository))
        return session
    }


    List getVersions(Artifact artifact, String version = '[0,)') {
        VersionRangeRequest rangeRequest = new VersionRangeRequest()
        rangeRequest.artifact = new DefaultArtifact(artifact.groupId, artifact.artifactId, 'pom', version)
        rangeRequest.repositories = remoteRepositories
        return repositorySystem.resolveVersionRange(repositorySession, rangeRequest).versions
    }

    /*
    static Artifact getPom(Artifact artifact) {
        ArtifactRequest request = new ArtifactRequest(
                new DefaultArtifact(artifact.groupId,
                        artifact.artifactId,
                        'pom',
                        artifact.version),
                [JCENTER],
                null)
        repoSystem.resolveArtifact(session, request).artifact
    }

    static MavenProject loadProject(Artifact artifact) {
        println "Trying to load model for $artifact.file"
        ModelReader reader = new DefaultModelReader()
        Model model = reader.read(artifact.file, [ : ])

        return new MavenProject(model)
    }*/

    //See: https://wiki.eclipse.org/Aether/Transitive_Dependency_Resolution
    DependencyNode getDependencyNode(Artifact artifact, String scope) {
        Dependency dependency = new Dependency(artifact, scope)

        CollectRequest collectRequest = new CollectRequest()
        collectRequest.setRoot(dependency)
        collectRequest.repositories = remoteRepositories

        DependencyNode node = repositorySystem.collectDependencies(repositorySession, collectRequest).getRoot()

        DependencyRequest dependencyRequest = new DependencyRequest()
        dependencyRequest.setRoot(node)

        repositorySystem.resolveDependencies(repositorySession, dependencyRequest)

        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator()
        node.accept(nlg)
        return node
    }

    Model getEffectiveModel(Artifact artifact) {
        ModelBuilder builder = new DefaultModelBuilderFactory().newInstance()
        ModelBuildingRequest request = new DefaultModelBuildingRequest()
        request.with {
            setProcessPlugins(false)
            setPomFile(artifact.file)
            setTwoPhaseBuilding(false)
            setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL)
            setSystemProperties(System.getProperties())
            setModelResolver(new ModelResolverImpl(repositorySystem, repositorySession, localRepository, remoteRepositories))
        }
        builder.build(request).getEffectiveModel()
    }

    Artifact getArtifact(String artifact) {
        def coords = artifact.tokenize(':')
        getArtifact(new DefaultArtifact(coords[0], coords[1], 'pom', coords[2]))
    }

    Artifact getArtifact(groupId, artifactId, version) {
        getArtifact(new DefaultArtifact(groupId, artifactId, 'pom', version))
    }

    Artifact getArtifact(Artifact artifact) {
        ArtifactRequest request = new ArtifactRequest(artifact, remoteRepositories, '')
        repositorySystem.resolveArtifact(repositorySession, request).artifact
    }
}
