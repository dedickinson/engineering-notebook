package name.dickinson.duncan.book.util

/*
 * Copyright 2015 Duncan Dickinson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *ƒ
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import static groovy.io.FileType.*
import static name.dickinson.duncan.book.SectionType.*
import static name.dickinson.duncan.book.util.FileCheck.*
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

import name.dickinson.duncan.book.Book
import name.dickinson.duncan.book.MetadataField
import name.dickinson.duncan.book.Part
import name.dickinson.duncan.book.Section
import name.dickinson.duncan.book.converter.markdown.MultiMarkdown

import org.yaml.snakeyaml.Yaml

/**
 * 
 * @author Duncan Dickinson
 *
 */
@Slf4j
class Loader {
	/** 
	 * The default base dir in which all the book files are found. 
	 * Can be overridden by a command-line parameter
	 */
	static final String BOOK_BASE_DIR = 'book'

	/** 
	 * The default output directory for the compiled book
	 * Can be overridden by a command-line parameter
	 */
	static final String BOOK_OUTPUT_DIR = "$BOOK_BASE_DIR/output"

	/** The directory containing the front- and back-matter and parts folder */
	static final String BOOK_TEXT_BASE_DIR = 'book'

	/** The directory containing the book parts */
	static final String BOOK_BODY_BASE_DIR = 'parts'

	/** The book's basic metadata file */
	static final String BOOK_CONFIG_FILE = 'book.yaml'

	/** Content file name formats */
	static final FILE_INCLUDES = ~/[0-9]{0,2}_?.*\.(md|mmd)/

	/** Applied to chapters and general-matter */
	static final FILE_IGNORES = [~/[0-9]{0,2}__.*/].asImmutable()


	/**
	 * The sections found under BOOK_TEXT_BASE_DIR that form the front-matter
	 */
	static final def BOOK_FRONT_MATTER = [
		//COVER,
		DEDICATION,
		PREFACE,
		LICENCE,
		LEGAL_NOTICE,
		INTRODUCTION
	].asImmutable()

	/**
	 * The sections found under BOOK_TEXT_BASE_DIR that form the back-matter
	 */
	static final def BOOK_BACK_MATTER = [
		BIBLIOGRAPHY,
		GLOSSARY,
		COLOPHON
	].asImmutable()

	static final BOOK_GENERAL_MATTER = BOOK_FRONT_MATTER + BOOK_BACK_MATTER

	/**
	 * The book sections that will be automatically generated
	 */
	static final BOOK_AUTO_GENERATE = ['contents'].asImmutable()

	static public Boolean checkIgnore(fileName) {
		for(pattern in FILE_IGNORES) {
			if (fileName ==~ pattern) {
				return true
			}
		}
		return false
	}

	/**
	 * Processes a book structure
	 * @param baseDir the book's base directory
	 * @param options a placeholder for future configuration
	 * @return A Book instance populated with metadata
	 * @throws FileNotFoundException if the requested YAML file does not exist or can't be read
	 * @throws IllegalStateException if the attempt to read the YAML file yielded a null value
	 */
	static public Book loadBook(Path baseDir, LinkedHashMap options = [:])
	throws FileNotFoundException, IllegalStateException {

		// Pre: The necessary layout must exist
		Path bookPath = checkDirectory(baseDir)
		Path bookConfig = checkFile("$baseDir/$BOOK_CONFIG_FILE")
		Path bookTextBase = checkDirectory("$baseDir/$BOOK_TEXT_BASE_DIR")
		Path bookBodyMatter = checkDirectory("$baseDir/$BOOK_TEXT_BASE_DIR/$BOOK_BODY_BASE_DIR")

		//Load the YAML structure into the Book object
		Yaml yaml = new Yaml()
		def book = yaml.loadAs(bookConfig.getText(), Book.class)
		book.path = bookPath
		book.configurationPath = bookConfig
		book.contentPath = bookTextBase
		book.contentPartsPath = bookBodyMatter

		loadGeneralMatter(bookTextBase, book)
		loadBodyMatter(bookBodyMatter, book)
		setSectionProgression(book)

		// Post: Never return null
		if (book == null) {
			throw new IllegalStateException('The book could not be generated')
		}

		log.info 'The book has been loaded'
		// Return
		return book
	}

