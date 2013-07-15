package mutationoperators.mnro;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class MNRO extends MutationOperator {

	public MNRO() {
		this(null);
	}
	
	public MNRO(JMutOpsEventListenerMulticaster eventListener) {
		super("Method name replacement operator", "MNRO",
				"Replace a method name in MethodInvocationExpression with other method names that have the same parameters and result type", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new MNRO_Matcher(this);
		this.visitor = new MNRO_Visitor(this.matcher);
	}
}
