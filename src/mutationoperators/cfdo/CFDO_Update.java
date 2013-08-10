package mutationoperators.cfdo;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class CFDO_Update extends MutationOperator {

	public CFDO_Update() {
		this(null);
	}
	
	public CFDO_Update(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new CFDO_Update_Matcher(this);
		this.twoAST_visitor = new CFDO_Update_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("CFDO");
		this.mutopproperty.setFullname("Control-flow disruption operator");
		this.mutopproperty.setDescription("Disrupting normal control-flow.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}

}
