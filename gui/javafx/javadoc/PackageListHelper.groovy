import groovy.transform.Canonical
import java.nio.file.Path
import java.nio.file.Paths

@Canonical
class PackageListHelper {

    final Path packageListFile
    final List packageList

    PackageListHelper(String packageFile) {
        packageListFile = Paths.get(packageFile)
        packageList = packageListFile.readLines().asImmutable()
    }

    static packageToPath(String pkg) {
        final char separator = '.'
        pkg.replace(separator, File.separatorChar)
    }
}
