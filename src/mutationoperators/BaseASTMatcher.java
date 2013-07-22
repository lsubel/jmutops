package mutationoperators;
import org.eclipse.jdt.core.dom.ASTMatcher;


/**
 * General (abstract) class which search for mutation operator specific 
 * matching patterns in two {@link ASTNode}s. 
 * @author Lukas Subel
 *
 */
public abstract class BaseASTMatcher extends ASTMatcher {
	
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
	 * @param mutop The {@link MutationOperator} which is linked to this {@link BaseASTMatcher}.
	 */
	public BaseASTMatcher(MutationOperator mutop) {
		this.mutop = mutop;
		this.defaultMatcher = new ASTMatcher();
	}
}
