package mutationoperators.jti;



import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.WhileStatement;


public class JTI_Matcher extends BaseASTMatcher {

	public JTI_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(MethodInvocation node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(!(other instanceof MethodInvocation)){
			return false;
		}
		
		// otherwise, try to check for the same underlying MethodInvocation
		MethodInvocation node2 = (MethodInvocation) other;
		Expression node_expr = node.getExpression();
		Expression node2_expr = node2.getExpression();
		
		// check case: x() and this.x()
		if( (node_expr == null) && (node2_expr instanceof ThisExpression)){
			mutop.found(node, node2);
			return true;
		}

		return false;
		
	}
	
	@Override
	public boolean match(SimpleName node, Object other) {
		// if the compared AST is any Name,
		// this operator couldn't be applied
		if(other instanceof Name){
			return false;
		}
		
		// check if other is an FieldAccess, starting with an ThisExpression
		if(other instanceof FieldAccess){
			// cast node to type
			// extract attributes from FieldAccess
			FieldAccess node2 = (FieldAccess) other;
			SimpleName name2 = node2.getName();
			Expression expr2 = node2.getExpression();
			// check case: a and this.a
			boolean haveSameName = this.defaultMatcher.match(node, name2);
			if((expr2 instanceof ThisExpression) && haveSameName){
				mutop.found(node, node2);
				return true;
			}
			
		}
		return false;
	}

	@Override
	public boolean match(ThisExpression node, Object other) {
		// since it's not allowed to concatinate this
		// this case is a clear signal that this operator couldn't be applied
		return false;
	}
}
