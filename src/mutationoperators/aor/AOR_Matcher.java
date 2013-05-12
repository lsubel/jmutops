package mutationoperators.aor;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.InfixExpression;

public class AOR_Matcher extends BaseASTMatcher {

	public AOR_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(InfixExpression node, Object other) {
		if(other instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) other;
			
			// check operators
			boolean correctPrefixedOperator = 
					(node.getOperator().equals(InfixExpression.Operator.PLUS))
					|| (node.getOperator().equals(InfixExpression.Operator.MINUS))
					|| (node.getOperator().equals(InfixExpression.Operator.TIMES))
					|| (node.getOperator().equals(InfixExpression.Operator.DIVIDE))
					|| (node.getOperator().equals(InfixExpression.Operator.REMAINDER));
			boolean correctPostfixedOperator = 
				(ie.getOperator().equals(InfixExpression.Operator.PLUS))
				|| (ie.getOperator().equals(InfixExpression.Operator.MINUS))
				|| (ie.getOperator().equals(InfixExpression.Operator.TIMES))
				|| (ie.getOperator().equals(InfixExpression.Operator.DIVIDE))
				|| (ie.getOperator().equals(InfixExpression.Operator.REMAINDER));
			boolean differentOperators = 
				!(node.getOperator().equals(ie.getOperator()));
			if(correctPrefixedOperator && correctPostfixedOperator && differentOperators){
				this.mutop.found(node, ie);
			}
			return true; 
		}
		return false;
	}
	
}
