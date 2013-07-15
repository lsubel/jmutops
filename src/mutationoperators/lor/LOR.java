package mutationoperators.lor;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.lco.LCO_Matcher;
import mutationoperators.lco.LCO_Visitor;

public class LOR extends MutationOperator {

	public LOR() {
		this(null);
	}
	
	public LOR(JMutOpsEventListenerMulticaster eventListener) {
		super("Logical Operator Replacement", "LOR", "Replace binary logical operators with other binary logical operators", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new LCO_Matcher(this);
		this.visitor = new LCO_Visitor(this.matcher);
	}
	
}
