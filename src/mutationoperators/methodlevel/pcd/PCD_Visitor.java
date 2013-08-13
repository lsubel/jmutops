package mutationoperators.methodlevel.pcd;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;

public class PCD_Visitor extends TwoASTVisitor {

	public PCD_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(CastExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an application
		matcher.match(node, localStoredTree);
		
		return false;
	}
	
}
