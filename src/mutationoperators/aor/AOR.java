package mutationoperators.aor;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class AOR extends MutationOperator {

	public static final String fullname = "Arithmethic Operator Replacement";
	
	public AOR(ResultListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new AOR_Matcher(this);
		this.visitor = new AOR_Visitor(this.matcher);
	}
	
	@Override
	public String getFullname() {
		return this.fullname;
	}
}
