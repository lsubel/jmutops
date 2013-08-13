package mutationoperators.methodlevel.swo;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class SWO extends MutationOperator {

	public SWO() {
		this(null);
	}
	
	public SWO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new SWO_Matcher(this);
		this.twoAST_visitor = new SWO_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("SWO");
		this.mutopproperty.setFullname("Statements swap operator");
		this.mutopproperty.setDescription("Changes the order of statements (Case-block-statements, statements in if-then-else, expressions before & after condition operator).");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setMove();
	}
}
