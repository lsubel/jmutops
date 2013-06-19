
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import jmutops.JMutOps;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;

import enums.OptionsVersion;

import results.ApplicationCounter;
import results.EventLogger;
import results.ResultsFileWriter;

import utils.Settings;


public class AlessandraVMApplication {

	private static final String PREFIX = "pre-fix";
	private static final String POSTFIX = "post-fix";
	
	/**
	 * Runs an check for one specific iBugs ID.
	 * @param args Expected parameters:<p>
	 * 1) path to the folder containing all ID folders<p>
	 * 2) the iBugs ID to check<p>
	 * 3) path to the folder containing the sources <p>
	 * <p>
	 * I assume a folder hierarchy in the following way: <p>
	 * ./ID/prefix/prefix.jar <p>
	 * ./ID/prefix/all different .java files <p>
	 * ./ID/postfix/postfix.jar <p>
	 * ./ID/postfix/all different .java files <p>
	 */
	public static void main(String[] args) {
		// parse the parameters
		String pathToIDFolders 	= args[0];
		if(pathToIDFolders == null){
			System.out.println("Please set up the first parameter.");
			System.exit(0);
		}
		int iBugs_ID = 0;
		try {
			iBugs_ID = Integer.parseInt(args[1]);
		} catch (NumberFormatException e1) {
			System.out.println("Please set up a correct integer as second parameter.");
			System.exit(0);			
		}
		String pathToSources 	= args[2];
		if(pathToSources == null){
			System.out.println("Please set up the third parameter.");
			System.exit(0);
		}
		
		// extract the different paths
		String initialPath = pathToIDFolders + File.separator + iBugs_ID;
		String prefixFolder  = initialPath + File.separator + PREFIX;
		String postfixFolder = initialPath + File.separator + POSTFIX;
		File[] prefixFoldercontent  = new File(prefixFolder).listFiles();
		File[] postfixFoldercontent = new File(postfixFolder).listFiles();
		String prefixSourceFolder 	= pathToSources + File.separator + PREFIX;
		String postfixSourceFolder 	= pathToSources + File.separator + POSTFIX;
		
		// initialize jMutOps
		JMutOps jmutops = new JMutOps();
		jmutops.initProgram("iBugs");
		jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		jmutops.setOptions(options, OptionsVersion.PREFIX);
		jmutops.setOptions(options, OptionsVersion.POSTFIX);  
		
		// 
		ResultsFileWriter fr = new ResultsFileWriter();
		fr.setResultingFileName("results_" + iBugs_ID + ".txt");
		jmutops.addResultListener(fr);
		
		EventLogger ol = new EventLogger();
		jmutops.addResultListener(ol);
		
		// look for source folders in pathToSources
		checkForSrc(jmutops, new File[]{new File(prefixSourceFolder)}, OptionsVersion.PREFIX);
		checkForSrc(jmutops, new File[]{new File(postfixSourceFolder)}, OptionsVersion.POSTFIX);
		
		// check each file in the prefix folder
		for(File prefixFile: prefixFoldercontent){
			File postfixFile = new File(postfixFolder + "\\" + prefixFile.getName());
			
			// check for file existance
			if((!prefixFile.exists()) || (!postfixFile.exists())){
				continue;
			}
			
			// check both files to be .java
			if( !(FilenameUtils.isExtension(prefixFile.getName(),"java")) || !(FilenameUtils.isExtension(postfixFile.getName(),"java"))){
				continue;
			}
			
			try {
				jmutops.checkFiles(prefixFile, postfixFile);
			} catch (Exception e) {
				System.out.println("Exception found: " + e.getMessage());
			}
							
		}
		
		
		jmutops.createResults();
	}
	
	public static void checkForSrc(JMutOps jmutops, File[] folderContent, OptionsVersion version){
		for(File file: folderContent){
			if(file.isDirectory()){
				if(file.getName().equals("src")){
					jmutops.addSourcepathEntry(file.getAbsolutePath(), null, version);
				}else{
					checkForSrc(jmutops, file.listFiles(), version);
				}
			}
		}
	}

}
