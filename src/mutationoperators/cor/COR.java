package mutationoperators.cor;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.aor.AOR_Matcher;
import mutationoperators.aor.AOR_Visitor;

public class COR extends MutationOperator {

	public COR() {
		this(null);
	}
	
	public COR(JMutOpsEventListenerMulticaster eventListener) {
		super("Conditional Operator Replacement", "COR", "Replace binary conditional operators with other binary conditional operators.", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new COR_Matcher(this);
		this.visitor = new COR_Visitor(this.matcher);
	}
	
}
