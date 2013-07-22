package mutationoperators.lco;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.FieldAccess;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

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
