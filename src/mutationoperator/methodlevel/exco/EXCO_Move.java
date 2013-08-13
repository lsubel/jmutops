package mutationoperator.methodlevel.exco;

import mutationoperator.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EXCO_Move extends MutationOperator {

	public EXCO_Move() {
		this(null);
	}
	
	public EXCO_Move(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new EXCO_Move_Matcher(this);
		this.twoAST_visitor = new EXCO_Move_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EXCO");
		this.mutopproperty.setFullname("Exception handler change operator");
		this.mutopproperty.setDescription("Insert, delete, move, update one or multiple catch clauses from an try-catch-construction.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setMove();
	}
}
