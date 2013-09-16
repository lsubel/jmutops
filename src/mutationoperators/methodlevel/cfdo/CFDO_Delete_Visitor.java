package mutationoperators.methodlevel.cfdo;

import mutationoperators.MutationOperator;
import mutationoperators.OneASTVisitor;

import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;

public class CFDO_Delete_Visitor extends OneASTVisitor {

	public CFDO_Delete_Visitor(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean visit(BreakStatement node) {
		// since we match with and without a label, we directly notify one
		this.mutop.found(node);
		return false;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		// since we match with and without a label, we directly notify one
		this.mutop.found(node);
		return false;
	}
	
	
}
