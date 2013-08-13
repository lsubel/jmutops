package mutationoperators.methodlevel.exco;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;

public class EXCO_Update_Visitor extends TwoASTVisitor {

	public EXCO_Update_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(CatchClause node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CatchClause){
			CatchClause cc = (CatchClause) localStoredTree;
		
			// since in this case only the exception might be a match, 
			// we call the matcher
			this.matcher.match(node.getException(), cc.getException());
			
			// visit the block node
			visitSubtree(node.getBody(), cc.getBody());
		}
		return false;
	}
}
