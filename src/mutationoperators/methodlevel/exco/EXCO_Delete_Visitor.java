package mutationoperators.methodlevel.exco;

import mutationoperators.MutationOperator;
import mutationoperators.OneASTVisitor;

import org.eclipse.jdt.core.dom.CatchClause;

public class EXCO_Delete_Visitor extends OneASTVisitor {

	public EXCO_Delete_Visitor(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean visit(CatchClause node) {
		// since we have found a inserted catch clause, we can notify a match
		this.mutop.found(node);
		return true;
	}
	
	

}
