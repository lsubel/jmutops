package mutationoperators.methodlevel.icm;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class ICM extends MutationOperator {
	
	public ICM() {
		this(null);
	}
	
	public ICM(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new ICM_Matcher(this);
		this.twoAST_visitor = new ICM_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ICM");
		this.mutopproperty.setFullname("Inline Constant Mutator");
		this.mutopproperty.setDescription("Mutates inline constants.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
