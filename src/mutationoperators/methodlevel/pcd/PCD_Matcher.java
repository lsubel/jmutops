package mutationoperators.methodlevel.pcd;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;

public class PCD_Matcher extends TwoASTMatcher {

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
