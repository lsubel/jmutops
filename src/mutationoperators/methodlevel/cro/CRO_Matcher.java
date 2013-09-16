package mutationoperators.methodlevel.cro;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;

public class CRO_Matcher extends TwoASTMatcher {

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
			boolean differentConstructors = !(constructor1.isEqualTo(constructor2));
			
			if(differentConstructors){
				this.mutop.found(node, node2);
				return false;
			}
		}
		return false;
	}
}