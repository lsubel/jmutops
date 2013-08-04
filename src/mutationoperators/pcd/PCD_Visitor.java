package mutationoperators.pcd;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;

public class PCD_Visitor extends BaseASTVisitor {

	public PCD_Visitor(BaseASTMatcher matcher) {
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
