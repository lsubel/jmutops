package results;

import org.eclipse.jdt.core.dom.ASTNode;
import mutationoperators.MutationOperator;

public abstract class ResultListener{
	
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix, ASTNode postfix){}
	
	public void OnProgramChanged(String newProgramName){}
	
	public void OnBugChanged(){}
	
	public void OnNewFileStarted(){}
	
	public void OnCreatingResult(){}
}
