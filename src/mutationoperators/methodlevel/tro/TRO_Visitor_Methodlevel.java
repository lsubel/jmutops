package mutationoperators.methodlevel.tro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class TRO_Visitor_Methodlevel extends TwoASTVisitor {

	public TRO_Visitor_Methodlevel(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		if(localStoredTree instanceof VariableDeclarationExpression){
			// cast other node
			VariableDeclarationExpression vde = (VariableDeclarationExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, vde);
		}
		
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		if(localStoredTree instanceof VariableDeclarationStatement){
			// cast other node
			VariableDeclarationStatement vds = (VariableDeclarationStatement) localStoredTree;

			// check for an application
			matcher.match(node, vds);
		}
		
		return false;
	}	
	
}
