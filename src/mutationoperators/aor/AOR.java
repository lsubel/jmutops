package mutationoperators.aor;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class AOR extends MutationOperator {

	public AOR() {
		this(null);
	}
	
	public AOR(JMutOpsEventListenerMulticaster eventListener) {
		super("Arithmethic Operator Replacement", "AOR", "Replace basic binary/unary/short-cut arithmetic operators with other binary/unary arithmetic operators.", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new AOR_Matcher(this);
		this.visitor = new AOR_Visitor(this.matcher);
	}
}
