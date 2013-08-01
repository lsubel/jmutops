package mutationoperators.lor;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class LOR extends MutationOperator {

	public LOR() {
		this(null);
	}
	
	public LOR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new LOR_Matcher(this);
		this.visitor = new LOR_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("LOR");
		this.mutopproperty.setFullname("Logical Operator Replacement");
		this.mutopproperty.setDescription("Replace binary logical operators with other binary logical operators.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
	
}
