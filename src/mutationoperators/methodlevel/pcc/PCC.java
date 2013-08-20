package mutationoperators.methodlevel.pcc;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class PCC extends MutationOperator {

	public PCC() {
		this(null);
	}
	
	public PCC(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new PCC_Matcher(this);
		this.twoAST_visitor = new PCC_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("PCC");
		this.mutopproperty.setFullname("Cast type change");
		this.mutopproperty.setDescription("Change the type that a variable is to be cast into.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}

}
