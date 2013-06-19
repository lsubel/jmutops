package mutationoperators.lco;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;

public class LCO extends MutationOperator {

	public LCO(ResultListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL, "Literal change operator");
		this.matcher = new LCO_Matcher(this);
		this.visitor = new LCO_Visitor(this.matcher);
	}
}
