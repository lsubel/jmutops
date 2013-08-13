package mutationoperators.methodlevel.exco;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;

public class EXCO_Move_Visitor extends TwoASTVisitor {

	public EXCO_Move_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(CatchClause node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CatchClause){
			CatchClause cc = (CatchClause) localStoredTree;
		
			// check for a match
			this.matcher.match(node, cc);
			
			// visit the FormalParameter node
			visitSubtree(node.getException(), cc.getException());
			
			// visit the block node
			visitSubtree(node.getBody(), cc.getBody());
		}
		return false;
	}
	
	

}
