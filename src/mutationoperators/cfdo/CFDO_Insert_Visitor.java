package mutationoperators.cfdo;

import mutationoperators.MutationOperator;
import mutationoperators.OneASTVisitor;

import org.eclipse.jdt.core.dom.BreakStatement;

public class CFDO_Insert_Visitor extends OneASTVisitor {

	public CFDO_Insert_Visitor(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean visit(BreakStatement node) {
		// since we match with and without a label, we directly notify one
		this.mutop.found(node);
		return true;
	}
}
