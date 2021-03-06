package mutationoperators.methodlevel.pcc;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ITypeBinding;

import utils.JDT_Utils;

public class PCC_Matcher extends TwoASTMatcher {

	public PCC_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(CastExpression node, Object other) {
		// if the compared AST is no CastExpression,
		// we cannot compare them
		if(!(other instanceof CastExpression)){
			return false;
		}
		CastExpression node2 = (CastExpression) other;
		ITypeBinding firstTypeBinding = node.resolveTypeBinding();
		ITypeBinding secondTypeBinding = node2.resolveTypeBinding();
		
		// check for the same Expression
		boolean sameExpression = (node.getExpression().subtreeMatch(defaultMatcher, node2.getExpression()));
		
		// check for different casted types
		boolean differentCastTypes = !(firstTypeBinding.isEqualTo(secondTypeBinding));
		
		// since the casted types are related, one type has to be the super class of the other one
		boolean leftValidTyping = JDT_Utils.isTypeParentOfOtherType(firstTypeBinding, secondTypeBinding);
		boolean rightValidTyping = JDT_Utils.isTypeParentOfOtherType(secondTypeBinding, firstTypeBinding);
		boolean validTyping = leftValidTyping || rightValidTyping;
		
		if(sameExpression && differentCastTypes && validTyping){
			mutop.found(node, node2);
			return false;
		}
		
		return false;
	}
}
