package mutationoperators.sco;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class SCO extends MutationOperator {

	public SCO() {
		this(null);
	}
	
	public SCO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new SCO_Matcher(this);
		this.visitor = new SCO_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("SCO");
		this.mutopproperty.setFullname("Scope change operator");
		this.mutopproperty.setDescription("Moves the location of local variable declaration to outer/inner blocks.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
}
