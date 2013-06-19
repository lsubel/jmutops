package mutationoperators.jti;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class JTI extends MutationOperator {
	
	public JTI(ResultListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL, "Java this insertion");
		this.matcher = new JTI_Matcher(this);
		this.visitor = new JTI_Visitor(matcher);
	}
}
