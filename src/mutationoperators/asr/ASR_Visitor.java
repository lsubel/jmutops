package mutationoperators.asr;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class ASR_Visitor extends BaseASTVisitor {

	public ASR_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}
	
	@Override
	public boolean visit(Assignment node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
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
