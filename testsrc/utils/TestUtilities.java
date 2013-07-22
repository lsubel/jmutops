package utils;

import java.io.File;

public class TestUtilities {

	public static String getTempDir(){
        String property = "java.io.tmpdir";
        
        // Get the temporary directory and print it.
        return System.getProperty(property);
	}
	
	public static File getFolder(String path){
		File tempFolder = new File(path);
		if(!tempFolder.exists() || !tempFolder.isDirectory()){
			tempFolder.mkdir();
		}
		return tempFolder;
	}
	
	public static File getDefaultPrefixFolder(){
		File tempFolder = new File(getTempDir() + File.separator + Settings.DEFAULT_PREFIX_FOLDER);
		if(!tempFolder.exists() || !tempFolder.isDirectory()){
			tempFolder.mkdir();
		}
		return tempFolder;		
	}
	
	public static File getDefaultPostfixFolder(){
		File tempFolder = new File(getTempDir() + File.separator + Settings.DEFAULT_POSTFIX_FOLDER);
		if(!tempFolder.exists() || !tempFolder.isDirectory()){
			tempFolder.mkdir();
		}
		return tempFolder;		
	}
}
