package mutationoperators.methodlevel.exco;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import utils.ITypeBindingUtils;

public class EXCO_Update_Matcher extends TwoASTMatcher {

	public EXCO_Update_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(SingleVariableDeclaration node, Object other) {
		// if the compared AST is no SingleVariableDeclaration,
		// we cannot compare them
		if(!(other instanceof SingleVariableDeclaration)){
			return false;
		}
		SingleVariableDeclaration node2 = (SingleVariableDeclaration) other;
		
		// check if both values have the same variable name
		boolean sameVariableName = node.getName().subtreeMatch(defaultMatcher, node2.getName());
		
		// retrieve the Bindings of both types
		ITypeBinding firstBinding = node.getType().resolveBinding();
		ITypeBinding secondBinding = node2.getType().resolveBinding();
		
		// check if these are compatible types, so one type is the parent of the other one
		boolean isCompatible = ITypeBindingUtils.isTypeParentOfOtherType(firstBinding, secondBinding) || ITypeBindingUtils.isTypeParentOfOtherType(secondBinding, firstBinding); 
		
		// if all conditions are true, we have a match
		if(sameVariableName && isCompatible){
			this.mutop.found(node, node2);
			return true;
		}
		
		return false;
	}

	
	
}
