//See: https://github.com/javaparser/javaparser/wiki/Manual
@Grab('com.github.javaparser:javaparser-core:2.2.2')
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.util.jar.JarFile
import static java.util.zip.ZipFile.OPEN_READ

import static com.github.javaparser.ast.body.ModifierSet.*

/*
 * Extracts a SOURCE file from a jar and displays the Java
 * class's details. 
 */

class Util {
    static List modifierString(int modifiers) {
        [isAbstract(modifiers) ? 'abstract' : '',
         isStatic(modifiers) ? 'static' : '',
         getAccessSpecifier(modifiers),
         isFinal(modifiers) ? 'final' : '']
    }

    static checkVisibility(int modifier, Closure closure) {
        if (!isPrivate(modifier)) {
            closure()
        }
    }
}

class MethodVisitor extends VoidVisitorAdapter {
    @Override
    public void visit(MethodDeclaration n, Object arg) {
        Util.checkVisibility(n.modifiers) {
            def mods = Util.modifierString(n.modifiers)
            def params = n.parameters*.type.join(', ')
            def thrown = n.throws*.name.join(', ')
            println "${mods.join(' ')} ${n.type} $n.name ($params) ${thrown ? "throws $thrown" : ''}"
        }
        super.visit(n, arg)
    }
}

class FieldVisitor extends VoidVisitorAdapter {
    @Override
    public void visit(FieldDeclaration n, Object arg) {
        Util.checkVisibility(n.modifiers) {
            def mods = Util.modifierString(n.modifiers)
            println "${mods.join(' ')} ${n.type} ${(n.variables*.id.name).join(', ')}"
        }
        super.visit(n, arg)
    }
}

interface TypeClassification {
    String getName()
}

class EnumType implements TypeClassification {
    final String name = 'enum'
}

class ClassType implements TypeClassification {
    final String name = 'class'
}

class InterfaceType implements TypeClassification {
    final String name = 'interface'
}

class TypeFactory {
    static TypeClassification getType(TypeDeclaration type) {
        switch (type.class) {
            case ClassOrInterfaceDeclaration:
                return type.isInterface() ? new InterfaceType() : new ClassType()
                break
            case EnumDeclaration:
                return new EnumType()
                break
        }
    }
}

JarFile sources = new JarFile(new File('../lib/tika-parsers-1.12-sources.jar'), true, OPEN_READ)
InputStream input = sources.getInputStream(sources.getEntry('org/apache/tika/parser/audio/AudioParser.java'))

CompilationUnit cu = JavaParser.parse(input)

println "Package: $cu.package"

cu.types.each { type ->
    Util.checkVisibility(type.modifiers) {
        def classifier = TypeFactory.getType(type)

        println "CLASS: ${getAccessSpecifier(type.modifiers)} $classifier.name $type.name"
        if (classifier in [ClassType, InterfaceType]) {
            println " - Extends: ${type.implements}"
        }
        println 'FIELDS'
        new FieldVisitor().visit(cu, null)
        println 'MEMBERS'
        new MethodVisitor().visit(cu, null)
    }
}

println 'IMPORTS: '
cu.imports.each {
    println "  - $it.name"
}
