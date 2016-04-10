import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path

@Grab(group='org.yaml', module='snakeyaml', version='1.14') 
import org.yaml.snakeyaml.Yaml

class Loader {
    /**
     * Performs basic pre-requisite check that the requested file exists
     * @param file the file being requested
     * @return a Path instance for the file
     * @throws FileNotFoundException when the file does not exist
     */
    static private Path checkFile(String file) throws FileNotFoundException {
        def path = Paths.get(file).toAbsolutePath()
        if (Files.notExists(path)) {
            throw new FileNotFoundException("File: ${path}")
        }
        return path
    }
}

trait MarkdownMetadata {
    /**
     * Reads a markdown file's metadata. This is based on Multimarkdown's use of a YAML-ish 
     * metadata header at the top of a document.
     * @see <a href="http://fletcher.github.io/MultiMarkdown-4/metadata">Multimarkdown documentation</a>
     * @param markdownFile the path and filename for the markdown file to be read
     * @return a Map of the metadata elements
     * @throws FileNotFoundException if the requested file does not exist or can't be read
     * @throws IllegalStateException if the attempt to read the YAML file yielded a null value
     */
    static public Map<String, String> loadMetadata(String markdownFile)
    throws FileNotFoundException, IllegalStateException {
        // Pre: The requested file must exist
        Path path = Loader.checkFile(markdownFile)

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
                metaBuffer << "$text\n"
            }
        }
        
        Yaml yaml = new Yaml()
        Map<String,String> metadata = yaml.load(metaBuffer.toString())
        input.close()

        // Post: Never return null
        if (metadata == null) {
            throw new IllegalStateException('The markdown file did not appear to have metadata')
        }
        
        return metadata
    }
}

interface MarkdownProcessor {
    public String convertFile(String origin, String output, String outputFormat)
    
    public String getVersion()
}

class MultiMarkdown implements MarkdownProcessor, MarkdownMetadata {

    static private String bin = 'multimarkdown'
    
    static final enum FORMATS {
        html, latex, beamer, memoir, odf, opml, lyx
    }
    
    public static String multimarkdown(params, wait = 1000, String stdin = '') {
        def sout = new StringBuffer()
        def serr = new StringBuffer()
        
        def process = "$bin $params".execute()
        
        process.consumeProcessOutput(sout, serr)
        process.waitForOrKill(wait)

        if (process.exitValue() == 0) {
            //process succeeded
            return sout
        } else {
            throw new Exception("${process.exitValue()}: $serr")
        }
    }
    
    public String convertFile(String origin, String output = "${origin}.html", String outputFormat ='html') {
        return multimarkdown("--output=$output --to=$outputFormat $origin")
    }
    
    public String getVersion() {
        multimarkdown('-v')
    }
    
    public getOutputFormats() {
        return FORMATS
    }
}

MultiMarkdown mmd = new MultiMarkdown()
println mmd.getVersion()
println mmd.convertFile('test.md')
println mmd.loadMetadata('test.md')