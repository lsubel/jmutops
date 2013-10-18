
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import jmutops.JMutOps;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;

import results.EventLogger;
import results.ResultCreator;
import results.ResultDatabase;
import enums.OptionsVersion;


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
		if(args.length != 3) {
			System.out.println("Please set up exactly three parameters. You provided " + args.length + ".");
			System.exit(0);
		}
		
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
		String pathToSources = args[2];
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
		jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		jmutops.setOptions(options, OptionsVersion.PREFIX);
		jmutops.setOptions(options, OptionsVersion.POSTFIX);  
		
		// load the properties
		Properties properties = new Properties();
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream("results.properties"));
			properties.loadFromXML(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// register the user defined JMutOpsEventListener
		boolean regEventLogger 		= properties.getProperty("eventlogger-active").equals("true");
		boolean regResultcreator 	= properties.getProperty("resultcreator-active").equals("true");
		boolean regDbResult 		= properties.getProperty("db-active").equals("true");
		if(regEventLogger) {
			boolean console = properties.getProperty("eventlogger-output-console").equals("true");
			boolean file	= properties.getProperty("eventlogger-output-file").equals("true");
			String filePath	= properties.getProperty("eventlogger-output-file-path");
			if(filePath != "") {
				filePath += File.separator + iBugs_ID + "_event.log";
			}
			else {
				filePath = iBugs_ID + "_event.log";
			}
			try {
				EventLogger el = new EventLogger(console, file, filePath);
				jmutops.addEventListener(el);
			} catch (Exception e) {
				System.out.println("Could not initialize EventLogger: " + "\n" + e.getMessage());
			}
		}
		if(regResultcreator) {
			boolean console = properties.getProperty("resultcreator-output-console").equals("true");
			boolean file	= properties.getProperty("resultcreator-output-file").equals("true");
			String filePath	= properties.getProperty("resultcreator-output-file-path");
			if(filePath != "") {
				filePath += File.separator + iBugs_ID + "_result.txt";
			}
			else {
				filePath = iBugs_ID + "_result.txt";
			}
			try {
				ResultCreator rc = new ResultCreator(console, file, filePath);
				jmutops.addEventListener(rc);
			} catch (Exception e) {
				System.out.println("Could not initialize ResultCreator: " + "\n" + e.getMessage());
			}
		}
		if(regDbResult) {
			String address 	= properties.getProperty("db-address");
			String name 	= properties.getProperty("db-username");
			String pw 		= properties.getProperty("db-password");
			try {
				ResultDatabase rd = new ResultDatabase(address, name, pw);
				jmutops.addEventListener(rd);
			} catch (Exception e) {
				System.out.println("Could not initialize ResultDatabase: " + "\n" + e.getMessage());
			}
		}
		
		// initialise program and bug
		jmutops.initializeProgram("iBugs", "", "", "");
		jmutops.initializeBugreport(new Integer(iBugs_ID).toString(), "");
		
		// look for source folders in pathToSources
		checkForSrc(jmutops, new File[]{new File(prefixSourceFolder)}, OptionsVersion.PREFIX);
		checkForSrc(jmutops, new File[]{new File(postfixSourceFolder)}, OptionsVersion.POSTFIX);
		
		// look for class folders in pathToSources
		checkForClassfiles(jmutops, new File[]{new File(prefixSourceFolder)}, OptionsVersion.PREFIX);
		checkForClassfiles(jmutops, new File[]{new File(postfixSourceFolder)}, OptionsVersion.POSTFIX);
		
		// check each file in the prefix folder
		for(File prefixFile: prefixFoldercontent){
			File postfixFile = new File(postfixFolder + File.separator + prefixFile.getName());
			
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

	public static void checkForClassfiles(JMutOps jmutops, File[] folderContent, OptionsVersion version){
		for(File file: folderContent){
			// if found a folder, search in it
			if(file.isDirectory()){
				// check all files in it
				checkForClassfiles(jmutops, file.listFiles(), version);
			}
			// if file is a jar, add it to classfiles
			else if(FilenameUtils.isExtension(file.getName(),"jar")) {
				jmutops.addClasspathEntry(file.getAbsolutePath(), version);
			}
		}
	}
	
}
