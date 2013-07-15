package mutationoperators.ror;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.lco.LCO_Matcher;
import mutationoperators.lco.LCO_Visitor;

public class ROR extends MutationOperator {

	public ROR() {
		this(null);
	}
	
	public ROR(JMutOpsEventListenerMulticaster eventListener) {
		super("Relational Operator Replacement", "ROR", "Replace relational operators with other relational operators, and replace the entire predicate with true and false", 
				MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new ROR_Matcher(this);
		this.visitor = new ROR_Visitor(this.matcher);
	}
}
