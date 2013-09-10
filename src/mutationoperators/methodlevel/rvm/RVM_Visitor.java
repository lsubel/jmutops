package mutationoperators.methodlevel.rvm;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ReturnStatement;

public class RVM_Visitor extends TwoASTVisitor {

	public RVM_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(ReturnStatement node) {
		
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		// if the parallel node is ReturnStatement
		if(localStoredTree instanceof ReturnStatement) {
			// check for a matching
			this.matcher.match(node, localStoredTree);
		}
	
		return false;
	}

	
	
}
