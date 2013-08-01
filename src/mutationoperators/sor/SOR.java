package mutationoperators.sor;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class SOR extends MutationOperator {

	public SOR() {
		this(null);
	}
	
	public SOR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new SOR_Matcher(this);
		this.visitor = new SOR_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("SOR");
		this.mutopproperty.setFullname("Shift Operator Replacement");
		this.mutopproperty.setDescription("Replace shift operators with other shift operators.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);

	}
}
