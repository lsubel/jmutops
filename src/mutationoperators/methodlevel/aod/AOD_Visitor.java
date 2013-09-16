package mutationoperators.methodlevel.aod;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

public class AOD_Visitor extends TwoASTVisitor {

	public AOD_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
	
		// since the Postfix operator might be deleted, 
		// we just check for an application
		matcher.match(node, localStoredTree);
			
		
		return true;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// since the Postfix operator might be deleted, 
		// we just check for an application
		matcher.match(node, localStoredTree);
		
		
		return true;
	}

}
