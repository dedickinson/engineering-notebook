package name.dickinson.duncan.book
/*
 * Copyright 2015 Duncan Dickinson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.nio.file.Path
import java.nio.file.Paths


/**
 * 
 * Represents a book section
 * 
 * Note: EqualsAndHashCode ignores previousSection and NextSection as including these 
 * causes stack overflows
 * 
 * @author Duncan Dickinson
 * @see <a href="http://docbook.org/tdg5/en/html/section.html">DocBook Section element</a>
 */
@ToString(includeNames=true)
@EqualsAndHashCode(excludes=['previousSection', 'nextSection'])
class Section {
	
	/** The section in which this section is housed */
	Section parent
	
	/** The section's title */
	String title
	
	String description
	
	/** The file path for the section content */
	Path path

	String baseFileName
	
	String pathToRoot = ''
	
	String relPath = ''
	
	String href = ''
	
	String htmlFile = ''
	
	Section previousSection = null
	
	Section nextSection = null
		
	/** Section metadata */
	EnumMap<MetadataField, String> metadata

	private Section() {}

	/**
	 * @param metadata The metadata for the section (the title is extracted from this)
	 * @param path The location of the content file
	 */
	public Section(EnumMap<MetadataField, String> metadata, Path path, String baseFileName, Section parent = null, String pathToRoot = '', String relPath = "") {
		this.title = metadata.get(MetadataField.TITLE)?:''
		this.description = metadata.get(MetadataField.DESCRIPTION)?:''
		this.setPath(path)
		if (pathToRoot) this.pathToRoot = pathToRoot
		if (relPath) this.relPath = relPath
		this.metadata = metadata.clone()
		this.baseFileName = baseFileName
		this.htmlFile = "${baseFileName}.html"
		this.href = "${relPath}/${this.htmlFile}"
	}
	
	/**
	 * Sets the file path for the section
	 * @param path
	 */
	void setPath(Path path) {
		this.path = path.toAbsolutePath()
	}
	
	void setPath(String path) {
		this.setPath(Paths.get(path))
	}
	
	
	public determineIssues() {
		def issueList = []
		if (!metadata.containsKey(MetadataField.TITLE)){
			issueList << "$path does not have a title."
		}
		if (!metadata.containsKey(MetadataField.DESCRIPTION)){
			issueList << "$path does not have a description."
		}
		if (!metadata.containsKey(MetadataField.STATUS)){
			issueList << "$path does not have a status."
		}
		return issueList
	}
	
	public String prettyPrint(String prefix = '') {
		def md = ""
		for (m in metadata) {
			md <<= "${prefix}    ${m.key}: ${m.value}\n"
		}
	
"""
${prefix}$title
${prefix}  path: $path
${prefix}  baseFileName: $baseFileName
${prefix}  relPath: $relPath
${prefix}  href: $href
${prefix}  pathToRoot: $pathToRoot
${prefix}  previous: ${previousSection?.title}
${prefix}  next: ${nextSection?.title}
${prefix}  metadata: 
$md
"""	
	}
}

