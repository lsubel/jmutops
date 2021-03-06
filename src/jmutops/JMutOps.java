package jmutops;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.afro.AFRO;
import mutationoperators.methodlevel.aod.AOD;
import mutationoperators.methodlevel.aoi.AOI;
import mutationoperators.methodlevel.aor.AOR;
import mutationoperators.methodlevel.asr.ASR;
import mutationoperators.methodlevel.ccm.CCM;
import mutationoperators.methodlevel.cfdo.CFDO_Delete;
import mutationoperators.methodlevel.cfdo.CFDO_Insert;
import mutationoperators.methodlevel.cfdo.CFDO_Update;
import mutationoperators.methodlevel.cod.COD;
import mutationoperators.methodlevel.coi.COI;
import mutationoperators.methodlevel.cor.COR;
import mutationoperators.methodlevel.cro.CRO;
import mutationoperators.methodlevel.emvm.EMVM;
import mutationoperators.methodlevel.eoa.EOA;
import mutationoperators.methodlevel.eoc.EOC;
import mutationoperators.methodlevel.exco.EXCO_Delete;
import mutationoperators.methodlevel.exco.EXCO_Insert;
import mutationoperators.methodlevel.exco.EXCO_Move;
import mutationoperators.methodlevel.exco.EXCO_Update;
import mutationoperators.methodlevel.icm.ICM;
import mutationoperators.methodlevel.ipc.IPC;
import mutationoperators.methodlevel.isd.ISD;
import mutationoperators.methodlevel.isi.ISI;
import mutationoperators.methodlevel.jtd.JTD;
import mutationoperators.methodlevel.jti.JTI;
import mutationoperators.methodlevel.lco.LCO;
import mutationoperators.methodlevel.lod.LOD;
import mutationoperators.methodlevel.loi.LOI;
import mutationoperators.methodlevel.lor.LOR;
import mutationoperators.methodlevel.mnro.MNRO;
import mutationoperators.methodlevel.nvmcm.NVMCM;
import mutationoperators.methodlevel.pcc.PCC;
import mutationoperators.methodlevel.pcd.PCD;
import mutationoperators.methodlevel.pci.PCI;
import mutationoperators.methodlevel.pmd.PMD;
import mutationoperators.methodlevel.pnc.PNC;
import mutationoperators.methodlevel.prv.PRV;
import mutationoperators.methodlevel.ror.ROR;
import mutationoperators.methodlevel.rvm.RVM;
import mutationoperators.methodlevel.sco.SCO;
import mutationoperators.methodlevel.sor.SOR;
import mutationoperators.methodlevel.swo.SWO;
import mutationoperators.methodlevel.tro.TRO_Methodlevel;
import mutationoperators.methodlevel.vmcm.VMCM;
import mutationoperators.methodlevel.vro.VRO;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.NodeFinder;

import results.JMutOpsEventListener;
import results.JMutOpsEventListenerMulticaster;
import utils.Preperator;
import utils.Settings;
import utils.SourceCodeChangeUtils;
import utils.TestUtilities;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import enums.OptionsVersion;



/**
 * Central class that handles the user's input.
 * 
 * @author Lukas Subel
 *
 */
public class JMutOps {

	/////////////////////////////////////////
	//	Fields
	/////////////////////////////////////////
	
