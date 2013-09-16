package mutationoperators.methodlevel.sor;

import org.eclipse.jdt.core.dom.InfixExpression;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

public class SOR_Matcher extends TwoASTMatcher {

	public SOR_Matcher(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(InfixExpression node, Object other) {
		if(other instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) other;
			
			// check operators
			boolean correctPrefixedOperator = 
					(node.getOperator().equals(InfixExpression.Operator.RIGHT_SHIFT_SIGNED))
					|| (node.getOperator().equals(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED))
					|| (node.getOperator().equals(InfixExpression.Operator.LEFT_SHIFT));
			boolean correctPostfixedOperator = 
					(ie.getOperator().equals(InfixExpression.Operator.RIGHT_SHIFT_SIGNED))
					|| (ie.getOperator().equals(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED))
					|| (ie.getOperator().equals(InfixExpression.Operator.LEFT_SHIFT));
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
