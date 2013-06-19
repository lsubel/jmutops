package mutationoperators.aor;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class AOR extends MutationOperator {

	public AOR(ResultListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL, "Arithmethic Operator Replacement");
		this.matcher = new AOR_Matcher(this);
		this.visitor = new AOR_Visitor(this.matcher);
	}
}
