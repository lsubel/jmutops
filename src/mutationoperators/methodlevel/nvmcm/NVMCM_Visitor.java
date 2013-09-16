package mutationoperators.methodlevel.nvmcm;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;

public class NVMCM_Visitor extends TwoASTVisitor {

	public NVMCM_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		// if the parallel is also a method invocation, do the 
		if(localStoredTree instanceof MethodInvocation){
			// call the super method, and follow the way there
			
		}
		// if the parallel node is NullLiteral
		else if(localStoredTree instanceof NullLiteral) {
			// check for a matching
			this.matcher.match(node, localStoredTree);
		}
		// if the parallel node is BooleanLiteral
		else if(localStoredTree instanceof BooleanLiteral) {
			// check for a matching
			this.matcher.match(node, localStoredTree);
		}
		// if the parallel node is CharacterLiteral
		else if(localStoredTree instanceof CharacterLiteral) {
			// check for a matching
			this.matcher.match(node, localStoredTree);
		}
		// if the parallel node is CharacterLiteral
		else if(localStoredTree instanceof NumberLiteral) {
			// check for a matching
			this.matcher.match(node, localStoredTree);
		}
		return false;
	}

	
	
}
