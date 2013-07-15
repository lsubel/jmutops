package mutationoperators.sor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class SOR_Visitor extends BaseASTVisitor {

	public SOR_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(InfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, ie);
			
			// visit the left operator
			visitSubtree(node.getLeftOperand(), ie.getLeftOperand());
			
			// visit the right operator
			visitSubtree(node.getRightOperand(), ie.getRightOperand());
			
			// visit the extended expression
			visitSubtrees(node.extendedOperands(), ie.extendedOperands());			
		}
		
		super.visit(node);
		return true;
	}
}
