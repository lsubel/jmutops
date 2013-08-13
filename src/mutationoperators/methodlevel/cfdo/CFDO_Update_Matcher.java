package mutationoperators.methodlevel.cfdo;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;

public class CFDO_Update_Matcher extends TwoASTMatcher {

	public CFDO_Update_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(BreakStatement node, Object other) {
		// if the parallel tree is no BreakStatement, we cannot match them
		if(!(other instanceof BreakStatement)){
			return false;
		}
		// cast the other node
		BreakStatement node2 = (BreakStatement) other;
		
		// check if the labels are different
		boolean differentLabel = !(node.getLabel().subtreeMatch(defaultMatcher, node2.getLabel()));		
		
		if(differentLabel){
			this.mutop.found(node, node2);
			return true;
		}

		return false;
	}

	@Override
	public boolean match(ContinueStatement node, Object other) {
		// if the parallel tree is no BreakStatement, we cannot match them
		if(!(other instanceof ContinueStatement)){
			return false;
		}
		// cast the other node
		ContinueStatement node2 = (ContinueStatement) other;
		
		// check if the labels are different
		boolean differentLabel = !(node.getLabel().subtreeMatch(defaultMatcher, node2.getLabel()));		
		
		if(differentLabel){
			this.mutop.found(node, node2);
			return true;
		}

		return false;
	}

	
	
}
