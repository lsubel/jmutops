package mutationoperators.asr;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.InfixExpression;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

public class ASR_Matcher extends TwoASTMatcher {

	public ASR_Matcher(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(Assignment node, Object other) {
		if(other instanceof Assignment){
			Assignment assign = (Assignment) other;
			
			// check operators
			boolean correctPrefixedOperator = 
					(node.getOperator().equals(Assignment.Operator.BIT_AND_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.BIT_OR_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.BIT_XOR_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.DIVIDE_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.LEFT_SHIFT_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.MINUS_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.PLUS_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.REMAINDER_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN))
					|| (node.getOperator().equals(Assignment.Operator.TIMES_ASSIGN));
			boolean correctPostfixedOperator = 
					(assign.getOperator().equals(Assignment.Operator.BIT_AND_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.BIT_OR_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.BIT_XOR_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.DIVIDE_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.LEFT_SHIFT_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.MINUS_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.PLUS_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.REMAINDER_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN))
					|| (assign.getOperator().equals(Assignment.Operator.TIMES_ASSIGN));
			boolean differentOperators = 
				!(node.getOperator().equals(assign.getOperator()));
			if(correctPrefixedOperator && correctPostfixedOperator && differentOperators){
				this.mutop.found(node, assign);
			}
			return true; 
		}
		return false;
	}
	
	@Override
	public boolean match(InfixExpression node, Object other) {
		if(other instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) other;
			
			// check operators
			boolean correctPrefixedOperator = 
					(node.getOperator().equals(InfixExpression.Operator.AND))
					|| (node.getOperator().equals(InfixExpression.Operator.CONDITIONAL_AND))
					|| (node.getOperator().equals(InfixExpression.Operator.OR))
					|| (node.getOperator().equals(InfixExpression.Operator.CONDITIONAL_OR))
					|| (node.getOperator().equals(InfixExpression.Operator.XOR));
			boolean correctPostfixedOperator = 
					(ie.getOperator().equals(InfixExpression.Operator.AND))
					|| (ie.getOperator().equals(InfixExpression.Operator.CONDITIONAL_AND))
					|| (ie.getOperator().equals(InfixExpression.Operator.OR))
					|| (ie.getOperator().equals(InfixExpression.Operator.CONDITIONAL_OR))
					|| (ie.getOperator().equals(InfixExpression.Operator.XOR));
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