	/**
	 * {@link FileDistiller} used to distill changes between two version of a file.
	 */
	private FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);

	/**
	 * Instance of {@link MutationOperatorTester} that maintains and forwards inputs
	 * to all registered instances of {@link MutationOperator}.
	 */
	private MutationOperatorTester tester;
	
	/**
	 * Preparation class used to retrieve information for the pre-fixed code.
	 */
	private Preperator prefixed_preperator;
	/**
	 * Preparation class used to retrieve information for the post-fixed code.
	 */
	private Preperator postfixed_preperator;

	/**
	 * Multicaster which will talk to all {@link JMutOpsEventListener} implementing classes that are registered.
	 */
	private final JMutOpsEventListenerMulticaster listener = new JMutOpsEventListenerMulticaster();
	
	/**
	 * Variable which is true iff {@link #checkFiles(File, File)} was called at least once.
	 */
	private boolean firstRunExecuted = false;
	
	/////////////////////////////////////////
	//	Constructors
	/////////////////////////////////////////	
	
	/**
	 * Default constructor; adding all implemented MutationOperators to MutationOperatorChecker.
	 */
	public JMutOps() {
		// initialize variables
		this.prefixed_preperator  = new Preperator(this.listener, TestUtilities.getDefaultPrefixFolder());
		this.postfixed_preperator = new Preperator(this.listener, TestUtilities.getDefaultPostfixFolder());
		this.tester			  = new MutationOperatorTester(this.listener);
	}
	
	/////////////////////////////////////////
	//	Methods
	/////////////////////////////////////////
	
	public void checkFiles(File prefixedFile, File postfixedFile){
		// if this method called for the first time,
		// add all mutation operators
		if(!firstRunExecuted){
			addImplementedMutationOperators();
			firstRunExecuted = true;
		}
		
		// check for null argument
		if(prefixedFile == null){
			String errorMessage = "First argument must not be null.";
			this.listener.OnErrorDetected("JMutOps - checkFiles(File, File)", errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		if(postfixedFile == null){
			String errorMessage = "Second argument must not be null.";
			this.listener.OnErrorDetected("JMutOps - checkFiles(File, File)", errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		
		// fire OnNewFileStarted event
		this.listener.OnFileCheckStarted(prefixedFile, postfixedFile);
		
		// prepare both files
		prefixed_preperator.prepare(prefixedFile);
		postfixed_preperator.prepare(postfixedFile);
		
		// get the changes between both files
		try {
		    distiller.extractClassifiedSourceCodeChanges(prefixed_preperator.getFile(), postfixed_preperator.getFile());
		} catch(Exception e) {
			String errorMessage = e.getMessage();
			this.listener.OnErrorDetected("JMutOps - checkFiles(File, File)", errorMessage);
			return;
		}
		
		// extract the differences
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes == null){
			String errorMessage = "No changes were found.";
			this.listener.OnErrorDetected("JMutOps - checkFiles(File, File)", errorMessage);
			return;
		}
		
		// try to summarize some of the changes
		if(Settings.TRY_TO_SUMMARIZE_CHANGES){
			changes = SourceCodeChangeUtils.summarizeChanges(changes, prefixed_preperator, postfixed_preperator);
		}
		
		// first check for mutation operators which are running on top, f.e. SWO
		this.listener.OnAllChangesCheck(changes);
		preCheckOperators(changes);
		
		// handle each change
		for(SourceCodeChange change: changes){
			
			try {
				// fire OnChangeChecked event
				this.listener.OnChangeChecked(change);
				
				// depending on the class of change,
				// call a different submethod which handles the different number of versions
				if((change instanceof Update) || (change instanceof Move)){
					checkChangeTwoVersions(change);
				}
				else if((change instanceof Insert) || (change instanceof Delete)){
					checkChangeOneVersion(change);
				}
				else{
					String errorMessage = "Impossible exception location.";
					throw new IllegalStateException(errorMessage);
				}
			} catch (Exception e) {
				this.listener.OnErrorDetected("JMutOps - checkFiles", e.getMessage());
				throw e;
			}
		}
		
		// fire OnRunFinished here
		this.listener.OnFileCheckFinished();
	}
	
	private void preCheckOperators(List<SourceCodeChange> changes) {
		this.tester.checkForMutationOperators(changes, this.prefixed_preperator, this.postfixed_preperator);
	}

	private void checkChangeOneVersion(SourceCodeChange change) {
		// define variables
		ASTNode expr; 
		// depending on the class of change, extract values 
		if(change instanceof Insert){
			// extract the inputs for the NodeFinder
			int[] input = SourceCodeChangeUtils.getNodeFinderInput((Insert) change);
			
			// extract information for file
			NodeFinder nodeFinder = new NodeFinder(postfixed_preperator.getAST(), input[0], input[1]);
			expr = nodeFinder.getCoveringNode();
		}
		else if(change instanceof Delete){
			// extract the inputs for the NodeFinder
			int[] input = SourceCodeChangeUtils.getNodeFinderInput((Delete) change);
			
			// extract information for file
			NodeFinder nodeFinder = new NodeFinder(prefixed_preperator.getAST(), input[0], input[1]);
			expr = nodeFinder.getCoveringNode();
		}
		else{
			String errorMessage = "Impossible exception location.";
			this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
			throw new IllegalStateException(errorMessage);
		}
		
		// run the mutation operator check
		this.tester.checkForMutationOperators(expr, change);
	}

	private void checkChangeTwoVersions(SourceCodeChange change) {
		// define variables
		int[] input;
		
		// depending on the class of change, extract values 
		if(change instanceof Update){
			// extract the inputs for the NodeFinder
			input = SourceCodeChangeUtils.getNodeFinderInput((Update) change);
		}
		else if(change instanceof Move){
			// extract the inputs for the NodeFinder
			input = SourceCodeChangeUtils.getNodeFinderInput((Move) change);
		}
		else{
			String errorMessage = "Impossible exception location.";
			this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
			throw new IllegalStateException(errorMessage);
		}

		// extract information for first file
		NodeFinder nodeFinder_old = new NodeFinder(prefixed_preperator.getAST(), input[0], input[1]);
		ASTNode expr_left = nodeFinder_old.getCoveringNode();
		
		// extract information for second file
		NodeFinder nodeFinder_new = new NodeFinder(postfixed_preperator.getAST(), input[2], input[3]);
		ASTNode expr_right = nodeFinder_new.getCoveringNode();
		
		// run the mutation operator check
		this.tester.checkForMutationOperators(expr_left, expr_right, change);	
	}



	/**
	 * Initialize a new program to check.
	 * @param programName The new program's name.
	 */
	public void initializeProgram(String programName, String programDescription, String urlToProjectPage, String urlToBugtracker){
		this.listener.OnProgramChanged(programName, programDescription, urlToProjectPage, urlToBugtracker);
	}
	
	/**
	 * Initialize a new bug which related to the last initialized program.
	 * @param officialID
	 */
	public void initializeBugreport(String officialID, String urlBugreport){
		this.listener.OnBugChanged(officialID, urlBugreport);
	}
	
	/**
	 * Add path to required binary type.
	 * 
	 * @param classPath Absolute path to a binary type.
	 * @param version Enum to check if binary type is related to prefixed or postfixed version.
	 * @return True if it was able to add the classpath, otherwise false.
	 */
	/**
	 * @param classPath
	 * @param version
	 * @return
	 */
	public boolean addClasspathEntry(String classPath, OptionsVersion version){
		switch(version){
		case PREFIX:
			return this.prefixed_preperator.addClasspathEntry(classPath);
		case POSTFIX:
			return this.postfixed_preperator.addClasspathEntry(classPath);
		default:
			String errorMessage = "Argument version has to be a correct value";
			this.listener.OnErrorDetected("JMutOps - addClasspathEntry", errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
	}
	
	/**
	 * Add path to required source type.
	 * 
	 * @param sourcePath Absolute path to a source type.
	 * @param encoding If source type need a specific encoding, this argument must contain this encoding. Otherwise it has to be null
	 * @param version Enum to check if binary type is related to prefixed or postfixed version.
	 * @return
	 */
	public boolean addSourcepathEntry(String sourcePath, String encoding, OptionsVersion version){
		switch(version){
		case PREFIX:
			return this.prefixed_preperator.addSourcepathEntry(sourcePath, encoding);
		case POSTFIX:
			return this.postfixed_preperator.addSourcepathEntry(sourcePath, encoding);
		default:
			String errorMessage = "Argument version has to be a correct value";
			this.listener.OnErrorDetected("JMutOps - addSourcepathEntry", errorMessage);
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}		
	}
	
	public void resetRequiredFiles() {
		 this.prefixed_preperator.resetPreperator();
		 this.postfixed_preperator.resetPreperator();
	}
	
	
	public boolean setUnitName(String name, OptionsVersion version) {
		switch(version){
		case PREFIX:
			return this.prefixed_preperator.setUnitName(name);
		case POSTFIX:
			return this.postfixed_preperator.setUnitName(name);
		default:
			String errorMessage = "Argument version has to be a correct value";
			this.listener.OnErrorDetected("JMutOps - setUnitName", errorMessage);
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}	
	}

	
	/**
	 * Set Variable to include bootclasspath of running VM.
	 * 
	 * @param newValue true if the bootclasspath of the running VM must be prepended to the given classpath <p>
	 *	and false if the bootclasspath of the running VM should be ignored.
	 * @return True if it was possible to set the value, otherwise false.
	 */
	public boolean setIncludeRunningVMBootclasspath(boolean newValue){
		boolean preFixedResult = this.prefixed_preperator.setIncludeRunningVMBootclasspath(newValue);
		boolean postFixedResult = this.postfixed_preperator.setIncludeRunningVMBootclasspath(newValue);
		return preFixedResult && postFixedResult;
	}
	
	public boolean setSourcePath(File path_to_source, OptionsVersion version) {
		switch(version) {
		case PREFIX:
			return this.prefixed_preperator.setPathToSources(path_to_source);
		case POSTFIX:
			return this.postfixed_preperator.setPathToSources(path_to_source);
		default:
			String errorMessage = "Argument version has to be a correct value";
			this.listener.OnErrorDetected("JMutOps - setSourcePath", errorMessage);
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}
	}
	
	public boolean setOptions(Hashtable<String, String> options, OptionsVersion version){
		switch(version){
		case PREFIX:
			return this.prefixed_preperator.setOptions(options);
		case POSTFIX:
			return this.postfixed_preperator.setOptions(options);
		default:
			String errorMessage = "Argument version has to be a correct value";
			this.listener.OnErrorDetected("JMutOps - setOptions", errorMessage);
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}		
	}
	
	public boolean addMutationOperator(MutationOperator mutop) {
		// TODO add an consistency check before adding operator
		return this.tester.addMutationOperator(mutop);
	}
	
	/**
	 * 
	 */
	private void addImplementedMutationOperators() {
		// TODO: add new mutation operators here
		this.tester.addMutationOperator(new JTI(this.listener));
		this.tester.addMutationOperator(new AOR(this.listener));
		this.tester.addMutationOperator(new MNRO(this.listener));
		this.tester.addMutationOperator(new LCO(this.listener));
		this.tester.addMutationOperator(new ROR(this.listener));
		this.tester.addMutationOperator(new COR(this.listener));
		this.tester.addMutationOperator(new SOR(this.listener));
		this.tester.addMutationOperator(new LOR(this.listener));
		this.tester.addMutationOperator(new ASR(this.listener));
		this.tester.addMutationOperator(new COD(this.listener));
		this.tester.addMutationOperator(new LOD(this.listener));
		this.tester.addMutationOperator(new AOD(this.listener));
		this.tester.addMutationOperator(new AFRO(this.listener));
		this.tester.addMutationOperator(new CRO(this.listener));
		this.tester.addMutationOperator(new ACO(this.listener));
		this.tester.addMutationOperator(new SCO(this.listener));
		this.tester.addMutationOperator(new AOI(this.listener));
		this.tester.addMutationOperator(new COI(this.listener));
		this.tester.addMutationOperator(new LOI(this.listener));
		this.tester.addMutationOperator(new JTD(this.listener));
		this.tester.addMutationOperator(new EOA(this.listener));
		this.tester.addMutationOperator(new EOC(this.listener));
		this.tester.addMutationOperator(new PRV(this.listener));
		this.tester.addMutationOperator(new PCC(this.listener));
		this.tester.addMutationOperator(new PCD(this.listener));
		this.tester.addMutationOperator(new PCI(this.listener));
		this.tester.addMutationOperator(new ISD(this.listener));
		this.tester.addMutationOperator(new ISI(this.listener));
		this.tester.addMutationOperator(new IPC(this.listener));
		this.tester.addMutationOperator(new PNC(this.listener));
		this.tester.addMutationOperator(new PMD(this.listener));
		this.tester.addMutationOperator(new CFDO_Insert(this.listener));
		this.tester.addMutationOperator(new CFDO_Delete(this.listener));
		this.tester.addMutationOperator(new CFDO_Update(this.listener));
		this.tester.addMutationOperator(new EXCO_Insert(this.listener));
		this.tester.addMutationOperator(new EXCO_Delete(this.listener));
		this.tester.addMutationOperator(new EXCO_Move(this.listener));
		this.tester.addMutationOperator(new EXCO_Update(this.listener));
		this.tester.addMutationOperator(new SWO(this.listener));
		this.tester.addMutationOperator(new VRO(this.listener));
		this.tester.addMutationOperator(new VMCM(this.listener));
		this.tester.addMutationOperator(new NVMCM(this.listener));
		this.tester.addMutationOperator(new CCM(this.listener));
		this.tester.addMutationOperator(new RVM(this.listener));
		this.tester.addMutationOperator(new ICM(this.listener));
		this.tester.addMutationOperator(new TRO_Methodlevel(this.listener));
		this.tester.addMutationOperator(new EMVM(this.listener));
	}
	
	//////////////////////////////////////////////////////
	//	ActionListener
	//////////////////////////////////////////////////////

	public boolean addEventListener(JMutOpsEventListener listener) {
		return this.listener.add(listener);
	}

	public boolean removeEventListener(JMutOpsEventListener listener){
		return this.listener.remove(listener);
	}
	
	public void createResults(){
		this.listener.OnCreatingResult();
	}
}
