package mutationoperators.methodlevel.eoc;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class EOC_Visitor extends TwoASTVisitor {

	public EOC_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(InfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check if the InfixExpression is an comparision (=> "==")
		boolean correctOperator = (node.getOperator() == Operator.EQUALS);
	
		// check if the parallel tree is a MethodInvocation
		boolean isMethodCall = (localStoredTree instanceof MethodInvocation);
		
		// if conditions are true,
		// check for a match
		if(correctOperator && isMethodCall){
			// check for an application
			matcher.match(node, localStoredTree);
		}
		super.visit(node);
		return false;
	}

}
