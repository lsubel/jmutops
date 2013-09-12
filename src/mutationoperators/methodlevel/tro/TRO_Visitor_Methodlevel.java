package mutationoperators.methodlevel.tro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class TRO_Visitor_Methodlevel extends TwoASTVisitor {

	public TRO_Visitor_Methodlevel(TwoASTMatcher matcher) {
		super(matcher);
	}
	
	@Override
	public boolean visit(CastExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		if(localStoredTree instanceof CastExpression){
			// cast other node
			CastExpression cast = (CastExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, cast);
			
			// visit the expression node
			visitSubtree(node.getExpression(), cast.getExpression());
		}
		
		return false;
	}

	@Override
	public boolean visit(CatchClause node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CatchClause){
			CatchClause cc = (CatchClause) localStoredTree;
		
			// since in this case only the exception might be a match, 
			// we call the matcher
			this.matcher.match(node.getException(), cc.getException());
			
			// visit the block node
			visitSubtree(node.getBody(), cc.getBody());
		}
		return false;
	}
	
	
	
	@Override
	public boolean visit(ThrowStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ThrowStatement){
			ThrowStatement ts = (ThrowStatement) localStoredTree;
		
			// since in this case only the exception might be a match, 
			// we call the matcher
			this.matcher.match(node, ts);
			
			// visit the block node
			visitSubtree(node.getExpression(), ts.getExpression());
		}
		return false;
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
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), vde.fragments());
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
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), vds.fragments());
		}
		
		return false;
	}	
	
}
