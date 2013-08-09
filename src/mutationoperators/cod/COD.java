package mutationoperators.cod;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class COD extends MutationOperator {

	public COD() {
		this(null);
	}
	
	public COD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new COD_Matcher(this);
		this.twoAST_visitor = new COD_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("COD");
		this.mutopproperty.setFullname("Conditional Operator Deletion");
		this.mutopproperty.setDescription("Delete unary conditional operators");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}
}
