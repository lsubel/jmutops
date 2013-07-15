package results;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import mutationoperators.MutationOperator;

public interface JMutOpsEventListener{
	
	/**
	 * Event gets fired when an mutation operator matching was found.<p>
	 * @param operator The mutation operator which was found.
	 * @param prefix The AST related to the prefix code.
	 * @param postfix The AST related to the postfix code.
	 */
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix, ASTNode postfix);
	
	/**
	 * Events get fired when a program was initialized. <p>
	 * @param newProgramName The name of the program.
	 */
	public void OnProgramChanged(String newProgramName, String programDescription, String urlToProjectPage, String urlToBugtracker);
	
	/**
	 * @param officialID the official ID related to this bug.
	 * @param urlToBugreport TODO
	 * 
	 */
	public void OnBugChanged(int officialID, String urlToBugreport);
	
	/**
	 * Event gets fired when the check of two files started.
	 * @param prefixedFile The prefixed file.
	 * @param postfixedFile The postfixed file.
	 */
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile);
	
	/**
	 * Event gets fired when the check of two files finished.
	 */
	public void OnFileCheckFinished();
	
	/**
	 * Event gets fired when the user wants to recieve results.
	 */
	public void OnCreatingResult();
	
	/**
	 * Event gets fired when an error was detected.
	 * @param location The location of the error.
	 * @param errorMessage The received error message.
	 */
	public void OnErrorDetected(String location, String errorMessage);
	
	/**
	 * Event gets fired when a new change will be checked.
	 * @param change The SourceCodeChange-object related to the change.
	 */
	public void OnChangeChecked(SourceCodeChange change);
	
	public void OnMutationOperatorInit(MutationOperator mutop);
	
	public void OnNoMatchingFound(List<MutationOperator> operatorlist);
}
