package mutationoperators.methodlevel.ehc;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;

public class EHC extends MutationOperator {

	public EHC() {
		this(null);
	}
	
	public EHC(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		// TODO
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EHC");
		this.mutopproperty.setFullname("Exception Handling Change");
		this.mutopproperty.setDescription("Changes an exception handling statement to an exception propagation statement and vice versa.");
	//	this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
	//	this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setPreCheck();
	}

}
