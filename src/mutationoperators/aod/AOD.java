package mutationoperators.aod;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.aor.AOR_Matcher;
import mutationoperators.aor.AOR_Visitor;

public class AOD extends MutationOperator {
	
	public AOD() {
		this(null);
	}
	
	public AOD(JMutOpsEventListenerMulticaster eventListener) {
		super("Arithmetic Operator Deletion", "AOD", "Delete basic unary/short-cut arithmetic operators.", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new AOD_Matcher(this);
		this.visitor = new AOD_Visitor(this.matcher);
	}
}
