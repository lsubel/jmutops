package mutationoperators.eoc;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class EOC_Matcher extends TwoASTMatcher {

	public EOC_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(InfixExpression node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(!(other instanceof MethodInvocation)){
			return false;
		}
		MethodInvocation node2 = (MethodInvocation) other;
		IMethodBinding methodBinding = node2.resolveMethodBinding();
		
		// check if the InfixExpression is valid (=> "==")
		boolean correctOperator = (node.getOperator() == Operator.EQUALS);
		
		// check if the method is valid
		boolean correctMethodName = (node2.getName().getIdentifier().equals("equals"));
		boolean correctParameterLength = (methodBinding.getParameterTypes().length == 1);

		// check if the left side is equal on both versions
		boolean correctLeftSide = (node.getLeftOperand().subtreeMatch(defaultMatcher, node2.getExpression()));
		boolean correctRightSide = false;
		if(correctParameterLength)
			correctRightSide = (node.getRightOperand().subtreeMatch(defaultMatcher, (ASTNode) node2.arguments().get(0)));
		
		// if both conditions are true
		// we have found a matching
		if(correctOperator && correctMethodName && correctParameterLength && correctLeftSide && correctRightSide){
			mutop.found(node, node2);
			return true;			
		}
		return false;
	}
	
	

}
