package mutationoperators.pcd;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;

public class PCD_Matcher extends BaseASTMatcher {

	public PCD_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(CastExpression node, Object other) {
		ASTNode node2 = (ASTNode) other;
		
		// check for the same Expression
		boolean sameExpression = (node.getExpression().subtreeMatch(defaultMatcher, node2));

		if(sameExpression){
			this.mutop.found(node, node2);
		}
		
		return false;
	}

}
