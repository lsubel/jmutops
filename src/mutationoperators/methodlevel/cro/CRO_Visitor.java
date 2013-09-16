package mutationoperators.methodlevel.cro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;

public class CRO_Visitor extends TwoASTVisitor {

	public CRO_Visitor(TwoASTMatcher matcher) {
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
		
		return false;
	}

}
