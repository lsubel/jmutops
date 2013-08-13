package mutationoperator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * General (abstract) class which traverse in a 
 * mutation operator depending way over one {@link ASTNode}.<p>
 * Checks for special {@link ASTNode}s and conditions and notify a match.
 * @author Lukas Subel
 *
 */
public abstract class OneASTVisitor extends ASTVisitor {
	
	/**
	 * Reference to the mutation operator to call the corresponding found().
	 */
	protected final MutationOperator mutop;
	
	/**
	 * Default constructor.
	 * 
	 * @param mutop The {@link MutationOperator} which is linked to this {@link OneASTVisitor}.
	 */
	public OneASTVisitor(MutationOperator mutop) {
		this.mutop = mutop;
	}
}
