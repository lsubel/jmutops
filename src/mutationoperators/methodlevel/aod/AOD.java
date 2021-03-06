package mutationoperators.methodlevel.aod;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class AOD extends MutationOperator {
	
	public AOD() {
		this(null);
	}
	
	public AOD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new AOD_Matcher(this);
		this.twoAST_visitor = new AOD_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("AOD");
		this.mutopproperty.setFullname("Arithmetic Operator Deletion");
		this.mutopproperty.setDescription("Delete basic unary/short-cut arithmetic operators.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
