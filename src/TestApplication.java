
import java.io.File;
import java.util.Hashtable;
import java.util.logging.Logger;

import jmutops.JMutOps;


import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;

import enums.OptionsVersion;

public class TestApplication {

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File[] folders_id = new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Repository\\iBugs changed files\\changedistiller-results").listFiles();
//		File[] folders_id = new File[]{new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Repository\\iBugs changed files\\changedistiller-results\\28974")};
//		File[] folders_id = new File[]{new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test\\12345")};
		
		JMutOps jmutops = new JMutOps();
		jmutops.initializeProgram("iBugs", "", "", "");
		jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		jmutops.setOptions(options, OptionsVersion.PREFIX);
		jmutops.setOptions(options, OptionsVersion.POSTFIX);  
		
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
