package mutationoperators.methodlevel.emvm;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class EMVM extends MutationOperator {

	public EMVM() {
		this(null);
	}
	
	public EMVM(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new EMVM_Matcher(this);
		this.twoAST_visitor = new EMVM_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("EMVM");
		this.mutopproperty.setFullname("Experimental Member Variable Mutator ");
		this.mutopproperty.setDescription("Removing assignments to member variables. The members will be initialized with their Java Default Value for the specific type.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
