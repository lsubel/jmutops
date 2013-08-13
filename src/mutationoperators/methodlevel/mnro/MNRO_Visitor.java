package mutationoperators.methodlevel.mnro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MNRO_Visitor extends TwoASTVisitor {

	public MNRO_Visitor(TwoASTMatcher matcher) {
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
