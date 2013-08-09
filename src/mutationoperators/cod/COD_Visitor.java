package mutationoperators.cod;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

public class COD_Visitor extends TwoASTVisitor {

	public COD_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an application
		matcher.match(node, localStoredTree);
		
		super.visit(node);
		return true;
	}
}
