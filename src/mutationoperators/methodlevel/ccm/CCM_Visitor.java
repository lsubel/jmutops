package mutationoperators.methodlevel.ccm;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.NullLiteral;

public class CCM_Visitor extends TwoASTVisitor {

	public CCM_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		// if the parallel is also a method invocation, do the 
		if(localStoredTree instanceof NullLiteral){
			// check for a matching
			this.matcher.match(node, localStoredTree);	
		}
		
		return false;
	}
	
}
