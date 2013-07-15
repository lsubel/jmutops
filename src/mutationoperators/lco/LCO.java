package mutationoperators.lco;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;

public class LCO extends MutationOperator {

	public LCO() {
		this(null);
	}
	
	public LCO(JMutOpsEventListenerMulticaster eventListener) {
		super("Literal change operator", "LCO", "Increase/decrease numeric values; swap boolean literal", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new LCO_Matcher(this);
		this.visitor = new LCO_Visitor(this.matcher);
	}
}
