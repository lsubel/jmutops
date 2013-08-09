package mutationoperators.ipc;

import mutationoperators.MutationOperator;
import mutationoperators.OneASTVisitor;

import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

public class IPC_Visitor extends OneASTVisitor {

	public IPC_Visitor(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// since this statement can only occure in a constructor, we do not have to check anything
		this.mutop.found(node);
		return false;
	}

	
}
