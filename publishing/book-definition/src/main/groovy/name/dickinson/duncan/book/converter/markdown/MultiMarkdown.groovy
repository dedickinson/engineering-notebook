package name.dickinson.duncan.book.converter.markdown

import groovy.util.logging.Slf4j

import java.nio.file.Path

import org.yaml.snakeyaml.Yaml

@Slf4j
class MultiMarkdown {

	static private String bin = 'multimarkdown'

	static final enum FORMATS {
		html, latex, beamer, memoir, odf, opml, lyx
	}

	public static String multimarkdown(params, wait = 1000, String stdin = '') {
		def sout = new StringBuffer()
		def serr = new StringBuffer()
		def cmd = "$bin $params"
		
		log.info "Running: $cmd"
		def process = cmd.execute()

		process.consumeProcessOutput(sout, serr)
		process.waitForOrKill(wait)

		if (process.exitValue() == 0) {
			//process succeeded
			return sout
		} else {
			throw new Exception("Multimarkdown error ${process.exitValue()}: $serr")
		}
	}
	
	public static String convertFileToHtml(origin, output, boolean snippet = true, boolean nolabels = true) {
		def opts = ""
		if (snippet) {
			opts <<= ' --snippet'
		}
		if (nolabels) {
			opts <<= ' --nolabels'
		}
		return multimarkdown("$opts --to=html --output=${output.toString()}  ${origin.toString()}")
	}

	public String getVersion() {
		multimarkdown('-v')
	}

	public getOutputFormats() {
		return FORMATS
	}

	/**
	 * Reads a markdown file's metadata. This is based on Multimarkdown's use of a YAML-ish 
	 * metadata header at the top of a document.
	 * @see <a href="http://fletcher.github.io/MultiMarkdown-4/metadata">Multimarkdown documentation</a>
	 * @param markdownFile the path and filename for the markdown file to be read
	 * @return a Map of the metadata elements
	 * @throws FileNotFoundException if the requested file does not exist or can't be read
	 * @throws IllegalStateException if the attempt to read the YAML file yielded a null value
	 */
	static public Map<String, String> extractMetadata(Path path) {

		def input = path.newReader()

		def lineNumber = 0
		StringBuilder metaBuffer = new StringBuilder()
		for  (def text=''; text=input.readLine(); text != null) {
			lineNumber++
			if (lineNumber == 1 && text != '---') {
				//There's no metadata
				break
			} else if (lineNumber == 1 && text == '---') {
				continue
			} else if (lineNumber != 1 && (text == '---' || text == '...')) {
				//End of metadata
				break
			} else {
				metaBuffer << "${text.replace('\t', '    ')}\n"
			}
		}
		input.close()

		Yaml yaml = new Yaml()
		Map<String,String> metadata = yaml.load(metaBuffer.toString())


		// Post: Never return null
		if (metadata == null) {
			metadata = new HashMap<String, String>()
		}

		return metadata
	}
}