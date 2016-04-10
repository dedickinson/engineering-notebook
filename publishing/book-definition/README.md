#NOTE

I have moved the book construction away from this system and over to [LeanPub](https://leanpub.com/groovytutorial). This repo is kept purely for sentimental reasons and is unlikely to be tended to.

Trust nothing below this line.

---

#BookBuilder

A basic tool for collating a number of [MultiMarkdown](http://fletcherpenney.net/multimarkdown/) files into a book. 

Primarily (only) utilised on building the [Groovy Tutorial](https://bitbucket.org/groovytutorial/groovy-tutorial) source.

To make working with the system a little easier, there are two subtrees: 

- book: The source text
	- <https://bitbucket.org/groovytutorial/groovy-tutorial>
- draft-site: The bitbucket public site I publish the work-in-progress
	- <https://bitbucket.org/groovytutorial/groovytutorial.bitbucket.org>
	- See it live: <http://draft.groovy-tutorial.org>

#Working with this code

- To build the book/site run `./gradlew run`
- `./server.sh` will start a basic HTTP server for easy viewing of the site in a browser
- `./copydraft.sh` just copies the prepared book text to draft-site/.

# Book format

I've tried to use mainly [DocBook](http://docbook.org/tdg5/en/html/docbook.html) elements. The book is laid out in a manner that tries to reflect the book structure:

- A `book.yaml` file holds the book's metadata
- A `book` directory contains front- and back-matter and a `parts` directory
    - All front- and back-matter is optional
- The `parts` directory contains only sub-directories - 1 for each book part
- Each book part directory contains a set of markdown files - 1 for each chapter in the part
	- A `resources` sub-directory can be used. This is just copied verbatim when the book is built
	   - Any other sub-directory is ignored - useful if you want to use transclusion as you can place files into dirs such as `include`.
    - Each markdown (`.md`) file can contain metadata [as per the MultiMarkdown format](http://fletcher.github.io/MultiMarkdown-4/MMD_Users_Guide.html#metadata)

The system will generate a table of contents for you.

## Directory layout
The following directory layout is required:

- `book.yaml` - YAML file containing the book's metadata
- `book/` - Front- and back-matter (elements mostly based on DocBook) including:
    - `Cover.md`
    - `Dedication.md`
    - `Preface.md`
    - `Licence.md`
    - `LegalNotice.md`
    - `Introduction.md`
    - `Colophon.md`
    - `parts/`
        - `[ChapterName]/`
            - `[MarkdownFile].md`

To aid in constructing a larger set of text I've used to following:

- Sub-directories in `parts/` and the chapter files can be prefixed with `nn_` where `nn` is a two-digit number such as `01`.
    - The `nn_` is only processed as a manner of sorting
        - This lets you leave gaps such as `10_` followed by `15_` - handy if you think you'll need to put some chapters in-between
    - If `nn__` (that's 2 underscores) is used then that part directory or chapter file is ignored
- A chapter (markdown) file named `00_Introduction.md` is used in the following manner:
    - The `title` metadata field is used as the Part title
    - The `description` metadata field is used as the Part's description
        - This is used in a "descriptive" table of contents. 
    - Any text in the body of the markdown file is used as a preface to the Part.
        - Avoid headings, keep it short.
	
## Book metadata
The metadata for the book is held in `book.yaml` - using the [YAML](http://yaml.org) format. The following fields should be provided:

- `title`
- `edition`
- `description` (rather than 'abstract')
- `authorgroup`
    - A list of author objects, each with a `firstname`, `surname` and `email`

Note: Refer to the [edition element](http://docbook.org/tdg5/en/html/info.html) of DocBook.

### Example

```yaml
--- 
title: A Book of Examples
edition: 0.0.1-DRAFT
description: An introductory book
authorgroup:
  - firstname: Fred
    surname: Nurk
    email: fred@nurk.com
---
```

## Markdown metadata fields

Markdown files can have any valid MultiMarkdown metadata but I focus on:

- `title`
    - Where a title is not provided, the filename (sans extension and `nn_`) is used.
- `description`
    - As "abstract" is difficult to use when mapping to Java I've used "description" as the the element name.
- `status` - One of:
    - `complete`
    - `under-review`
    - `draft` (mostly done)
    - `in-progress` (early days)
    - `not-started` (chicken scratchings)
    
# Working with git

(See: <http://blogs.atlassian.com/2013/05/alternatives-to-git-submodule-git-subtree/>)

1. `src/main/book` is a subtree -> <git@bitbucket.org:groovytutorial/groovy-tutorial.git>
2. `src/main/draft` is a subtree -> <git@bitbucket.org:groovytutorial/groovytutorial.bitbucket.org.git>

Add the remotes:

```bash
git remote add book git@bitbucket.org:groovytutorial/groovy-tutorial.git
git remote add draft git@bitbucket.org:groovytutorial/groovytutorial.bitbucket.org.git
```

To pull:

```bash
git subtree pull -P src/main/book book master
git subtree pull -P src/main/draft draft master
```