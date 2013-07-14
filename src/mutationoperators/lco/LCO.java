package mutationoperators.lco;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;

public class LCO extends MutationOperator {

	public LCO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorLevel.METHOD_LEVEL, "Literal change operator", "LCO", "Increase/decrease numeric values; swap boolean literal");
		this.matcher = new LCO_Matcher(this);
		this.visitor = new LCO_Visitor(this.matcher);
	}
}
