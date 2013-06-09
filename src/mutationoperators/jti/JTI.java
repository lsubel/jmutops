package mutationoperators.jti;

import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class JTI extends MutationOperator {

	public JTI(MutationOperatorChecker checker) {
		super(checker, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new JTI_Matcher(this);
		this.visitor = new JTI_Visitor(matcher);
	}
}
