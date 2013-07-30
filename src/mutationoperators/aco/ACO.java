package mutationoperators.aco;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class ACO extends MutationOperator {

	public ACO() {
		this(null);
	}
	
	public ACO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new ACO_Matcher(this);
		this.visitor = new ACO_Visitor(matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ACO");
		this.mutopproperty.setFullname("Argument change operator");
		this.mutopproperty.setDescription("This operator changes the number, position of arguments in ClassInstanceCreationExpression and MethodInvocationExpression.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setMove(false);
	}

}
