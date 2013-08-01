package mutationoperators.eoa;

import mutationoperators.MutationOperator;
import mutationoperators.cro.CRO_Matcher;
import mutationoperators.cro.CRO_Visitor;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EOA extends MutationOperator {

	public EOA() {
		this(null);
	}
	
	public EOA(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new CRO_Matcher(this);
		this.visitor = new CRO_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EOA");
		this.mutopproperty.setFullname("Reference assignment and content assignment replacement");
		this.mutopproperty.setDescription("Replaces an assignment of a pointer reference with a copy of the object, using the Java convention of a clone() method.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}

}
