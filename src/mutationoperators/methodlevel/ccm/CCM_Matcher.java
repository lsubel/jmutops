package mutationoperators.methodlevel.ccm;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.NullLiteral;

public class CCM_Matcher extends TwoASTMatcher {

	public CCM_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(ClassInstanceCreation node, Object other) {
		// if the parallel node is NullLiteral
		if(other instanceof NullLiteral) {
			// cast the node
			NullLiteral node2 = (NullLiteral) other;
			
			// check for conditions
			boolean isConstructor = node.resolveConstructorBinding().isConstructor();
			
			// if all conditions are true, notify a matching
			if(isConstructor) {
				mutop.found(node, node2);
			}
		}
		
		return false;
	}

	
	
}
