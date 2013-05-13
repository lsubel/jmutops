package mutationoperators;
import org.eclipse.jdt.core.dom.ASTMatcher;


public class BaseASTMatcher extends ASTMatcher {
	
	/**
	 * Reference to the mutation operator to call the corresponding found().
	 */
	protected final MutationOperator mutop;
	
	/**
	 * Default ASTMatcher to check for equality even when the ASTNode's match was overridden.
	 */
	protected final ASTMatcher defaultMatcher;
	
	/**
	 * Default constructor.
	 * 
	 * @param mutop
	 */
	public BaseASTMatcher(MutationOperator mutop) {
		this.mutop = mutop;
		this.defaultMatcher = new ASTMatcher();
	}
}
