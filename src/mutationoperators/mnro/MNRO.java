package mutationoperators.mnro;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class MNRO extends MutationOperator {

	public MNRO() {
		this(null);
	}
	
	public MNRO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new MNRO_Matcher(this);
		this.visitor = new MNRO_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("MNRO");
		this.mutopproperty.setFullname("Method name replacement operator");
		this.mutopproperty.setDescription("Replace a method name in MethodInvocationExpression with other method names that have the same parameters and result type.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setMove(false);
	}
}
