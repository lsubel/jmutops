package mutationoperators.methodlevel.afro;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import utils.JDT_Utils;

public class AFRO_Matcher extends TwoASTMatcher {

	public AFRO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(FieldAccess node, Object other) {
		// if the compared AST is no FieldAccess,
		// we cannot compare them
		if(!(other instanceof FieldAccess)){
			return false;
		}

		// try to check for the same class binding
		FieldAccess node2 = (FieldAccess) other;
		IVariableBinding node_fieldbinding 	= node.resolveFieldBinding();
		IVariableBinding node2_fieldbinding = node2.resolveFieldBinding();
		ITypeBinding node_fieldbinding_declaringclass 	= node_fieldbinding.getDeclaringClass();
		ITypeBinding node2_fieldbinding_declaringclass 	= node2_fieldbinding.getDeclaringClass();
		
		
		// check the conditions
		boolean sameDeclaringClass 		= (node_fieldbinding_declaringclass.isEqualTo(node2_fieldbinding_declaringclass) || JDT_Utils.isTypeParentOfOtherType(node_fieldbinding_declaringclass, node2_fieldbinding_declaringclass) || JDT_Utils.isTypeParentOfOtherType(node2_fieldbinding_declaringclass, node_fieldbinding_declaringclass));
		boolean differentAccessedField 	= !(node.subtreeMatch(defaultMatcher, node2));
		boolean differentBindedField 	= !(node.resolveFieldBinding().isEqualTo(node2.resolveFieldBinding()));
		
		// if both fields are declared in the same class and both fields are different,
		// we have found a matching
		if(sameDeclaringClass && differentAccessedField && differentBindedField){
			mutop.found(node, node2);
			return false;			
		}
		return false;
	}
	

}
