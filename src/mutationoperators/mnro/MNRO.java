package mutationoperators.mnro;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class MNRO extends MutationOperator {

	public MNRO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL,
				"Method name replacement operator", "MNRO", "Replace a method name in MethodInvocationExpression with other method names that have the same parameters and result type");
		this.matcher = new MNRO_Matcher(this);
		this.visitor = new MNRO_Visitor(this.matcher);
	}
}
