package mutationoperators.methodlevel.cfdo;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;

public class CFDO_Update_Visitor extends TwoASTVisitor {

	public CFDO_Update_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	
	@Override
	public boolean visit(BreakStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof BreakStatement){
			BreakStatement bs = (BreakStatement) localStoredTree;
			
			// check for an application
			matcher.match(node, bs);	
		}
		return false;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ContinueStatement){
			ContinueStatement cs = (ContinueStatement) localStoredTree;
			
			// check for an application
			matcher.match(node, cs);
		}
		return false;
	}
}
