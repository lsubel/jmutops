package mutationoperators.methodlevel.asr;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;

import mutationoperator.TwoASTMatcher;
import mutationoperator.TwoASTVisitor;

public class ASR_Visitor extends TwoASTVisitor {

	public ASR_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}
	
	@Override
	public boolean visit(Assignment node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Assignment){
			Assignment assign = (Assignment) localStoredTree;
			
			// check for an application
			matcher.match(node, assign);
			
			// visit the left expression node
			visitSubtree(node.getLeftHandSide(), assign.getLeftHandSide());
			
			// visit the right expression node
			visitSubtree(node.getRightHandSide(), assign.getRightHandSide());
		}
		return false;
	}

}
