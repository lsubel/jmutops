package mutationoperators.methodlevel.vmcm;

import mutationoperators.MutationOperator;
import mutationoperators.OneASTVisitor;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class VMCM_Visitor extends OneASTVisitor {

	public VMCM_Visitor(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean visit(MethodInvocation node) {

		IMethodBinding methodbinding 	= node.resolveMethodBinding();

		// calculate conditions
		boolean returnsVoid = (methodbinding.getReturnType().isPrimitive()) && (methodbinding.getReturnType().getName().equals("void"));
		boolean isMethod = !(node.resolveMethodBinding().isConstructor());
		
		if(returnsVoid && isMethod) {
			mutop.found(node);
		}
		
		return false;
	}

	
	
}
