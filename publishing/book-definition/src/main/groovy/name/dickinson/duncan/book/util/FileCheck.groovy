package name.dickinson.duncan.book.util

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FilenameUtils

class FileCheck {
	/**
	 * Performs basic pre-requisite check that the requested file exists
	 * @param file the file being requested
	 * @return a Path instance for the file
	 * @throws FileNotFoundException when the file does not exist
	 */
	static public Path checkFileBase(String file) throws FileNotFoundException, IllegalStateException {
		return checkFileBase(Paths.get(file))
	}
	
	static public Path checkFileBase(Path path) throws FileNotFoundException, IllegalStateException {
		if (Files.notExists(path)) {
			throw new FileNotFoundException("File: $path")
		}

		if (!Files.isReadable(path)) {
			throw new IllegalStateException("Unable to read $path")
		}
		return path
	}

	static public Path checkDirectory(String dir, create = false) throws FileNotFoundException {
		return checkDirectory(Paths.get(dir), create) 
	}
	
	static public Path checkDirectory(Path path, create = false) throws FileNotFoundException {
		if (create) {
			Files.createDirectories(path)
		}
		checkFileBase(path)
		
		if (!Files.isDirectory(path)) {
			throw new FileNotFoundException("File: $path")
		}
		return path
	}

	static public Path checkFile(String file) throws FileNotFoundException {
		def path = checkFileBase(file)

		if (!Files.isRegularFile(path)) {
			throw new FileNotFoundException("File: $path")
		}
		return path
	}
	
	static public String getTitle(Path path) {
		def name = FilenameUtils.getBaseName(path.toString())
		return name.tokenize('_').last()
	}
	
	static public Path appendPath(p1, p2) {
		return Paths.get(p1.toString(), p2.toString())
	}
}
