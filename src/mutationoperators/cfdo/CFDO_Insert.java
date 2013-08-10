package mutationoperators.cfdo;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class CFDO_Insert extends MutationOperator {

	public CFDO_Insert() {
		this(null);
	}
	
	public CFDO_Insert(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.oneAST_visitor = new CFDO_Insert_Visitor(this);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("CFDO");
		this.mutopproperty.setFullname("Control-flow disruption operator");
		this.mutopproperty.setDescription("Disrupting normal control-flow.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanOneAST();
		this.mutopproperty.setInsert();
	}

}
