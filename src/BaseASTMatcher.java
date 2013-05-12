import org.eclipse.jdt.core.dom.ASTMatcher;


public class BaseASTMatcher extends ASTMatcher {
	
	// store an reference to the mutation operator to call the corresponding found method
	protected MutationOperator mutop;
	
	// constructor
	public BaseASTMatcher(MutationOperator mutop) {
		this.mutop = mutop;
	}
}
