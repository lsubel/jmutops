
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jmutops.JMutOps;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import results.EventLogger;
import results.ResultCreator;
import results.ResultDatabase;
import utils.FileUtilities;
import utils.iBugsTools;
import enums.OptionsVersion;


public class IbugsEvaluationApplication {

	private static final String	PREFIX = "pre-fix";
	private static final String	POSTFIX = "post-fix";
	private static final File	IBUGS_REPOSITORY = new File("repository.xml");
	private static final int	NUMBER_OF_ARGUMENTS = 3;
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
		// check the valid number of parameters
		if(args.length != NUMBER_OF_ARGUMENTS) {
			System.out.println(
				"Please provide arguments to run this application:" + System.lineSeparator() + 
				"	1) path to the folders containing all iBugs ID folders with the different files" + System.lineSeparator() +
				"	2) iBugs ID to test" + System.lineSeparator() +
				"	3) path to folder containing the extracted iBugs sources" + System.lineSeparator()
			);
			System.exit(0);
		}
		// extract the parameters
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
		
		// extract the path with the different files
		String strPathRoot 		= pathToIDFolders + File.separator + iBugs_ID;
		String strPathPrefix  	= strPathRoot + File.separator + PREFIX;
		String strPathPostfix 	= strPathRoot + File.separator + POSTFIX;
		File[] arrayFilesPrefix  = new File(strPathPrefix).listFiles();
		File[] arrayFilesPostfix = new File(strPathPostfix).listFiles();
		
		// extract the path with the sources
		String strPathSourcesPrefix 	= pathToSources + File.separator + PREFIX;
		String strPathSourcesPostfix 	= pathToSources + File.separator + POSTFIX;
		File filePathSourcesPrefix = new File(strPathSourcesPrefix);
		File filePathSourcesPostfix = new File(strPathSourcesPostfix);
		
		// extract the path with the modules
		File prefix_module_folder = new File(filePathSourcesPrefix,  "org.aspectj" +  File.separator + "modules");
		File postfix_module_folder = new File(filePathSourcesPostfix, "org.aspectj" +  File.separator + "modules");
		
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
			System.out.println("Error during loading of 'result.properties': " + e.getMessage());
			System.exit(0);
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
		 
		// set the source paths, so the parser can generate valid names
		jmutops.setSourcePath(filePathSourcesPrefix,  OptionsVersion.PREFIX);
		jmutops.setSourcePath(filePathSourcesPostfix, OptionsVersion.POSTFIX);
			
		// get all java files in the source folders
		Iterator<File> prefix_iterator  = null;
		Iterator<File> postfix_iterator = null;
		
		// check each file in the prefix folder
		for(File prefixFile: arrayFilesPrefix){
			File postfixFile = new File(strPathPostfix + File.separator + prefixFile.getName());
			
			// check for file existance
			if((!prefixFile.exists()) || (!postfixFile.exists())){
				continue;
			}
			
			// check both files to be .java
			if( !(FilenameUtils.isExtension(prefixFile.getName(),"java")) || !(FilenameUtils.isExtension(postfixFile.getName(),"java"))){
				continue;
			}
			
			// reset the preperators
			jmutops.resetRequiredFiles();
			
			
			
			// get the files in the project source folder
//			prefix_iterator  = FileUtils.iterateFiles(filePathSourcesPrefix, new String[]{"java"}, true);
//			postfix_iterator = FileUtils.iterateFiles(filePathSourcesPostfix, new String[]{"java"}, true);
			
//			File prefix_input  = FileUtilities.findFile(prefixFile.getName(), prefix_iterator);
//			File postfix_input = FileUtilities.findFile(postfixFile.getName(), postfix_iterator);
			
			File prefix_input = iBugsTools.getFileFromiBugsRepository(iBugs_ID, filePathSourcesPrefix, prefixFile.getName(), IBUGS_REPOSITORY);
			File postfix_input = iBugsTools.getFileFromiBugsRepository(iBugs_ID, filePathSourcesPostfix, postfixFile.getName(), IBUGS_REPOSITORY);
			
			// read in the .classpath files
			processClasspath(jmutops, prefix_input,  prefix_module_folder,  OptionsVersion.PREFIX);
			processClasspath(jmutops, postfix_input, postfix_module_folder, OptionsVersion.POSTFIX);
			
			// set the unitname
			String prefix_unitname  = getUnitName(prefix_input, filePathSourcesPrefix);
			String postfix_unitname = getUnitName(postfix_input, filePathSourcesPostfix);
			jmutops.setUnitName(prefix_unitname,  OptionsVersion.PREFIX);
			jmutops.setUnitName(postfix_unitname, OptionsVersion.POSTFIX);
			
			// look for source folders in pathToSources
//			checkForSrc(jmutops, new File[]{filePathSourcesPrefix},  OptionsVersion.PREFIX);
//			checkForSrc(jmutops, new File[]{filePathSourcesPostfix}, OptionsVersion.POSTFIX);
			// look for class folders in pathToSources
//			checkForClassfiles(jmutops, new File[]{filePathSourcesPrefix},  OptionsVersion.PREFIX);
//			checkForClassfiles(jmutops, new File[]{filePathSourcesPostfix}, OptionsVersion.POSTFIX);
		
			try {
				jmutops.checkFiles(prefix_input, postfix_input);
			} catch (Exception e) {
				System.out.println("Exception found: " + e.getMessage());
			}
							
		}
		jmutops.resetRequiredFiles();
		
