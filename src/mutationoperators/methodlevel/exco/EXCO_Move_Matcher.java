package mutationoperators.methodlevel.exco;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.TryStatement;

public class EXCO_Move_Matcher extends TwoASTMatcher {

	public EXCO_Move_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(CatchClause node, Object other) {
		// if the compared AST is no CatchClause,
		// we cannot compare them
		if(!(other instanceof CatchClause)){
			return false;
		}
		CatchClause node2 = (CatchClause) other;
		
		// get the parent node of both elements
		boolean haveTryParent = (node.getParent() instanceof TryStatement) && (node2.getParent() instanceof TryStatement);
		if(!haveTryParent){
			return false;
		}
		TryStatement try1 = (TryStatement) node.getParent();
		TryStatement try2 = (TryStatement) node2.getParent();
		
		// check if both catch clauses are the same;
		// implicit due to the Move from ChangeDistiller
		boolean sameCatch = node.subtreeMatch(defaultMatcher, node2);
		
		// check if both try statements have the same body
		boolean sameParentBody = (try1.getBody().subtreeMatch(defaultMatcher, try2.getBody()));
		
		// check that both catches have different indices in their catch clause block
		// (=> a move within the same block exists)
		boolean differentIndices = (try1.catchClauses().indexOf(node) != try2.catchClauses().indexOf(node2));
		
		// if all of these conditions are true, we detected a matching
		if(sameCatch && sameParentBody && differentIndices){
			this.mutop.found(node, node2);
			return true;
		}
		
		return false;
	}

	
	
}
