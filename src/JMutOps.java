import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.Statement;

import results.DatabaseResults;
import results.ResultInterface;

import utils.Preperator;

import mutationoperators.MutationOperatorChecker;
import mutationoperators.aor.AOR;
import mutationoperators.jti.JTI;
import mutationoperators.mnro.MNRO;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;


/**
 * Initial object.
 * 
 * @author Lukas Subel
 *
 */
public class JMutOps {

	/////////////////////////////////////////
	//	Fields
	/////////////////////////////////////////
	
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger(JMutOps.class.getName());
	
	/**
	 * FileDistiller used to distill changes between two version of a file.
	 */
	private FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);;

	/**
	 * MutationOperatorChecker handles all implemented MutationOperators.
	 */
	private MutationOperatorChecker checker = new MutationOperatorChecker();
	
	/**
	 * Preperation class used to retrieve information for the prefixed code.
	 */
	private Preperator prefixed_preperator;
	/**
	 * Preperation class used to retrieve information for the postfixed code.
	 */
	private Preperator postfixed_preperator;

	/**
	 * Stores the name of the program related to this changes.
	 */
	private String programName;

	/////////////////////////////////////////
	//	Constructors
	/////////////////////////////////////////	
	
	/**
	 * Default constructor; adding all implemented MutationOperators to MutationOperatorChecker.
	 */
	public JMutOps() {
		// initialize variables
		this.prefixed_preperator = new Preperator();
		this.postfixed_preperator = new Preperator();
		// initialize all implemented MutationOperator
		checker.addMutationOperator(new JTI(checker));
		checker.addMutationOperator(new AOR(checker));
		checker.addMutationOperator(new MNRO(checker));
	}
	
	/////////////////////////////////////////
	//	Methods
	/////////////////////////////////////////
	
	public void checkFiles(File prefixedFile, File postfixedFile){
		// check for null argument
		if(prefixedFile == null){
			throw new IllegalArgumentException("First argument must not be null.");
		}
		if(postfixedFile == null){
			throw new IllegalArgumentException("Second argument must not be null.");
		}
		
		prefixed_preperator.prepare(prefixedFile);
		postfixed_preperator.prepare(postfixedFile);
		
		// Get the changes between both files
		try {
		    distiller.extractClassifiedSourceCodeChanges(prefixed_preperator.getFile(), postfixed_preperator.getFile());
		} catch(Exception e) {
		    System.err.println("Warning: error while change distilling: " + e.getMessage());
		}
		
		// extract the differences
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes == null){
			logger.info("No changes were found.");
			logger.info("Ending to check File " + prefixedFile.getName() + "." + "\n");
			return;
		}
		
		// handle each change
		for(SourceCodeChange change: changes){
			
			if(change instanceof Update){
				// cast the change into the correct type
				Update change_update = (Update) change;	
				
				// extract the change entities
				SourceCodeEntity sce_old = change_update.getChangedEntity();
				SourceCodeEntity sce_new = change_update.getNewEntity();
				
				// extract the ranges in the document
				int sce_old_start 	= sce_old.getStartPosition();
				int sce_old_end 	= sce_old.getEndPosition();
				int sce_new_start 	= sce_new.getStartPosition();
				int sce_new_end 	= sce_new.getEndPosition();

				// extract information for first file
				NodeFinder nodeFinder_old = new NodeFinder(prefixed_preperator.getAST(), sce_old_start, sce_old_end - sce_old_start + 1);
				ASTNode expr_left = nodeFinder_old.getCoveredNode();
				
				// extract information for second file
				NodeFinder nodeFinder_new = new NodeFinder(postfixed_preperator.getAST(), sce_new_start, sce_new_end - sce_new_start + 1);
				ASTNode expr_right = nodeFinder_new.getCoveredNode();

				if((expr_left instanceof Statement) && (expr_right instanceof Statement)){
					this.checker.checkForMutationOperators(expr_left, expr_right, change);
				}
			}	
		}
	}
	
	/**
	 * Initial a new program to check.
	 * @param programName
	 */
	public void initProgram(String programName){
		this.programName = programName;
		// TODO: call OnCreateProgram event
	}
	
	/**
	 * Add path to required binary type.
	 * 
	 * @param classPath Absolute path to a binary type.
	 * @param version Enum to check if binary type is related to prefixed or postfixed version.
	 * @return True if it was able to add the classpath, otherwise false.
	 */
	public boolean addClasspathEntry(String classPath, OptionsVersion version){
		switch(version){
		case PREFIX:
			return this.prefixed_preperator.addClasspathEntry(classPath);
		case POSTFIX:
			return this.postfixed_preperator.addClasspathEntry(classPath);
		default:
			throw new IllegalArgumentException("Argument version has to be a correct value");
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
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}		
	}
	
	public boolean addResultClass(ResultInterface ri){
		return this.checker.addResultClass(ri);
	}
	
	public boolean createResults(){
		return this.checker.createResults();
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
	
	public boolean setOptions(Hashtable<String, String> options, OptionsVersion version){
		switch(version){
		case PREFIX:
			return this.prefixed_preperator.setOptions(options);
		case POSTFIX:
			return this.postfixed_preperator.setOptions(options);
		default:
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}		
	}
	
	
	
}
