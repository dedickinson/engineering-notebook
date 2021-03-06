import org.jboss.windup.decompiler.api.DecompilationListener
import org.jboss.windup.decompiler.api.DecompilationResult
@Grab('org.jboss.windup.decompiler:decompiler-fernflower:2.5.0.CR2')
import org.jboss.windup.decompiler.fernflower.FernflowerDecompiler
import org.jboss.windup.decompiler.util.Filter

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.FileSystems
import java.nio.file.PathMatcher

/*
 * Decompiles an entire JAR file
 * See: http://windup.github.io/windup/docs/latest/javadoc/
 * See: https://github.com/windup/windup/wiki
 *
 * TODO: The decompiler appears to ignore inner classes (files such as HtmlParser$HtmlParserMapper )
 */

 String library = 'tika-parsers-1.12'
 Path rootDir = Paths.get("output/extract/$library/")
 Path outputDir = Paths.get("output/decompile/$library/")
 Path jarFile = Paths.get("../lib/${library}.jar")

class FilterForClassFiles implements Filter {
    @Override
    Filter.Result decide(Object o) {
        if (!o in java.util.jar.JarFile$JarFileEntry) {
            return Filter.Result.REJECT
        }

        if (o.directory) {
            return Filter.Result.REJECT
        }

        PathMatcher matcher = FileSystems.default.getPathMatcher('glob:**.class')
        if (! matcher.matches(Paths.get(o.name))) {
            return Filter.Result.REJECT
        }
        return Filter.Result.ACCEPT
    }
}

class DecompilationListenerImpl implements DecompilationListener {

    @Override
    void fileDecompiled(List<String> sourceClassPaths,
                        String outputPath) {
        //println "DECOMPILED - $sourceClassPaths: $outputPath"
    }

    @Override
    void decompilationFailed(List<String> sourceClassPaths,
                             String message) {
        //println "FAILED - $sourceClassPaths: $message"
    }

    @Override
    void decompilationProcessComplete() {
        //println 'DECOMPILATION COMPLETE'
    }
}

FernflowerDecompiler decompiler = new FernflowerDecompiler()

DecompilationResult result = decompiler.decompileArchiveImpl jarFile,
        outputDir, new FilterForClassFiles(), new DecompilationListenerImpl()

println 'DECOMPILED FILES:'
result.decompiledFiles.each { src, dest ->
    println " - $dest"
}

if (result.failures) {
    println 'DECOMPILATION FAILURES:'
    result.failures.each{ failure ->
        println " - $failure.path $failure.cause [$failure.message]"
    }
}
