package mutationoperators.methodlevel.lor;

import org.eclipse.jdt.core.dom.InfixExpression;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

public class LOR_Matcher extends TwoASTMatcher {

	public LOR_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(InfixExpression node, Object other) {
		if(other instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) other;
			
			// check operators
			boolean correctPrefixedOperator = 
					(node.getOperator().equals(InfixExpression.Operator.AND))
					|| (node.getOperator().equals(InfixExpression.Operator.OR))
					|| (node.getOperator().equals(InfixExpression.Operator.XOR));
			boolean correctPostfixedOperator = 
					(ie.getOperator().equals(InfixExpression.Operator.AND))
					|| (ie.getOperator().equals(InfixExpression.Operator.OR))
					|| (ie.getOperator().equals(InfixExpression.Operator.XOR));
			boolean differentOperators = 
				!(node.getOperator().equals(ie.getOperator()));
			if(correctPrefixedOperator && correctPostfixedOperator && differentOperators){
				this.mutop.found(node, ie);
			}
			return false; 
		}
		return false;
	}
}
