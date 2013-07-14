package mutationoperators.aor;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class AOR extends MutationOperator {

	public AOR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorLevel.METHOD_LEVEL, "Arithmethic Operator Replacement", "AOR", "Replace basic binary/unary/short-cut arithmetic operators with other binary/unary arithmetic operators.");
		this.matcher = new AOR_Matcher(this);
		this.visitor = new AOR_Visitor(this.matcher);
	}
}
