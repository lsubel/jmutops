package results;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTNode;
import mutationoperators.MutationOperator;

public interface ResultListener{
	
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
	public void OnProgramChanged(String newProgramName);
	
	/**
	 * @param officalID the offical ID related to this bug.
	 * 
	 */
	public void OnBugChanged(int officalID);
	
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
}
