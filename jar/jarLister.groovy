import groovy.xml.MarkupBuilder
import groovy.xml.MarkupBuilderHelper

import java.util.jar.JarFile

import static java.util.zip.ZipFile.OPEN_READ

//Lists the items in a jar File
//Excludes META-INF dir

JarFile jar = new JarFile(new File('../lib/tika-parsers-1.12-sources.jar'), true, OPEN_READ)

jar.stream().
        filter { !(it.name =~ ~/META-INF\//) }.
        filter { !it.isDirectory() }.
        each {
            println it.name
        }
