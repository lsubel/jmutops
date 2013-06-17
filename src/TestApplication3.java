
import java.io.File;
import java.util.Hashtable;
import java.util.logging.Logger;

import jmutops.JMutOps;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;

import enums.OptionsVersion;


public class TestApplication3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File prefixedFile = new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test\\12345\\Pre\\Test1.java");
		File postfixedFile = new File("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test\\12345\\Post\\Test1.java");
		
		JMutOps jmutops = new JMutOps();
		jmutops.initProgram("iBugs");
		jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		jmutops.setOptions(options, OptionsVersion.PREFIX);
		jmutops.setOptions(options, OptionsVersion.POSTFIX);  
		jmutops.addSourcepathEntry("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test\\12345\\Pre", null, OptionsVersion.PREFIX);
		jmutops.addSourcepathEntry("C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\Code\\Test\\12345\\Post", null, OptionsVersion.POSTFIX);
		
		Logger logger = Logger.getLogger(TestApplication.class.getName());
		
				
		logger.fine("Starting to check");
				
		jmutops.checkFiles(prefixedFile, postfixedFile);
								
		logger.fine("Ending to check");
	
		
	}

}
