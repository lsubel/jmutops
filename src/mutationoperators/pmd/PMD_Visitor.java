package mutationoperators.pmd;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

public class PMD_Visitor extends TwoASTVisitor {

	public PMD_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

}
