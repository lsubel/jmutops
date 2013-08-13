package mutationoperators.methodlevel.exco;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EXCO_Update extends MutationOperator {

	public EXCO_Update() {
		this(null);
	}
	
	public EXCO_Update(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new EXCO_Update_Matcher(this);
		this.twoAST_visitor = new EXCO_Update_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EXCO");
		this.mutopproperty.setFullname("Exception handler change operator");
		this.mutopproperty.setDescription("Insert, delete, move, update one or multiple catch clauses from an try-catch-construction.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}
}
