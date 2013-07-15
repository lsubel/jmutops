package mutationoperators.aod;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

public class AOD_Matcher extends BaseASTMatcher {

	public AOD_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(PostfixExpression node, Object other) {
	
		ASTNode secondTree = (ASTNode) other;
		ASTNode firstSubtree = node.getOperand();
		
		boolean validPostfixExpression = 
				node.getOperator().equals(PostfixExpression.Operator.DECREMENT) 
				|| node.getOperator().equals(PostfixExpression.Operator.INCREMENT); 
		
		// check if the prefix ast without the unary operator and the postfix ast are the same
		boolean correctSubtree = (firstSubtree.subtreeMatch(defaultMatcher, secondTree));
		
		// if both conditions are true, notify a matching
		if(validPostfixExpression && correctSubtree){
			this.mutop.found(node, secondTree);
			return true;
		}
		return false; 
	}

	@Override
	public boolean match(PrefixExpression node, Object other) {
		
		ASTNode secondTree = (ASTNode) other;
		ASTNode firstSubtree = node.getOperand();
		
		// extract prefix operators
		boolean validPrefixedUnaryOperator = 
				(node.getOperator().equals(PrefixExpression.Operator.PLUS))
				|| (node.getOperator().equals(PrefixExpression.Operator.MINUS));
		boolean validPrefixedShortcutOperator = 
				(node.getOperator().equals(PrefixExpression.Operator.INCREMENT))
				|| (node.getOperator().equals(PrefixExpression.Operator.DECREMENT));
		
		// check if the prefix ast without the unary operator and the postfix ast are the same
		boolean correctSubtree = (firstSubtree.subtreeMatch(defaultMatcher, secondTree));
		
		// if both conditions are true, notify a matching
		if((validPrefixedUnaryOperator || validPrefixedShortcutOperator) && correctSubtree){
			this.mutop.found(node, secondTree);
			return true;
		}
		return false; 
	}
	
	

}
