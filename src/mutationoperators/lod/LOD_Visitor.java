package mutationoperators.lod;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class LOD_Visitor extends BaseASTVisitor {

	public LOD_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}
	
	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for an application
		matcher.match(node, localStoredTree);
		
		super.visit(node);
		return true;
	}
}
