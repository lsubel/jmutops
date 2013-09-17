package results;

import java.io.File;
import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * Interface which is related to the events fired within jMutOps.
 * @author Lukas Subel
 *
 */
public interface JMutOpsEventListener{
	
	/**
	 * Event gets fired when an mutation operator matching was found.<p>
	 * @param operator The mutation operator which was found.
	 * @param prefix The AST related to the prefix code.
	 * @param postfix The AST related to the postfix code.
	 */
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix, ASTNode postfix);
	
	/**
	 * Event gets fired when an mutation operator matching was found.<p>
	 * @param operator The mutation operator which was found.
	 * @param prefix The AST related to the prefix code.
	 * @param postfix The AST related to the postfix code.
	 */
	public void OnMatchingFound(MutationOperator operator, ASTNode node);
	
	/**
	 * Event gets fired when a program was initialized. <p>
	 * This event can be started by calling the method {@link jmutops.JMutOps#initProgram(String, String, String, String) initProgram(String, String, String, String)} in {@link jmutops.JMutOps JMutOps}.
	 * @param newProgramName The name of the program.
	 * @param programDescription A short description of the program.
	 * @param urlToProjectPage The weblink to the project's webpage.
	 * @param urlToBugtracker The weblink to the project's bugtracker.
	 */
	public void OnProgramChanged(String newProgramName, String programDescription, String urlToProjectPage, String urlToBugtracker);
	
	/**
	 * Event gets fired when a new bug will be initialized.
	 * This event can be started by calling the method {@link jmutops.JMutOps#initBug(String) initBug(String)} in {@link jmutops.JMutOps JMutOps}.
	 * @param bugID The official ID related to this bug.
	 * @param urlToBugreport The weblink to the bug's entry in the bugtracker.
	 */
	public void OnBugChanged(String bugID, String urlToBugreport);
	
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
	 * Event gets fired when the user wants to receive results.<p>
	 * This event can be started by calling the method {@link jmutops.JMutOps#createResults() createResults()} in {@link jmutops.JMutOps JMutOps}.
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
	 * @param change The {@link SourceCodeChange} which will be checked.
	 */
	public void OnChangeChecked(SourceCodeChange change);
	
	/**
	 * Event gets fired when a new mutation operator will be added to {@link jmutops.MutationOperatorTester MutationOperatorChecker}.
	 * @param mutop The initialized {@link mutationoperators.MutationOperator MutationOperator}.
	 */
	public void OnMutationOperatorInit(MutationOperator mutop);
	
	/**
	 * Event gets fired when there were no mutation operator detected in the last checked change.
	 * @param operatorlist The list of checked {@link mutationoperators.MutationOperator MutationOperator}.
	 */
	public void OnNoMatchingFound(List<MutationOperator> operatorlist);
	
	/**
	 * Event gets fired when we start to check for mutation operator on multiple changes.
	 * Counterpart to {@link #OnChangeChecked(SourceCodeChange)}.
	 */
	public void OnAllChangesChecked(List<SourceCodeChange> changes);
}
