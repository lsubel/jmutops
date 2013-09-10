package mutationoperators.methodlevel.rvm;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class RVM extends MutationOperator {
	
	public RVM() {
		this(null);
	}
	
	public RVM(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new RVM_Matcher(this);
		this.twoAST_visitor = new RVM_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("RVM");
		this.mutopproperty.setFullname("Return Values Mutator");
		this.mutopproperty.setDescription("Mutates the return values of method calls.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
