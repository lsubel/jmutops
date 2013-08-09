package mutationoperators.ror;

import org.eclipse.jdt.core.dom.InfixExpression;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

public class ROR_Matcher extends TwoASTMatcher {

	public ROR_Matcher(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(InfixExpression node, Object other) {
		if(other instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) other;
			
			// check operators
			boolean correctPrefixedOperator = 
					(node.getOperator().equals(InfixExpression.Operator.EQUALS))
					|| (node.getOperator().equals(InfixExpression.Operator.NOT_EQUALS))
					|| (node.getOperator().equals(InfixExpression.Operator.GREATER))
					|| (node.getOperator().equals(InfixExpression.Operator.GREATER_EQUALS))
					|| (node.getOperator().equals(InfixExpression.Operator.LESS))
					|| (node.getOperator().equals(InfixExpression.Operator.LESS_EQUALS));
			boolean correctPostfixedOperator = 
					(ie.getOperator().equals(InfixExpression.Operator.EQUALS))
					|| (ie.getOperator().equals(InfixExpression.Operator.NOT_EQUALS))
					|| (ie.getOperator().equals(InfixExpression.Operator.GREATER))
					|| (ie.getOperator().equals(InfixExpression.Operator.GREATER_EQUALS))
					|| (ie.getOperator().equals(InfixExpression.Operator.LESS))
					|| (ie.getOperator().equals(InfixExpression.Operator.LESS_EQUALS));
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
