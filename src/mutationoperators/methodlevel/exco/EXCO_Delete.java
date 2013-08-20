package mutationoperators.methodlevel.exco;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EXCO_Delete extends MutationOperator {

	public EXCO_Delete() {
		this(null);
	}
	
	public EXCO_Delete(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.oneAST_visitor = new EXCO_Delete_Visitor(this);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EXCO");
		this.mutopproperty.setFullname("Exception handler change operator");
		this.mutopproperty.setDescription("Insert, delete, move, update one or multiple catch clauses from an try-catch-construction.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setOneAST();
		this.mutopproperty.setDelete();
	}

}
