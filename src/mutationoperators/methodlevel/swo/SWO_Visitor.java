package mutationoperators.methodlevel.swo;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchStatement;

public class SWO_Visitor extends TwoASTVisitor {

	public SWO_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(IfStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof IfStatement){
			
			// call the matcher to check a correct matching
			this.matcher.match(node, localStoredTree);
		}
		return false;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SwitchStatement){
			
			// call the matcher to check a correct matching
			this.matcher.match(node, localStoredTree);
		}
		return false;
	}

	
	
}
