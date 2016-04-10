package name.dickinson.duncan.book.util

import static groovy.io.FileType.*
import static name.dickinson.duncan.book.util.FileCheck.*
import static name.dickinson.duncan.book.util.Loader.BOOK_BACK_MATTER
import static name.dickinson.duncan.book.util.Loader.BOOK_FRONT_MATTER
import static name.dickinson.duncan.book.util.Loader.BOOK_GENERAL_MATTER
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

import name.dickinson.duncan.book.Book
import name.dickinson.duncan.book.Part
import name.dickinson.duncan.book.Section
import name.dickinson.duncan.book.converter.markdown.MultiMarkdown

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.FileFilterUtils
import org.apache.commons.io.filefilter.IOFileFilter

@Slf4j
class Generator {

	static final String[] STATIC_DIRS = ['resources'].asImmutable()
	static final IOFileFilter STATIC_FILE_EXCLUDE = FileFilterUtils.notFileFilter(FileFilterUtils.prefixFileFilter('.'))
	static final String SNIPPETS_DIR = 'snippets'
	static final String HTML_DIR = 'html'
	static final String HTML_FULL_DIR = 'html-full'
	static final String SITE_DIR = 'site'
	static final String TMP = 'tmp'
	static final Template COVER_TEMPLATE
	static final Template ARTICLE_TEMPLATE
	static final Template ARTICLE_PART_TEMPLATE
	static final Template BOOK_TOC_TEMPLATE
	static final Template PART_TOC_TEMPLATE
	static final Template HTML_MULTI_TEMPLATE
	static final Path SITE_RESOURCES_STYLE


	static {
		def engine = new SimpleTemplateEngine()
		def cl = Generator.class.getClassLoader()

		def templatePath = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/template/article.html').toURI())
		ARTICLE_TEMPLATE = engine.createTemplate(templatePath.toFile())

		templatePath = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/template/toc-book.html').toURI())
		BOOK_TOC_TEMPLATE = engine.createTemplate(templatePath.toFile())

		templatePath = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/template/toc-part.html').toURI())
		PART_TOC_TEMPLATE = engine.createTemplate(templatePath.toFile())

		templatePath = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/template/article-part.html').toURI())
		ARTICLE_PART_TEMPLATE = engine.createTemplate(templatePath.toFile())

		templatePath = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/template/html-multi.html').toURI())
		HTML_MULTI_TEMPLATE = engine.createTemplate(templatePath.toFile())

		templatePath = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/template/cover.html').toURI())
		COVER_TEMPLATE = engine.createTemplate(templatePath.toFile())

		SITE_RESOURCES_STYLE = Paths.get(cl.getSystemResource('name/dickinson/duncan/book/style/site.css').toURI())
	}

	static private Path cleanBuildPath(Path outputPath) {
		outputPath.deleteDir()
		return outputPath
	}

	static private Path initBuildPath(Path outputPath) {
		FileCheck.checkDirectory(outputPath, true)
	}

	static private createSite(Book book, Path snippetPath, Path sitePath) {

		def partTocPaths = [:]

		def exportPaths = [:]

		//First pass to extract the TOC
		def bookToc = getBookMatterToc(book, BOOK_FRONT_MATTER)

		for (part in book.parts) {
			def partToc = []
			exportPaths[part] =  FileCheck.appendPath(snippetPath, part.relPath)
			for (chapter in part.chapters) {
				partToc <<= ['title': chapter.title,
					'href': chapter.htmlFile]
			}
			def partTocPath = FileCheck.appendPath(exportPaths[part],'toc.html')
			applyTemplate(['toc': partToc, 'title': 'Contents', 'part': part], PART_TOC_TEMPLATE, partTocPath)
			partTocPaths[part] = partTocPath

			//Add part into the book toc
			bookToc <<= [title: part.title,
				href: part.href]
		}
		bookToc += getBookMatterToc(book, BOOK_BACK_MATTER)

		def bookTocPath = FileCheck.appendPath(snippetPath, 'toc.html')
		applyTemplate(['toc': bookToc, 'title': 'Contents'], BOOK_TOC_TEMPLATE, bookTocPath)

		//Second pass to build the book snippets and site
		convertBookMatter(book, BOOK_FRONT_MATTER, bookTocPath, snippetPath, sitePath)
		for (part in book.parts) {
			for (chapter in part.chapters) {
				convertMarkdown(book, chapter, partTocPaths[part], snippetPath, sitePath)
			}
			convertMarkdown(book, part, partTocPaths[part], snippetPath, sitePath)
		}

		convertBookMatter(book, BOOK_BACK_MATTER, bookTocPath, snippetPath, sitePath)

		//Build the "cover"
		applyTemplate(['book': book], COVER_TEMPLATE, FileCheck.appendPath(snippetPath, 'index.html'))
		applyHtmlMultiTemplate(book, null, FileCheck.appendPath(snippetPath, 'index.html'), bookTocPath, FileCheck.appendPath(sitePath, 'index.html'))

	}

