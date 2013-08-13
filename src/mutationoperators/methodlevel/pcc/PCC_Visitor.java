package mutationoperators.methodlevel.pcc;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;

public class PCC_Visitor extends TwoASTVisitor {

	public PCC_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(CastExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CastExpression){
			CastExpression ce = (CastExpression) localStoredTree;
			// check for an application
			matcher.match(node, ce);
		}
		return false;
	}
	

}
