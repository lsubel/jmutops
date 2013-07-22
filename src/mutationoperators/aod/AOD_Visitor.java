package mutationoperators.aod;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class AOD_Visitor extends BaseASTVisitor {

	public AOD_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
	
		// since the Postfix operator might be deleted, 
		// we just check for an application
		matcher.match(node, localStoredTree);
			
		super.visit(node);
		return true;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// since the Postfix operator might be deleted, 
		// we just check for an application
		matcher.match(node, localStoredTree);
		
		super.visit(node);
		return true;
	}

}
