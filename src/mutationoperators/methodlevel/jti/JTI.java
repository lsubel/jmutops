package mutationoperators.methodlevel.jti;

import mutationoperator.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class JTI extends MutationOperator {
	
	public JTI() {
		this(null);
	}
	
	public JTI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new JTI_Matcher(this);
		this.twoAST_visitor = new JTI_Visitor(twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("JTI");
		this.mutopproperty.setFullname("This Keyword Insertion");
		this.mutopproperty.setDescription("Inserts the keyword this.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}
}
