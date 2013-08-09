package mutationoperators.aor;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

public class AOR_Matcher extends TwoASTMatcher {

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
	
	@Override
	public boolean match(PostfixExpression node, Object other) {
		
		boolean correctPrefixedShortcutOperator = 
				(node.getOperator().equals(PostfixExpression.Operator.INCREMENT))
				|| (node.getOperator().equals(PostfixExpression.Operator.DECREMENT));
		
		// check for other is Postfix Expression (=> shortcut)
		if(other instanceof PostfixExpression){
			PostfixExpression pe = (PostfixExpression) other;	
			
			boolean correctPostfixedShortcutOperator = 
					(pe.getOperator().equals(PostfixExpression.Operator.INCREMENT))
					|| (pe.getOperator().equals(PostfixExpression.Operator.DECREMENT));
		
			boolean differentOperators = 
					!(node.getOperator().equals(pe.getOperator()));
			
			if( (correctPrefixedShortcutOperator && correctPostfixedShortcutOperator) && differentOperators){
				this.mutop.found(node, pe);
			}
			return true; 
		}
		
		
		// check for other is Prefix Expression (=> unary or shortcut)
		if(other instanceof PrefixExpression){
			PrefixExpression pe = (PrefixExpression) other;

			boolean correctPostfixedShortcutOperator = 
					(pe.getOperator().equals(PrefixExpression.Operator.INCREMENT))
					|| (pe.getOperator().equals(PrefixExpression.Operator.DECREMENT));
			
			if(correctPrefixedShortcutOperator && correctPostfixedShortcutOperator){
				this.mutop.found(node, pe);
			}
			return true; 
		}
		return false;
	}
	
	@Override
	public boolean match(PrefixExpression node, Object other) {
		// extract prefix operators
		boolean correctPrefixedUnaryOperator = 
				(node.getOperator().equals(PrefixExpression.Operator.PLUS))
				|| (node.getOperator().equals(PrefixExpression.Operator.MINUS));
		boolean correctPrefixedShortcutOperator = 
				(node.getOperator().equals(PrefixExpression.Operator.INCREMENT))
				|| (node.getOperator().equals(PrefixExpression.Operator.DECREMENT));
		
		// check for other is Postfix Expression (=> shortcut)
		if(other instanceof PostfixExpression){
			PostfixExpression pe = (PostfixExpression) other;	
			
			boolean correctPostfixedShortcutOperator = 
					(pe.getOperator().equals(PostfixExpression.Operator.INCREMENT))
					|| (pe.getOperator().equals(PostfixExpression.Operator.DECREMENT));
			
			if(correctPrefixedShortcutOperator && correctPostfixedShortcutOperator){
				this.mutop.found(node, pe);
			}
			return true; 
		}
		
		
		// check for other is Prefix Expression (=> unary or shortcut)
		if(other instanceof PrefixExpression){
			PrefixExpression pe = (PrefixExpression) other;
			

			boolean correctPostfixedUnaryOperator = 
					(pe.getOperator().equals(PrefixExpression.Operator.PLUS))
					|| (pe.getOperator().equals(PrefixExpression.Operator.MINUS));


			boolean correctPostfixedShortcutOperator = 
					(pe.getOperator().equals(PrefixExpression.Operator.INCREMENT))
					|| (pe.getOperator().equals(PrefixExpression.Operator.DECREMENT));
		
			boolean differentOperators = 
					!(node.getOperator().equals(pe.getOperator()));
			
			if( ((correctPrefixedUnaryOperator && correctPostfixedUnaryOperator) || (correctPrefixedShortcutOperator && correctPostfixedShortcutOperator) )
					&& differentOperators){
				this.mutop.found(node, pe);
			}
			return true; 
		}
		return false;
	}
	
}
