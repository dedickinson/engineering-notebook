/*
 *    Copyright 2016 Duncan Dickinson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


import static java.nio.file.Files.isReadable

import jdk.nashorn.internal.ir.annotations.Immutable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@Grab('org.jsoup:jsoup:1.8.3')
import java.nio.file.Path
import java.nio.file.Paths

enum JavaDocFiles {
    PACKAGE_LIST('package-list'),
    ALL_CLASSES('allclasses-noframe.html')

    String fileName

    JavaDocFiles(name) {
        fileName = name
    }
}

@Immutable
class JavaDocHelper {

    String baseDir

    List<String> packageList = [ ]

    List classList = [ ]

    JavaDocHelper(String baseDir) {
        baseDir = baseDir

        Path packageListPath = getPath(baseDir, JavaDocFiles.PACKAGE_LIST.fileName)
        packageListPath.eachLine { packageList << it }
        packageList.asImmutable()

        Path classListPath = getPath(baseDir, JavaDocFiles.ALL_CLASSES.fileName)
        extractClassList(classListPath)
        //classList << extractClassList(classListPath)
        //classList.asImmutable()
    }

    private static Path getPath(String baseDir, String... path) {
        Path p = Paths.get(baseDir, path)
        if (!isReadable(p)) {
            throw new FileNotFoundException("$p")
        }
        return p.toAbsolutePath()
    }

    static String pathToPackage(String path){
        path.tokenize('/')[0..-2].join('.')
    }

    static def extractClassList(Path allClassesFile) {
        def listing = [ classes   : [ : ],
                        interfaces: [ : ] ]

        Document doc = Jsoup.parse(allClassesFile.toFile(), 'UTF-8')
        Elements elements = doc.select('div.indexContainer > ul > li > a')

        elements.each { Element el ->
            def iface = el.select('span.interfaceName')
            def pkg, name
            if (iface) {
                name = iface[0].ownText()
                pkg = pathToPackage iface[0].parent().attr('href')
                //TODO listing.interfaces.put
            } else {
                name = el.ownText()
                pkg = pathToPackage el.attr('href')
            }
        }

        listing.classes.each { path, name ->
            println "$name: $path"
        }
        return listing
    }
}

def jdoc = new JavaDocHelper("${System.getProperty("user.home")}/Development/doc/java8/api")

//jdoc.packageList.each {
//    println it
//}

jdoc.classList.each {
    println it
}
