
import java.io.File;
import java.util.Hashtable;
import java.util.logging.Logger;

import jmutops.JMutOps;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;

import enums.OptionsVersion;


public class TestApplication2 {
	
	private static final String VERSION_ID 			= "86789";
	private static final String PATH_TO_IBUGS 		= "C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\iBugs\\AspectJ\\ibugs_aspectj-1.3\\versions\\";
	private static final String PATH_TO_DIFF_FILES 	= "C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Repository\\iBugs changed files\\changedistiller-results\\";
	private static final String PATH_TO_PREFIX_JAR 	= "C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\iBugs\\AspectJ\\ibugs_aspectj-1.3\\versions\\86789\\pre-fix\\org.aspectj\\modules\\weaver\\src";
	private static final String PATH_TO_POSTFIX_JAR = "C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\iBugs\\AspectJ\\ibugs_aspectj-1.3\\versions\\86789\\post-fix\\org.aspectj\\modules\\weaver\\src";
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File initialPath = new File(PATH_TO_IBUGS + VERSION_ID);
		File prefixedPath = new File(initialPath.getAbsolutePath() + "\\pre-fix");
		File postfixedPath = new File(initialPath.getAbsolutePath() + "\\post-fix");
		
		
		File[] folders_id = new File[]{new File(PATH_TO_DIFF_FILES + VERSION_ID)};
		
		JMutOps jmutops = new JMutOps();
		jmutops.initProgram("iBugs");
		jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		jmutops.setOptions(options, OptionsVersion.PREFIX);
		jmutops.setOptions(options, OptionsVersion.POSTFIX);  

		jmutops.addSourcepathEntry(PATH_TO_PREFIX_JAR, null, OptionsVersion.PREFIX);
		jmutops.addSourcepathEntry(PATH_TO_POSTFIX_JAR, null, OptionsVersion.POSTFIX);
		
		Logger logger = Logger.getLogger(TestApplication.class.getName());
		
		// for each iBugs id
		for(File folder_id: folders_id){ 	
			
			logger.info("Starting to check iBugs ID " + folder_id.getName());
			
			File[] prefixedFiles  = new File(folder_id.getAbsolutePath() + "\\pre-fix").listFiles();
			
			// for each file containing in the prefixed folder
			for(File prefixedFile: prefixedFiles){
				File postfixedFile = new File(folder_id.getAbsolutePath() + "\\post-fix\\" + prefixedFile.getName());

				// check both files to be .java
				if( !(FilenameUtils.isExtension(prefixedFile.getName(),"java")) || !(FilenameUtils.isExtension(postfixedFile.getName(),"java"))){
					continue;
				}
				
				logger.fine("Starting to check file " + prefixedFile.getName() + ".");
				
				jmutops.checkFiles(prefixedFile, postfixedFile);
								
				logger.fine("Ending to check iBugs ID " + folder_id.getName() + ", File " + prefixedFile.getName());
			}
			
			logger.info("Ending to check iBugs ID " + folder_id.getName() + "\n");
	
		}

	}

}
