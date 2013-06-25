package jmutops;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import org.eclipse.jdt.core.dom.ASTNode;

import org.eclipse.jdt.core.dom.NodeFinder;
import results.JMutOpsEventListener;
import results.JMutOpsEventListenerMulticaster;

import utils.Preperator;
import utils.Settings;
import utils.TestUtilities;

import mutationoperators.MutationOperatorChecker;
import mutationoperators.aor.AOR;
import mutationoperators.jti.JTI;
import mutationoperators.lco.LCO;
import mutationoperators.mnro.MNRO;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import enums.OptionsVersion;


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
	 * Multicaster which will talk to all ResultListeners which were added
	 */
	private JMutOpsEventListenerMulticaster listener = new JMutOpsEventListenerMulticaster();
	
	/////////////////////////////////////////
	//	Constructors
	/////////////////////////////////////////	
	
	/**
	 * Default constructor; adding all implemented MutationOperators to MutationOperatorChecker.
	 */
	public JMutOps() {
		// initialize variables
		this.prefixed_preperator = new Preperator(this.listener, TestUtilities.getDefaultPrefixFolder());
		this.postfixed_preperator = new Preperator(this.listener, TestUtilities.getDefaultPostfixFolder());
		addImplementedMutationOperators();
	}
	
	/////////////////////////////////////////
	//	Methods
	/////////////////////////////////////////
	
	public void checkFiles(File prefixedFile, File postfixedFile){
		// check for null argument
		if(prefixedFile == null){
			String errorMessage = "First argument must not be null.";
			this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		if(postfixedFile == null){
			String errorMessage = "Second argument must not be null.";
			this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		
		// fire OnNewFileStarted here
		this.listener.OnFileCheckStarted(prefixedFile, postfixedFile);
		
		// prepare both files
		prefixed_preperator.prepare(prefixedFile);
		postfixed_preperator.prepare(postfixedFile);
		
		// get the changes between both files
		try {
		    distiller.extractClassifiedSourceCodeChanges(prefixed_preperator.getFile(), postfixed_preperator.getFile());
		} catch(Exception e) {
			String errorMessage = e.getMessage();
			this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
			return;
		}
		
		// extract the differences
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes == null){
			String errorMessage = "No changes were found.";
			this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
			return;
		}
		
		if(Settings.TRY_TO_SUMMARIZE_CHANGES){
			changes = summarizeChanges(changes);
		}
		
		// handle each change
		for(SourceCodeChange change: changes){
			
			if((change instanceof Update) || (change instanceof Move)){
				SourceCodeEntity sce_old;
				SourceCodeEntity sce_new;
				int sce_old_start;
				int sce_old_end;
				int sce_new_start;
				int sce_new_end;
				
				if(change instanceof Update){
					// cast the change into the correct type
					Update change_update = (Update) change;	
				
					// extract the change entities
					sce_old = change_update.getChangedEntity();
					sce_new = change_update.getNewEntity();
				
					// extract the ranges in the document
					sce_old_start 	= sce_old.getStartPosition();
					sce_old_end 	= sce_old.getEndPosition();
					sce_new_start 	= sce_new.getStartPosition();
					sce_new_end 	= sce_new.getEndPosition();
				}
				else if(change instanceof Move){
					// cast the change into the correct type
					Move change_move = (Move) change;	
				
					// extract the change entities
					sce_old = change_move.getChangedEntity();
					sce_new = change_move.getNewEntity();
				
					// extract the ranges in the document
					sce_old_start 	= sce_old.getStartPosition();
					sce_old_end 	= sce_old.getEndPosition();
					sce_new_start 	= sce_new.getStartPosition();
					sce_new_end 	= sce_new.getEndPosition();
				}
				else{
					String errorMessage = "Impossible exception location.";
					this.listener.OnErrorDetected("JMutOps - checkFiles", errorMessage);
					throw new IllegalStateException(errorMessage);
				}

				// extract information for first file
				NodeFinder nodeFinder_old = new NodeFinder(prefixed_preperator.getAST(), sce_old_start, sce_old_end - sce_old_start + 1);
				ASTNode expr_left = nodeFinder_old.getCoveredNode();
				
				// extract information for second file
				NodeFinder nodeFinder_new = new NodeFinder(postfixed_preperator.getAST(), sce_new_start, sce_new_end - sce_new_start + 1);
				ASTNode expr_right = nodeFinder_new.getCoveredNode();

				// run the mutation operator check
				this.checker.checkForMutationOperators(expr_left, expr_right, change);
			}	// end of if((change instanceof Update) || (change instanceof Move)){
		}
		
		// fire OnRunFinished here
		this.listener.OnFileCheckFinished();
	}
	
	private List<SourceCodeChange> summarizeChanges(
			List<SourceCodeChange> changes) {
		List<SourceCodeChange> newList = new ArrayList<SourceCodeChange>();

		for(SourceCodeChange change: changes){
			
			int nInsertStart;
			int nInsertEnd;
			
			if(change instanceof Insert){
				Insert castedChange = (Insert) change;
				changes.remove(change);
				
				for(SourceCodeChange change2: changes){
					if(change2 instanceof Delete){
						Delete castedChange2 = (Delete) change2;
						
						boolean sameRange = 
								(castedChange.getChangedEntity().getStartPosition() == castedChange2.getChangedEntity().getStartPosition())
								&& (castedChange.getChangedEntity().getEndPosition() == castedChange2.getChangedEntity().getEndPosition());
						boolean sameParentEntity = castedChange.getParentEntity() == castedChange2.getParentEntity();
						boolean sameRootEntity = castedChange.getRootEntity() == castedChange2.getRootEntity();
						if(sameRange && sameParentEntity && sameRootEntity){
							Update newUpdate = new Update(castedChange.getRootEntity(), castedChange2.getChangedEntity(), castedChange.getChangedEntity(), castedChange.getParentEntity());
							changes.remove(change);
							changes.remove(change2);
							castedChange = null;
							newList.add(newUpdate);
							break;
						}
					}
				}
				
				if(castedChange != null){
					newList.add(castedChange);
				}
			}
			
			else if(change instanceof Delete){
				Delete castedChange = (Delete) change;
				changes.remove(change);
				
				for(SourceCodeChange change2: changes){
					if(change2 instanceof Insert){
						Insert castedChange2 = (Insert) change2;
						
						boolean sameRange = 
								(castedChange.getChangedEntity().getStartPosition() == castedChange2.getChangedEntity().getStartPosition())
								&& (castedChange.getChangedEntity().getEndPosition() == castedChange2.getChangedEntity().getEndPosition());
						boolean sameParentEntity = castedChange.getParentEntity() == castedChange2.getParentEntity();
						boolean sameRootEntity = castedChange.getRootEntity() == castedChange2.getRootEntity();
						if(sameRange && sameParentEntity && sameRootEntity){
							Update newUpdate = new Update(castedChange.getRootEntity(), castedChange.getChangedEntity(), castedChange2.getChangedEntity(), castedChange.getParentEntity());
							changes.remove(change2);
							castedChange = null;
							newList.add(newUpdate);
							break;
						}
					}
				}
				
				if(castedChange != null){
					newList.add(castedChange);
				}
			}
			else{
				newList.add(change);
				changes.remove(change);
			}
			
		}
		return newList;
	}

	/**
	 * Initial a new program to check.
	 * @param programName The new program's name.
	 */
	public void initProgram(String programName){
		this.listener.OnProgramChanged(programName);
	}
	
	public void initBug(String officialID){
		// TODO: fire event
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
			String errorMessage = "Argument version has to be a correct value";
			this.listener.OnErrorDetected("JMutOps - setOptions", errorMessage);
			throw new IllegalArgumentException("Argument version has to be a correct value");
		}		
	}
	
	/**
	 * 
	 */
	private void addImplementedMutationOperators() {
		// TODO: add new mutation operators here
		this.checker.addMutationOperator(new JTI(this.listener));
		this.checker.addMutationOperator(new AOR(this.listener));
		this.checker.addMutationOperator(new MNRO(this.listener));
		this.checker.addMutationOperator(new LCO(this.listener));
	}
	
	//////////////////////////////////////////////////////
	//	ActionListener
	//////////////////////////////////////////////////////

	public void addResultListener(JMutOpsEventListener rl) {
		this.listener.add(rl);
	}

	public void removeResultListener(JMutOpsEventListener rl){
		this.listener.remove(rl);
	}
	
	public void createResults(){
		this.listener.OnCreatingResult();
	}
}
