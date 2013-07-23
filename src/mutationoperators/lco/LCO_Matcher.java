package mutationoperators.lco;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.BooleanLiteral;

public class LCO_Matcher extends BaseASTMatcher {

	public LCO_Matcher(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(BooleanLiteral node, Object other) {
		// check if other is an BooleanLiteral
		if(other instanceof BooleanLiteral){
			BooleanLiteral bl = (BooleanLiteral) other;
		 
			// check if the literal values are different
			boolean haveDifferentValue = (node.booleanValue() ^ bl.booleanValue());
			
			if(haveDifferentValue){
				this.mutop.found(node, bl);
			}
			return true;
		}
		return false;
	}

}
