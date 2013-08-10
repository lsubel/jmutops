package mutationoperators.pnc;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class PNC extends MutationOperator {

	public PNC() {
		this(null);
	}
	
	public PNC(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new PNC_Matcher(this);
		this.twoAST_visitor = new PNC_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("PNC");
		this.mutopproperty.setFullname("New method call with child class type");
		this.mutopproperty.setDescription("Changes the instantiated type of an object reference. This causes the object reference to refer to an object of a type that is different from the declared type.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}
}
