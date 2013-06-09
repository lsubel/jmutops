package results;

import org.eclipse.jdt.core.dom.ASTNode;
import mutationoperators.MutationOperator;

public interface ResultInterface{
	
	public abstract void OnMatchingFound(MutationOperator operator, ASTNode prefix, ASTNode postfix);
	
	public abstract void createResults();
}
