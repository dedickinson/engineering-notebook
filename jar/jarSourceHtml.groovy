import groovy.xml.StreamingMarkupBuilder

import java.util.jar.JarFile

import static java.util.zip.ZipFile.OPEN_READ

//Extracts a source file from a JAR and creates a basic HTML rendition
//TODO: Check out https://github.com/codelibs/jhighlight

JarFile sources = new JarFile(new File('../lib/tika-parsers-1.12-sources.jar'), true, OPEN_READ)

sources.getInputStream(sources.getEntry('org/apache/tika/parser/audio/AudioParser.java')).withReader { reader ->
    def builder = new StreamingMarkupBuilder()
    def html = builder.bind {
        mkp.yieldUnescaped('<!DOCTYPE html>')
        html {
            head {
                title('AudioParser.java')
                link rel: "stylesheet", href: "highlight/styles/github.css"
                script(src: 'highlight/highlight.pack.js', '')
                script('hljs.initHighlightingOnLoad();')

            }
            body {
                pre {
                    code{
                        mkp.yieldUnescaped("\n$reader.text")
                    }
                }
            }
        }
    }

    StringWriter writer = new StringWriter()
    writer << html
    print writer.toString()
}
