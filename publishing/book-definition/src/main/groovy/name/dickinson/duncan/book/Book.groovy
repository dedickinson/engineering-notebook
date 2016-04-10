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
import groovy.transform.Canonical
import groovy.transform.ToString

import java.nio.file.Path

/**
 * Describes a book
 * 
 * Apologies for the obvious descriptions :)
 * 
 * @author Duncan Dickinson
 *
 */
@Canonical
class Book {
	
	/** The book's filesystem path */
	Path path
	
	/** The book's YAML configuration */
	Path configurationPath
	
	/** The base path of the text content */
	Path contentPath
	
	/** The path for the books 'parts' */
	Path contentPartsPath
	
	/** The book's title */
	String title
	
	/** The book's edition (version) */
	String edition
	
	/** A brief blurb for the book (aka abstract) */
	String description

	/** A set of copyright assertions */
	Copyright[] copyright
	
	/** A list of authors */
	Person[] authorgroup

	/** The cover page of the book */
	Section cover
	
	/** A dedication section */
	Section dedication
	
	/** A preface to the book */
	Section preface
	
	/** A section listing trademarks etc */
	Section legalnotice
	
	/** The copyright and reproduction details/licence */
	Section licence
	
	/** A table of contents - most likely auto-generated */
	Section contents
	
	/** The book's introduction */
	Section introduction
	
	/** The references section */
	Section bibliography
	
	/** Helps demystify terms */
	Section glossary
	
	/** For the book nerds */
	Section colophon

	/** 
	 * The book parts
	 * @see <a href="http://docbook.org/tdg5/en/html/part.html">DocBook Part element</a>
	 */
	private final LinkedList<Part> parts = new LinkedList<Part>()

	/**
	 * Append a part to the book
	 * @param part The part
	 */
	public void addPart(Part part) {
		//part.parent = this
		this.parts.add(part)
	}

	/**
	 * 
	 * @return a clone of the book's various Parts
	 */
	//public LinkedList<Part> getParts(){
	//	return this.parts.clone()
	//}
	
	public determineIssues() {
		def issueList = []
		
		for (part in parts) {
			issueList += part.determineIssues()
		}
		
		return issueList
	}
	
	/** The book's filesystem path */
	Path setPath(Path path) {
		this.path = path.toAbsolutePath()
		return this.path
	}
	
	/** The book's YAML configuration */
	Path setConfigurationPath(Path path){
		this.configurationPath = path.toAbsolutePath()
		return this.configurationPath
	}
	
	/** The base path of the text content */
	Path setContentPath(Path path) {
		this.contentPath = path.toAbsolutePath()
		return this.contentPath
	}
	
	/** The path for the books 'parts' */
	Path setContentPartsPath(Path path){
		this.contentPartsPath = path.toAbsolutePath()
		return this.contentPartsPath
	}
	
	public String prettyPrint() {
		def plist = ""
		for (part in parts) {
			plist <<= part.prettyPrint()
		}
		
		def auths = ""
		for (auth in authorgroup) {
			auths <<= "  - ${auth.prettyPrint()}\n"
		}
	
"""
title: $title
edition: $edition
description: $description
path: $path
configurationPath: $configurationPath
contentPath: $contentPath
contentsPartsPath: $contentPartsPath
authors: 
$auths

FRONT MATTER
------------
Cover: ${cover?.prettyPrint('  ')}

Dedication: ${dedication?.prettyPrint('  ')}

Preface: ${preface?.prettyPrint('  ')}

Legal notices: ${legalnotice?.prettyPrint('  ')}

Licence: ${licence?.prettyPrint('  ')}

Introduction: ${introduction?.prettyPrint('  ')}

PARTS
-----
$plist

BACK MATTER
-----------
Bibliography: ${bibliography?.prettyPrint('  ')}

Glossary: ${glossary?.prettyPrint('  ')}

Colophon: ${colophon?.prettyPrint('  ')}
"""
	}
	
}

/**
 * A very basic method for describing a copyright period
 * @author Duncan Dickinson
 *
 */
@ToString(includeNames=true)
class Copyright {
	/** A list of the years covered by this copyright */
	String[] years
	
	/** A list of names - one entry per copyright holder */
	String[] holders
}

/**
 * The basic details for a person (such as an author)
 * @author Duncan Dickinson
 * @see <a href="http://docbook.org/tdg5/en/html/author.html">DocBook Author element</a>
 */
@ToString(includeNames=true)
class Person {
	String honorific = '', firstname= '', surname = '', othername = '', email = '', authorblurb = ''
	public String prettyPrint() {
		"$honorific $firstname $othername $surname ($email) - $authorblurb"
	}
}