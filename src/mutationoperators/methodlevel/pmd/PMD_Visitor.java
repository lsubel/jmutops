package mutationoperators.methodlevel.pmd;

import mutationoperator.TwoASTMatcher;
import mutationoperator.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class PMD_Visitor extends TwoASTVisitor {

	public PMD_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof VariableDeclarationExpression){
			VariableDeclarationExpression vde = (VariableDeclarationExpression) localStoredTree;
			
			// call the matcher
			matcher.match(node, vde);
			
			// visit the modifier nodes
			visitSubtrees(node.modifiers(), vde.modifiers());
			
			// visit the type node
			visitSubtree(node.getType(), vde.getType());
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), vde.fragments());
		}
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof VariableDeclarationStatement){
			VariableDeclarationStatement vds = (VariableDeclarationStatement) localStoredTree;
			
			// call the matcher
			matcher.match(node, vds);
			
			// visit the modifier nodes
			visitSubtrees(node.modifiers(), vds.modifiers());
			
			// visit the type node
			visitSubtree(node.getType(), vds.getType());
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), vds.fragments());
		}
		return false;
	}
}