	static private getBookMatterToc(Book book, sectionList) {
		def toc = []
		for (type in sectionList) {
			Section section = book."${type.getName().toLowerCase()}"
			if (section) {
				section.href = "${section.baseFileName}.html"
				toc <<= [title: section.title,
					href: section.href]
			}
		}
		return toc
	}

	static private convertBookMatter(Book book, sectionList, Path bookTocPath, snippetPath, sitePath) {
		for (type in sectionList) {
			Section section = book."${type.getName().toLowerCase()}"
			if (section) {
				convertMarkdown(book, section, bookTocPath, snippetPath, sitePath)
			}
		}
	}

	static private convertMarkdown(Book book, Section section, Path tocPath = [:], Path snippetPath, Path sitePath) {
		def srcPath

		if (section instanceof Part) {
			srcPath = section.introduction.path
		} else {
			srcPath = section.path
		}
		def htmlFile = FileCheck.appendPath(snippetPath, section.href).toAbsolutePath()
		log.info "Converting ${srcPath.toString()} to ${htmlFile.toString()}"
		MultiMarkdown.convertFileToHtml(srcPath, htmlFile)

		if (section instanceof Part) {
			//Add in the TOC for the part
			applyArticleTemplate(section, htmlFile, htmlFile, ARTICLE_PART_TEMPLATE)
		} else {
			applyArticleTemplate(section, htmlFile, htmlFile)
		}

		applyHtmlMultiTemplate(book, section, htmlFile, tocPath, FileCheck.appendPath(sitePath, section.href))
	}

	static private applyArticleTemplate(Section section, Path inputPath, Path outputPath, template=ARTICLE_TEMPLATE) {
		def next, previous

		if (section.previousSection) {
			previous = "${section.pathToRoot}${section.previousSection.href}"
		}
		
		
		if (section.nextSection) {
			next = "${section.pathToRoot}${section.nextSection.href}"
		}
		
		def binding = [
			section: section,
			body: inputPath,
			previousLink: previous,
			nextLink: next
		]
		log.info "Applying article template to $outputPath"
		applyTemplate(binding, template, outputPath)
	}

	static private applyHtmlMultiTemplate(Book book, Section section, Path inputPath, Path tocPath, Path outputPath) {
		def binding = [
			book: book,
			section: section,
			body: inputPath,
			toc: tocPath
		]
		log.info "Applying HTML Multi template to $outputPath"
		applyTemplate(binding, HTML_MULTI_TEMPLATE, outputPath)
	}

	static private applyTemplate (binding = [:], Template template, Path outputPath) {
		outputPath.write(template.make(binding).toString())
	}

	static private void prepareSiteStructure(Book book, Path exportPath, Boolean copyResources = false) {
		def baseDir = FileCheck.checkDirectory(exportPath, true)

		//Copy the resources for each part
		for (part in book.parts) {
			def export = determineExportPath(book.contentPath, part.getPath(), baseDir)
			checkDirectory(export, true)
			if (copyResources) copyResourceDirs(part.path, export)
		}

		//Copy the resources for the book
		if (copyResources) {
			Path stylesPath = FileCheck.checkDirectory(FileCheck.appendPath(baseDir, 'resources/styles'), true)
			FileUtils.copyFileToDirectory(SITE_RESOURCES_STYLE.toFile(), stylesPath.toFile())
			copyResourceDirs(book.contentPath, baseDir)
		}
	}

	static Path determineExportPath(inputBasePath, inputPath, exportPath){
		Path rel = inputBasePath.relativize(inputPath)
		return FileCheck.appendPath(exportPath, rel)
	}

	static private void copyResourceDirs(inputPath, exportPath) {
		for (srcDir in STATIC_DIRS) {
			Path origPath
			try {
				origPath = FileCheck.checkDirectory(FileCheck.appendPath(inputPath, srcDir), false)
			} catch (FileNotFoundException e) {
				//This is fine - there's no directories to copy
				continue
			}
			Path newPath = FileCheck.appendPath(exportPath, srcDir)
			FileUtils.copyDirectory(origPath.toFile(), newPath.toFile(), STATIC_FILE_EXCLUDE)
		}
	}


	/**
	 * @param book
	 * @param outputPath
	 * @return
	 */
	static Path generateBook(Book book, Path exportPath) {
		//Prepare the outputPath
		cleanBuildPath(exportPath)
		initBuildPath(exportPath)
		def sitePath = FileCheck.appendPath(exportPath, SITE_DIR)
		def snippetPath = FileCheck.appendPath(exportPath, SNIPPETS_DIR)

		prepareSiteStructure(book, sitePath, true)
		prepareSiteStructure(book, snippetPath, false)

		//Prepare the HTML renditions
		createSite(book, snippetPath, sitePath)


		return exportPath
	}
}
