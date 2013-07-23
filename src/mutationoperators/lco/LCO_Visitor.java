package mutationoperators.lco;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;

public class LCO_Visitor extends BaseASTVisitor {

	public LCO_Visitor(BaseASTMatcher matcher) {
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
		
		return super.visit(node);
	}
	
}
