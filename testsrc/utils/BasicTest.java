package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import jmutops.JMutOps;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.JavaCore;
import org.junit.After;
import org.junit.Before;

import results.ApplicationCounter;
import enums.OptionsVersion;

/**
 * General (abstract) testing class. Contains method to retrieve number of applications.
 * @author Lukas Subel
 *
 */
public abstract class BasicTest {

	/////////////////////////////////////////
	/// Constants
	/////////////////////////////////////////
	
	protected static final File PATH_FOR_PREFIX_FILES = TestUtilities.getFolder(TestUtilities.getTempDir() + File.separator + "prefix_test");

	protected static final File PATH_FOR_POSTFIX_FILES = TestUtilities.getFolder(TestUtilities.getTempDir() + File.separator + "postfix_test");
	
	/////////////////////////////////////////
	/// Fields
	/////////////////////////////////////////
	
	JMutOps jmutops;
	
	ApplicationCounter counter;
	
	ArrayList<File> tempfilelist;
	
	protected final MutationOperator mutop;
	
	/////////////////////////////////////////
	/// Methods
	/////////////////////////////////////////	

	/**
	 * Default constructor.
	 * @param mutop The {@link MutationOperator} under test.
	 */
	public BasicTest(MutationOperator mutop){
		this.mutop = mutop;
	}
	
	@Before
	public void setUp() throws Exception {
		// initialize arraylist which contains all temporary created files
		this.tempfilelist = new ArrayList<File>();
		
		// initialize ApplicationCounter which counts the number of applied mutation operators
		this.counter = new ApplicationCounter();
		
		// initialize jMutOps
		this.jmutops = new JMutOps();
		this.jmutops.initProgram("Internal testSuite", "Used during the JUnitTests", "", "");
		this.jmutops.setIncludeRunningVMBootclasspath(true);
	    Hashtable<String, String> options = JavaCore.getDefaultOptions();
	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
	    this.jmutops.setOptions(options, OptionsVersion.PREFIX);
	    this.jmutops.setOptions(options, OptionsVersion.POSTFIX); 
	    
	    // add the ApplicationsCounter to jMutOps so we can count the applications
		this.jmutops.addResultListener(counter);
		
		// initialize test specific context 
		initializeContextFiles();
	}

	@After
	public void tearDown() throws Exception {
		// delete all temporary files
		for(File tempfile: this.tempfilelist){
			if(tempfile != null){
				tempfile.delete();
			}
		}
		// remove objects
		this.jmutops = null;
		this.counter = null;
	}

	/**
	 * Write the {@code javaCode} into a temporary file named {@code fileName} into the folder {@code pathToFolder}.
	 * @param fileName The filename to the temporary file.
	 * @param javaCode The java code which should be written into the temporary file. 
	 * @param pathToFolder The path where the temporary file should be saved.
	 * @return The temporary file.
	 */
	protected File createSourceFile(String fileName, String javaCode, File pathToFolder) {
		// create new tempfile
		File tempfile = new File(pathToFolder, fileName + ".java");
		// add the temp file to the tempfile array so we can delete it at the end
		this.tempfilelist.add(tempfile);
		
		// write sourcecode into tempfile
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(tempfile));
			out.write(javaCode);
			out.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		
		// return the tempfile
		return tempfile;
	}
	
	/**
	 * Add a source file to jMutOps with {@code fileName} as name of file 
	 * and {@code fileContent} as the content of the file.
	 * @param fileName
	 * @param fileContent
	 */
	protected void addContextSourceFile(String fileName, String fileContent){
		// create files for both versions
		createSourceFile(fileName, fileContent, PATH_FOR_PREFIX_FILES);
		createSourceFile(fileName, fileContent, PATH_FOR_POSTFIX_FILES);
		
		// add the storing path the the sourcepath entry of jmutops
		this.jmutops.addSourcepathEntry(PATH_FOR_PREFIX_FILES.getAbsolutePath(), "", OptionsVersion.PREFIX);
		this.jmutops.addSourcepathEntry(PATH_FOR_POSTFIX_FILES.getAbsolutePath(), "", OptionsVersion.POSTFIX);
	}

	
	/**
	 * Run a prefix and postfix version on jMutOps and returns the number of applications 
	 * of the tested mutation operator.
	 * @param preFix The prefix version of code.
	 * @param postFix The postfix version of code.
	 * @return The number of applications >= 0.
	 */
	public int compareMatches(File preFix, File postFix) {
		// stores the current count for this mutation operator
		int oldValue = this.counter.getCount(this.getTestedMutationOperator().getShortname()).intValue();
		// run jMutOps on both files
		this.jmutops.checkFiles(preFix, postFix);
		// returns the difference between both versions
		return this.counter.getCount(this.getTestedMutationOperator().getShortname()).intValue() - oldValue;
	}
	
	/**
	 * Get the tested {@link MutationOperator}.
	 * @return The {@link MutationOperator} under test.
	 */
	protected MutationOperator getTestedMutationOperator(){
		return this.mutop;
	}
	
	/**
	 * Initialized context files for the class under test,
	 * so classes in other files. <p>
	 * Will be called in the @Before method of {@link BasicTest}.
	 */
	protected abstract void initializeContextFiles();

}
