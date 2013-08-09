package mutationoperators.lco;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class LCO extends MutationOperator {

	public LCO() {
		this(null);
	}
	
	public LCO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new LCO_Matcher(this);
		this.twoAST_visitor = new LCO_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("LCO");
		this.mutopproperty.setFullname("Literal change operator");
		this.mutopproperty.setDescription("Increase/decrease numeric values; swap boolean literal.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
}
