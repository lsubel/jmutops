package mutationoperators.methodlevel.ccm;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class CCM extends MutationOperator {
	
	public CCM() {
		this(null);
	}
	
	public CCM(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new CCM_Matcher(this);
		this.twoAST_visitor = new CCM_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("CCM");
		this.mutopproperty.setFullname("Constructor Call Mutator");
		this.mutopproperty.setDescription("Replaces constructor calls with null values.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
