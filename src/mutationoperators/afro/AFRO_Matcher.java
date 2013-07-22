package mutationoperators.afro;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class AFRO_Matcher extends BaseASTMatcher {

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
		ITypeBinding firstClass = node.resolveFieldBinding().getDeclaringClass();
		ITypeBinding secondClass = node2.resolveFieldBinding().getDeclaringClass();
		boolean sameDeclaringClass = firstClass.isEqualTo(secondClass);
		
		// check if both FieldAccess are describing different fields
		boolean differentFieldName = !(node.getName().subtreeMatch(defaultMatcher, (node2.getName())));
		
		// if both fields are declared in the same class and both fields are different,
		// we have found a matching
		if(sameDeclaringClass && differentFieldName){
			mutop.found(node, node2);
			return true;			
		}
		return false;
	}
	

}
