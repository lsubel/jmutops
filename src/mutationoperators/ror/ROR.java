package mutationoperators.ror;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class ROR extends MutationOperator {

	public ROR() {
		this(null);
	}
	
	public ROR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new ROR_Matcher(this);
		this.visitor = new ROR_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ROR");
		this.mutopproperty.setFullname("Relational Operator Replacement");
		this.mutopproperty.setDescription("Replace relational operators with other relational operators, and replace the entire predicate with true and false.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setMove(false);
	}
}
