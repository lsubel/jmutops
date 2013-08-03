package mutationoperators.pcc;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class PCC_Matcher extends BaseASTMatcher {

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
		
		// since the casted types are related, one type has to be the XXX of the other one
		// TODO: implement a method so this works over both versions
		boolean validTyping = ((firstTypeBinding.isSubTypeCompatible(secondTypeBinding)) || (secondTypeBinding.isSubTypeCompatible(firstTypeBinding)));

		if(sameExpression && differentCastTypes && validTyping){
			mutop.found(node, node2);
			return true;
		}
		
		return false;
	}
	
	

}
