

import org.apache.maven.model.Parent
import org.apache.maven.model.Repository
import org.apache.maven.model.building.FileModelSource
import org.apache.maven.model.building.ModelSource2
import org.apache.maven.model.resolution.InvalidRepositoryException
import org.apache.maven.model.resolution.ModelResolver
import org.apache.maven.model.resolution.UnresolvableModelException
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResult

public class ModelResolverImpl
        implements ModelResolver {

    private List<RemoteRepository> remoteRepositories
    private final LocalRepository localRepo
    private final RepositorySystem repoSystem
    private final RepositorySystemSession session

    ModelResolverImpl(
            RepositorySystem repoSystem,
            RepositorySystemSession session,
            LocalRepository localRepo,
            List<RemoteRepository> remoteRepositories) {
        this.remoteRepositories = remoteRepositories
        this.localRepo = localRepo
        this.repoSystem = repoSystem
        this.session = session
    }

    @Override
    ModelSource2 resolveModel(String groupId, String artifactId, String version) throws UnresolvableModelException {
        Artifact artifact = new DefaultArtifact(groupId, artifactId, 'pom', version)

        ArtifactRequest request = new ArtifactRequest(artifact, remoteRepositories, '')
        ArtifactResult result = repoSystem.resolveArtifact(session, request)

        return new FileModelSource(result.artifact.file)
    }

    @Override
    ModelSource2 resolveModel(Parent parent) throws UnresolvableModelException {
        Artifact artifact = new DefaultArtifact(parent.groupId, parent.artifactId, 'pom', parent.version)

        ArtifactRequest request = new ArtifactRequest(artifact, remoteRepositories, '')
        ArtifactResult result = repoSystem.resolveArtifact(session, request)

        return new FileModelSource(result.artifact.file)
    }

    @Override
    void addRepository(Repository repository) throws InvalidRepositoryException {
        //do nothing
    }

    @Override
    void addRepository(Repository repository, boolean replace) throws InvalidRepositoryException {
        //do nothing
    }

    @Override
    ModelResolver newCopy() {
        return null
    }
}
