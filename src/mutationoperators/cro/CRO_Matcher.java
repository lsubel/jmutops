package mutationoperators.cro;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;

public class CRO_Matcher extends BaseASTMatcher {

	public CRO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	
	@Override
	public boolean match(ClassInstanceCreation node, Object other) {
		
		// if the compared AST is ClassInstanceCreation
		if(other instanceof ClassInstanceCreation){
			// cast the parallel node
			ClassInstanceCreation node2 = (ClassInstanceCreation) other;	
			
			// resolve the bindings
			IMethodBinding constructor1 = node.resolveConstructorBinding();
			IMethodBinding constructor2 = node2.resolveConstructorBinding();
			
			// check if both constructors are equal
			boolean sameConstructors = !(constructor1.isEqualTo(constructor2));
			
			if(sameConstructors){
				this.mutop.found(node, node2);
				return true;
			}
		}
		return false;
	}
}