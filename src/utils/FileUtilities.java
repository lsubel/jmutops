package utils;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class FileUtilities {

	
	
	/**
	 * Check if there is a file with the name {@code filename} in the list of {@code iter}.
	 * @param filename The file name to check.
	 * @param iter
	 * @return The {@link File} if it was found, otherwise null.
	 */
	public static File findFile(String filename, Iterator<File> iter) {
		// reset iterator
		for(; iter.hasNext(); ) {
			File temp_file = iter.next();
			if(temp_file.getName().equals(filename)) {
				return temp_file;
			}
		}
		return null;
	}
	
	public static boolean containsClasspathFile(Iterator<File> iter) {
		return (findFile(".classpath", iter) != null);
	}
	
	public static File getClasspathFile(Iterator<File> iter) {
		return findFile(".classpath", iter);
	}
	
	public static File getClasspathFile(File file) {
		File traversedFile = file.getParentFile();
		
		while(traversedFile != null) {
			// if in this folder is a .classpath, we stop
			Iterator<File> folder_content = FileUtils.iterateFiles(traversedFile, null, false);
			File found = FileUtilities.findFile(".classpath", folder_content);
			if(found != null) {
				return found;
			}
			traversedFile = traversedFile.getParentFile();
		}

		return null;
	}
}
