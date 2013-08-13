package mutationoperators.methodlevel.eoc;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EOC extends MutationOperator {

	public EOC() {
		this(null);
	}
	
	public EOC(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new EOC_Matcher(this);
		this.twoAST_visitor = new EOC_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EOC");
		this.mutopproperty.setFullname("Reference comparison and content comparison replacement");
		this.mutopproperty.setDescription("Replaces comparision of operators with method call of equals().");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}


}
