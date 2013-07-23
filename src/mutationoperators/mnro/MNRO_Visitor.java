package mutationoperators.mnro;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MNRO_Visitor extends BaseASTVisitor {

	public MNRO_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		if(localStoredTree instanceof MethodInvocation){
			// cast other node
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
