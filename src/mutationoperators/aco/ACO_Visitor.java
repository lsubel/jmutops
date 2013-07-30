package mutationoperators.aco;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ACO_Visitor extends BaseASTVisitor {

	public ACO_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ClassInstanceCreation){
			// cast the parallel ast
			ClassInstanceCreation cic = (ClassInstanceCreation) localStoredTree;
			
			// call the matcher
			matcher.match(node, cic);
			
			// visit the expression node
			visitSubtree(node.getExpression(), cic.getExpression());
			
			// visit the AnonymousClassDeclaration node
			visitSubtree(node.getAnonymousClassDeclaration(), cic.getAnonymousClassDeclaration());
			
			// visit the type node
			visitSubtree(node.getType(), cic.getType());
			
			// visit the argument nodes
			visitSubtrees(node.arguments(), cic.arguments());
			
		}
		
		return super.visit(node);
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
