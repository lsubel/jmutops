package mutationoperators.methodlevel.lod;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

public class LOD_Matcher extends TwoASTMatcher {

	public LOD_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(PrefixExpression node, Object other) {

		ASTNode secondTree = (ASTNode) other;
		ASTNode firstSubtree = node.getOperand();
		
		// check for a correct prefix unary operator
		boolean correctPrefixedUnaryOperator = (node.getOperator().equals(PrefixExpression.Operator.COMPLEMENT));
		
		// check if the prefix ast without the unary operator and the postfix ast are the same
		boolean correctSubtree = (firstSubtree.subtreeMatch(defaultMatcher, secondTree));
		
		// if both conditions are true, notify a matching
		if(correctPrefixedUnaryOperator && correctSubtree){
			this.mutop.found(node, secondTree);
			return false;
		}
		return false; 
	}
}
