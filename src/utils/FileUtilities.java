package utils;

import java.io.File;
import java.util.Iterator;

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
	
	
}
