package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import jmutops.JMutOps;

import org.eclipse.jdt.core.JavaCore;
import org.junit.After;
import org.junit.Before;

import results.ApplicationCounter;
import enums.OptionsVersion;

public abstract class MethodTest {

	protected static final String METHOD_NAME = "method";
	protected static final String CLASS_NAME = "Foo";
	protected static final File PATH_FOR_PREFIX_FILES = TestUtilities.getFolder(TestUtilities.getTempDir() + File.separator + "prefix");
	protected static final File PATH_FOR_POSTFIX_FILES = TestUtilities.getFolder(TestUtilities.getTempDir() + File.separator + "postfix");
	
	JMutOps jmutops;
	ApplicationCounter counter;
	ArrayList<File> tempfilelist;
	
	@Before
	public void setUp() throws Exception {
		// init arraylist
		this.tempfilelist = new ArrayList<File>();
		
		// init ApplicationCounter
		this.counter = new ApplicationCounter();
		
		// init jmutops
		this.jmutops = new JMutOps();
		this.jmutops.initProgram("Internal testSuite", "Used during the JUnitTests", "", "");
		this.jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
	    this.jmutops.setOptions(options, OptionsVersion.PREFIX);
	    this.jmutops.setOptions(options, OptionsVersion.POSTFIX); 
		this.jmutops.addResultListener(counter);
	}

	@After
	public void tearDown() throws Exception {
		this.jmutops = null;
		this.counter = null;
	}

	public int compareMatches(String prefixMethodBody, String postfixMethodBody) {
		int oldValue = this.counter.getCount(this.getOperatorName()).intValue();
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode(prefixMethodBody));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode(postfixMethodBody));
		this.jmutops.checkFiles(preFix, postFix);
		// returns the difference between both versions
		return this.counter.getCount(this.getOperatorName()).intValue() - oldValue;
	}
	
	protected abstract String getOperatorName();
	
	/**
	 * Returns the other fields and methods of the main file under test.
	 * @return The fields and methods of the file under test.
	 */
	protected abstract String getOtherClassContent();
	
	protected File createPrefixSourceFile(String sourceCode) {
		return this.createSourceFile(sourceCode, CLASS_NAME, PATH_FOR_PREFIX_FILES);
	}
	
	protected File createPostfixSourceFile(String sourceCode) {
		return this.createSourceFile(sourceCode, CLASS_NAME, PATH_FOR_POSTFIX_FILES);
	}
	
	private String createSourceCode(String snippet){
		return "public class " + CLASS_NAME + " { \n" + snippet + " \n};";
	}
	
	protected String createMethodSourceCode(String methodBody){
		StringBuilder methodSource = new StringBuilder();
        methodSource.append("void ");
        methodSource.append(METHOD_NAME);
        methodSource.append("() { ");
        methodSource.append(methodBody);
        methodSource.append(" }");
        return createSourceCode(methodSource.toString());
	}

	protected String createFieldMethodSourceCode(String methodBody){
		StringBuilder methodSource = new StringBuilder();
        methodSource.append("void ");
        methodSource.append(METHOD_NAME);
        methodSource.append("() { ");
        methodSource.append(methodBody);
        methodSource.append(" }");
        return createSourceCode(getOtherClassContent() + "\n" + methodSource.toString());		
	}
	
	protected File createSourceFile(String javacode, String fileName, File pathToFolder) {
		// create new tempfile
		File tempfile = null;
		tempfile = new File(pathToFolder, fileName + ".java");

		this.tempfilelist.add(tempfile);
		
		// write sourcecode into tempfile
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(tempfile));
			out.write(javacode);
			out.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		
		// return the tempfile
		return tempfile;
	}

}
