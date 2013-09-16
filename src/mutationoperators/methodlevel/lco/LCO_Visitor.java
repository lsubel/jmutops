package mutationoperators.methodlevel.lco;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;

public class LCO_Visitor extends TwoASTVisitor {

	public LCO_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
	
		// locally store the second tree
		ASTNode tempStore = this.parallelTree;

		// check case: a.b and this.a.b
		if(tempStore instanceof BooleanLiteral){
			
			matcher.match(node, tempStore);
			
		}
		
		return false;
	}
	
}
