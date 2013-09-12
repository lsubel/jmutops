package mutationoperators.methodlevel.tro;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class TRO_Methodlevel extends MutationOperator {

	public TRO_Methodlevel() {
		this(null);
	}
	
	public TRO_Methodlevel(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new TRO_Matcher_Methodlevel(this);
		this.twoAST_visitor = new TRO_Visitor_Methodlevel(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("TRO");
		this.mutopproperty.setFullname("Type Replacement Operator");
		this.mutopproperty.setDescription("Replaces a type with compatible types.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}

}
