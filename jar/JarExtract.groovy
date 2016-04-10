import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.util.jar.JarFile
import static java.util.zip.ZipFile.OPEN_READ

String library = 'tika-parsers-1.12'
Path extractDir = Paths.get("output/extract/$library/")
Path jarFile = Paths.get("../lib/${library}.jar")

JarFile jar = new JarFile(jarFile.toFile(), true, OPEN_READ)

jar.stream().filter{ !(it.name =~ ~/META-INF\//) }.forEach { entry ->
    if (entry.isDirectory()) return
    Path p = Paths.get(extractDir.toString(), entry.toString())
    Files.createDirectories p.parent
    p.bytes = jar.getInputStream(entry).bytes
}
