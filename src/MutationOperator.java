import org.eclipse.jdt.core.dom.ASTNode;

public abstract class MutationOperator {

	/////////////////////////////////////////////////
	///	methods
	/////////////////////////////////////////////////	
	
	// method to check for applied mutation operator 
	public abstract void check(ASTNode leftNode, ASTNode rightNode);
	
	// method describing the action which should happen when an application was found
	public abstract void found(ASTNode leftNode, ASTNode rightNode);
}
