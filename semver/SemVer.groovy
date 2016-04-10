/**
 * @see <a href='http://semver.org/'>Semantic Versioning</a>
 */
import groovy.transform.ToString

@ToString
class SemVer implements Comparable {
    final int major, minor, patch

    SemVer(String version) {
        def elements = version.tokenize('.')

        major = elements[0]? elements[0].toInteger(): 0
        minor = elements[1]? elements[1].toInteger(): 0
        patch = elements[2]? elements[2].toInteger(): 0
    }

    @Override
    int compareTo(Object v2) {
        if (major > v2.major) return 1

        return 0
    }
}

