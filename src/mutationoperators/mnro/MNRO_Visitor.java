package mutationoperators.mnro;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class MNRO_Visitor extends BaseASTVisitor {

	public MNRO_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();

		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// check for an application
			matcher.match(node, mi);
			
			// visit the expression node
			visitSubtree(node.getExpression(), mi.getExpression());;
			
			// visit each arguments 
			visitSubtrees(node.arguments(), mi.arguments());
		}
		
		return super.visit(node);
	}
	
}
