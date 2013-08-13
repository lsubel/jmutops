package mutationoperators.methodlevel.pnc;

import mutationoperator.MutationOperator;
import mutationoperator.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ITypeBinding;

import utils.ITypeBindingUtils;

public class PNC_Matcher extends TwoASTMatcher {

	public PNC_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(ClassInstanceCreation node, Object other) {
		// if the compared AST is no ClassInstanceCreation,
		// we can ignore this case
		if(!(other instanceof ClassInstanceCreation)){
			return false;
		}
		// cast other node
		ClassInstanceCreation node2 = (ClassInstanceCreation) other;
		
		// retrieve the binding
		ITypeBinding firstTypeBinding = node.resolveTypeBinding();
		ITypeBinding secondTypeBinding = node2.resolveTypeBinding();
		
		// check that the parameters are equal
		boolean sameArgumentLength = (node.arguments().size() == node2.arguments().size());
		// check for the same arguments
		boolean sameArgument = true;
		if(sameArgumentLength){
			sameArgument = this.defaultMatcher.safeSubtreeListMatch(node.arguments(), node2.arguments());
		}
		
		// since the casted types are related, one type has to be the super class of the other one
		boolean leftValidTyping = ITypeBindingUtils.isFirstTypeParentOfRightType(firstTypeBinding, secondTypeBinding);
		boolean rightValidTyping = ITypeBindingUtils.isFirstTypeParentOfRightType(secondTypeBinding, firstTypeBinding);
		boolean validTyping = leftValidTyping || rightValidTyping;
		
		// check for all conditions
		if(sameArgumentLength && sameArgument && validTyping){
			this.mutop.found(node, node2);
			return true;
		}
		
		return false;
	}
	
}
