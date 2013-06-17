package mutationoperators.jti;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class JTI extends MutationOperator {

	public static final String fullname = "Java this insertion";
	
	public JTI(ResultListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new JTI_Matcher(this);
		this.visitor = new JTI_Visitor(matcher);
	}

	@Override
	public String getFullname() {
		return this.fullname;
	}
}
