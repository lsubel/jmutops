package mutationoperators.methodlevel.nvmcm;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class NVMCM extends MutationOperator {
	
	public NVMCM() {
		this(null);
	}
	
	public NVMCM(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new NVMCM_Matcher(this);
		this.twoAST_visitor = new NVMCM_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("NVMCM");
		this.mutopproperty.setFullname("Non Void Method Call Mutator");
		this.mutopproperty.setDescription("Removes method calls to non void methods. Their return value is replaced by the Java Default Value for that specific type.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
