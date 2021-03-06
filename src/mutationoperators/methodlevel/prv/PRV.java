package mutationoperators.methodlevel.prv;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class PRV extends MutationOperator {

	public PRV() {
		this(null);
	}
	
	public PRV(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new PRV_Matcher(this);
		this.twoAST_visitor = new PRV_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("PRV");
		this.mutopproperty.setFullname("Reference assignment with other comparable variable");
		this.mutopproperty.setDescription("Changes operands of a reference assignment to be assigned to objects of subclasses.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}

}
