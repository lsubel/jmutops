package mutationoperators.pcd;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class PCD extends MutationOperator {

	public PCD() {
		this(null);
	}
	
	public PCD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new PCD_Matcher(this);
		this.visitor = new PCD_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("PCD");
		this.mutopproperty.setFullname("Type cast operator deletion");
		this.mutopproperty.setDescription("Deletes type casting operator.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}

}
