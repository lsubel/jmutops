package mutationoperators.methodlevel.cor;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class COR extends MutationOperator {

	public COR() {
		this(null);
	}
	
	public COR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new COR_Matcher(this);
		this.twoAST_visitor = new COR_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("COR");
		this.mutopproperty.setFullname("Conditional Operator Replacement");
		this.mutopproperty.setDescription("Replace binary conditional operators with other binary conditional operators");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
	
}
