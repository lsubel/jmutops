package mutationoperators.cod;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

public class COD_Matcher extends TwoASTMatcher {

	public COD_Matcher(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(PrefixExpression node, Object other) {

		ASTNode secondTree = (ASTNode) other;
		ASTNode firstSubtree = node.getOperand();
		
		// check for a correct prefix unary operator
		boolean correctPrefixedUnaryOperator = (node.getOperator().equals(PrefixExpression.Operator.NOT));
		
		// check if the prefix ast without the unary operator and the postfix ast are the same
		boolean correctSubtree = (firstSubtree.subtreeMatch(defaultMatcher, secondTree));
		
		// if both conditions are true, notify a matching
		if(correctPrefixedUnaryOperator && correctSubtree){
			this.mutop.found(node, secondTree);
			return true;
		}
		return false; 
	}
}
