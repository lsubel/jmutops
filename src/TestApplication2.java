import java.io.File;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;


public class TestApplication2 {

	public static void visitFile(File file, JMutOps jmutops, JMutOps.TargetVersion version){
		File[] subfiles = file.listFiles();
		for(File subfile: subfiles){
			if(subfile.isDirectory()){
				visitFile(subfile, jmutops, version);
			}
			else if(subfile.isFile()){
				// check both files to be .java
				if((FilenameUtils.isExtension(subfile.getName(),"java"))){
					jmutops.addSourcepathEntry(subfile.getAbsolutePath(), null, version);
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File initialPath = new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\iBugs\\AspectJ\\ibugs_aspectj-1.3\\versions\\86789");
		File prefixedPath = new File(initialPath.getAbsolutePath() + "\\pre-fix");
		File postfixedPath = new File(initialPath.getAbsolutePath() + "\\post-fix");
		
		
		File[] folders_id = new File[]{new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Repository\\iBugs changed files\\changedistiller-results\\86789")};
		
		JMutOps jmutops = new JMutOps();
		jmutops.initProgram("iBugs");
		jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		jmutops.setOptions(options, JMutOps.TargetVersion.PREFIX);
		jmutops.setOptions(options, JMutOps.TargetVersion.POSTFIX);  

		jmutops.addSourcepathEntry("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\iBugs\\AspectJ\\ibugs_aspectj-1.3\\versions\\86789\\prefix.jar", null, JMutOps.TargetVersion.PREFIX);
		jmutops.addSourcepathEntry("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\iBugs\\AspectJ\\ibugs_aspectj-1.3\\versions\\86789\\postfix.jar", null, JMutOps.TargetVersion.POSTFIX);
		
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
