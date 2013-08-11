package utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import jmutops.JMutOps;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.JavaCore;
import org.junit.After;
import org.junit.Before;

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
	
	ArrayList<MutationOperator> listOfTestedMutationOperators;
	
	/////////////////////////////////////////
	/// Methods
	/////////////////////////////////////////	

	/**
	 * Default constructor.
	 * @param afro The {@link MutationOperator} under test.
	 */
	public BasicTest(){
		// initialize mutation operators which should be tested
		this.listOfTestedMutationOperators = new ArrayList<MutationOperator>();
		initializeMutationOperatorsToTest();
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
	 * Run a prefix and postfix version on jMutOps and returns a map from {@link MutationOperator} 
	 * to the number of detected applications.
	 * @param preFix The prefix version of code.
	 * @param postFix The postfix version of code.
	 * @return The map.
	 */
	public HashMap<MutationOperator, Integer> compareMatches(File preFix, File postFix) {
		// initialize the hashmap which contains the result
		HashMap<MutationOperator, Integer> resultMap = new HashMap<MutationOperator, Integer>();
		// run jMutOps on both files
		this.jmutops.checkFiles(preFix, postFix);
		// fill the HashMap
		for(MutationOperator mutop: this.listOfTestedMutationOperators){
			resultMap.put(mutop, this.counter.getCount(mutop.getShortname()));
		}
		return resultMap;
	}
	
	public void checkOtherMutationOperators(HashMap<MutationOperator, Integer> map, MutationOperator checkedOperators){
		ArrayList<MutationOperator> list = new ArrayList<MutationOperator>();
		list.add(checkedOperators);
		checkOtherMutationOperators(map, list);
	}	
	
	public void checkOtherMutationOperators(HashMap<MutationOperator, Integer> map, List<MutationOperator> checkedOperators){
		for(MutationOperator mutop: map.keySet()){
			if(!checkedOperators.contains(mutop)){
				assertEquals(0, map.get(mutop).intValue());
			}
		}
	}
	
	/**
	 * Initialize the mutation operators which should be tested
	 * by calling 
	 */
	protected abstract void initializeMutationOperatorsToTest();
	
	/**
	 * Initialized context files for the class under test,
	 * so classes in other files. <p>
	 * Will be called in the @Before method of {@link BasicTest}.
	 */
	protected abstract void initializeContextFiles();

	/**
	 * Add the {@link MutationOperator} {@code mutop} to the list of tested operators.
	 * @param mutop
	 */
	protected void addMutationOperatorToTest(MutationOperator mutop){
		this.listOfTestedMutationOperators.add(mutop);
	}
	
	
}
