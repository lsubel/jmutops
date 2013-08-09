package mutationoperators.eoa;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EOA extends MutationOperator {

	public EOA() {
		this(null);
	}
	
	public EOA(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new EOA_Matcher(this);
		this.twoAST_visitor = new EOA_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EOA");
		this.mutopproperty.setFullname("Reference assignment and content assignment replacement");
		this.mutopproperty.setDescription("Replaces an assignment of a pointer reference with a copy of the object, using the Java convention of a clone() method.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}

}
