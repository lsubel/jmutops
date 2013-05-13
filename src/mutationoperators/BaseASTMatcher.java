package mutationoperators;
import org.eclipse.jdt.core.dom.ASTMatcher;


public class BaseASTMatcher extends ASTMatcher {
	
	/**
	 * Reference to the mutation operator to call the corresponding found().
	 */
	protected MutationOperator mutop;
	
	/**
	 * Default constructor.
	 * 
	 * @param mutop
	 */
	public BaseASTMatcher(MutationOperator mutop) {
		this.mutop = mutop;
	}
}
