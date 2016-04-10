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

/**
 *
 * Represents a book part
 *
 * @author Duncan Dickinson
 * @see <a href="http://docbook.org/tdg5/en/html/part.html">DocBook Part element</a>
 */
@ToString(includeNames=true)
class Part extends Section {

	/**
	 * The chapters in the part
	 */
	LinkedList<Section> chapters = new LinkedList<Section>()


	/** The introductory blurb for the Part **/
	Section introduction

	private Part() {
	}


	/**
	 * @param metadata
	 * @param path
	 */
	Part(EnumMap<MetadataField, String> metadata, Path path, String pathToRoot = '../../', String relPath = '') {
		super(metadata, path, 'index', null, pathToRoot, relPath)
	}

	/**
	 * 
	 * @param name
	 * @param chapter
	 * @return
	 */
	public addChapter(String name, Section chapter) {
		if (name == SectionType.INTRODUCTION.getName()) {
			chapter.href = this.href
			this.metadata << chapter.metadata
			this.introduction = chapter
			this.title = chapter.title //metadata.get(MetadataField.TITLE)?:this.title
			this.description = chapter.description //metadata.get(MetadataField.DESCRIPTION)?:this.title
		} else {
			this.chapters.add(chapter)
		}
	}

	/*
	@Override
	public String getHref() {
		return this.introduction.href
	}
	*/

	/**
	 * @return a clone of the chapters in the Part
	 */
	public LinkedList<Section> getChapters() {
		return this.chapters.clone()
	}

	public determineIssues() {
		def issueList = super.determineIssues()
		for (ch in chapters) {
			issueList += ch.determineIssues()
		}
		return issueList
	}

	public String prettyPrint() {
		def intro = introduction.prettyPrint('    ')
		def ch = ""
		for (chapter in chapters) {
			ch <<= chapter.prettyPrint('    ')
		}

		"""
${super.prettyPrint()}
  Introduction:
$intro
  Chapters:
$ch
"""
	}
}
