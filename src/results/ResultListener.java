package results;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTNode;
import mutationoperators.MutationOperator;

public interface ResultListener{
	
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix, ASTNode postfix);
	
	public void OnProgramChanged(String newProgramName);
	
	public void OnBugChanged();
	
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile);
	
	public void OnFileCheckFinished();
	
	public void OnCreatingResult();
	
	public void OnErrorDetected(String location, String errorMessage);
}