		jmutops.createResults();
	}
	
	private static void processClasspath(JMutOps jmutops, File file_input, File folder_modules, OptionsVersion version) {
		// first search for the classpath
		File fileClasspath = FileUtilities.getClasspathFile(file_input);
		File fileClasspathContainingFolder = fileClasspath.getParentFile();
		
		try {
			// now process the content of the .classpath
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fileClasspath);
			Element classpath = doc.getDocumentElement();
			NodeList allClasspathEntries = classpath.getChildNodes();
			for (int i = 0; i < allClasspathEntries.getLength(); i++) {
				Node classpathentry = allClasspathEntries.item(i);
				if (classpathentry.getNodeName().equals("classpathentry")) {
					NamedNodeMap map = classpathentry.getAttributes();
					// extract the "kind"-attribute
					Node kind = map.getNamedItem("kind");
					if (kind.getTextContent().equals("lib")) {
						Node path = map.getNamedItem("path");
						String strSubfolder = path.getTextContent().replace("/", File.separator);
						// if the sub folder is contained in the local module, add it
						File folder_input = new File(fileClasspathContainingFolder, strSubfolder);
						if(folder_input.exists()) {
							// add the content of the src folder to the preperator
							checkForClassfiles(jmutops, new File[]{folder_input}, version);
							continue;
						}
						// otherwise, search in the module folder
						folder_input = new File(folder_modules, strSubfolder);
						if(folder_input.exists()) {
							// add the content of the src folder to the preperator
							checkForClassfiles(jmutops, new File[]{folder_input}, version);
							continue;
						}
					}
					if (kind.getTextContent().equals("src")) {
						// extract the "path"-attribute
						Node path = map.getNamedItem("path");
						String strSubfolder = path.getTextContent().replace("/", File.separator);
						// if the sub folder is contained in the local module, add it
						File folder_input = new File(fileClasspathContainingFolder, strSubfolder);
						if(folder_input.exists()) {
							// add the content of the src folder to the preperator
							checkForSrc(jmutops, new File[]{folder_input}, version);
							continue;
						}
						// otherwise, search in the module folder
						folder_input = new File(folder_modules, strSubfolder);
						if(folder_input.exists()) {
							// add the content of the src folder to the preperator
							checkForSrc(jmutops, new File[]{folder_input}, version);
							continue;
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public static void checkForSrc(JMutOps jmutops, File[] folderContent, OptionsVersion version){
		for(File file: folderContent){
			if(file.isDirectory()){
				if(file.getName().contains("src")){
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
	
	private static String getUnitName(File inputFile, File sourcesPath) {
		StringBuffer buffer = new StringBuffer();
		File traversedFile = inputFile;
		buffer.insert(0, "/" + traversedFile.getName());
		traversedFile = traversedFile.getParentFile();
		
		while((!sourcesPath.equals(traversedFile)) || (traversedFile == null)) {
			// if in this folder is a .classpath, we stop
			Iterator<File> folder_content = FileUtils.iterateFiles(traversedFile, null, false);
			File found = FileUtilities.findFile(".classpath", folder_content);
			if(found != null) {
				buffer.insert(0, "/" + traversedFile.getName());
				traversedFile = traversedFile.getParentFile();
				break;
			}
			
			buffer.insert(0, "/" + traversedFile.getName());
			traversedFile = traversedFile.getParentFile();
		}
		
		
		
		return buffer.toString();
	}
}