	static private setSectionProgression(book) {
		def sectionList = []

		for (type in BOOK_FRONT_MATTER) {
			def section = book."${type.getName().toLowerCase()}"
			if (!section) {
				continue
			}
			sectionList << section
		}
		for (part in book.parts) {
			sectionList << part
			for (chapter in part.chapters) {
				sectionList << chapter
			}
		}
		for (type in BOOK_BACK_MATTER) {
			def section = book."${type.getName().toLowerCase()}"
			if (!section) {
				continue
			}
			sectionList << section
		}

		Section prev = null
		for (section in sectionList) {
			if (prev) {
				prev.nextSection = section
				section.previousSection = prev
			}
			prev = section
		}
		sectionList = null
		log.info 'Prepared the section progression'
		return book
	}

	static private Book loadGeneralMatter(path, book) {
		def files = [:]
		log.info("Preparing General Matter in $path")
		path.eachFileMatch FILES, FILE_INCLUDES, {
			if (!checkIgnore(it)) {
				String name = getTitle(it)
				files[name] = it
			} else {
				log.info "Ignoring file $it"
			}
		}

		if (files) {
			if (files) {
				for (type in BOOK_GENERAL_MATTER) {
					for (f in files.keySet()) {
						if (type.getName() == f) {
							Section section = loadSection(type.getName(), files[f])
							book."${type.getName().toLowerCase()}" = section
							break
						}
					}
				}
			}
		} else {
			log.info "No General Matter files found in $path"
		}
		return book
	}

	static private Book loadBodyMatter(Path path, Book book) {
		def parts = [:]
		path.eachDir {
			def fname = it.getFileName().toString()
			if (!checkIgnore(fname)) {
				def num = fname.substring(0,2)
				Part part = loadPart(book, it)
				book.addPart part
				log.info "Added part: $part.title ($part.path)"
			} else {
				log.info "Ignored part directory: $fname"
			}
		}
		return book
	}

	static private Part loadPart(Book book, Path path = null) {
		EnumMap<MetadataField, String> metadata = new EnumMap<MetadataField, String>(MetadataField)
		metadata[MetadataField.TITLE] = getTitle(path)

		def relPath = book.contentPath.relativize(path.toAbsolutePath())
		
		Part part = new Part(metadata, path, '../../', relPath.toString())
		log.info "Loading part from $path"
		path.eachFileMatch FILES, FILE_INCLUDES, {
			def fileName = it.getFileName().toString()
			if (!checkIgnore(fileName)) {
				String num = fileName.substring(0,2)
				String name = getTitle(it)
				Section chapter = loadSection(name, it, part)
				part.addChapter name, chapter
				log.info "Added chapter: $chapter.title ($chapter.path)"
			} else {
				log.info "Ignoring file $it"
			}
		}
		return part
	}

	static private Section loadSection(String name, Path path, Part part = null) {
		Section section
		EnumMap<MetadataField, String> metadata

		Map mdMeta = MultiMarkdown.extractMetadata(path)
		if (!mdMeta[MetadataField.TITLE.value]) {
			log.warn "$path does not provide a title metadata field"
			//mdMeta.put(MetadataField.TITLE.value,name)
		}

		metadata = MetadataField.createMetadataMap(mdMeta)

		section = new Section(metadata, path, name, part, part?.pathToRoot, part?.relPath, )
		section.baseFileName = name
		return section
	}

	static private displayDebug(Book book) {
		def line = '=' * 80
		def issues = ""

		for (i in book.determineIssues()) {
			issues <<= "- $i\n"
		}
		print """
$line
BOOK STRUCTURE
$line

${book?.prettyPrint()}
		
$line
POSSIBLE ISSUES
$line
$issues
"""
	}


	static main(args) {

		CliBuilder cli = new CliBuilder(usage:'BookBuilder [options]')
		cli.with{
			h longOpt: 'help', 			'Show usage information'
			i longOpt: 'input', args:1,	'The base directory containing book.yaml and book/. Defaults to a directory named "./book".'
			o longOpt: 'output',args:1,	'The output directory for the book. Defaults to a directory named "./book/output"'
			//d longOpt: 'debug', args:0, 'Display useful book structure info'
			q longOpt: 'quiet', args:0,	'Calm down the feedback'
		}
		OptionAccessor options = cli.parse(args)

		if (options.h) {
			cli.usage()
			return
		}

		//I just use the constant here as a useful default value
		def baseDir = FileCheck.checkDirectory((options.i)?:BOOK_BASE_DIR, false)

		def outputDir = Paths.get((options.o)?:BOOK_OUTPUT_DIR)

		log.info "Loading book from [$baseDir]"
		Book book = loadBook(baseDir)

		log.info "Generating book in [$outputDir]"
		Generator.generateBook(book, outputDir)

		if (!options.q) {
			displayDebug(book)
		}

	}
}
