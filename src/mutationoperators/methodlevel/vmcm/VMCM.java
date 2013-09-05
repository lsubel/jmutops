package mutationoperators.methodlevel.vmcm;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class VMCM extends MutationOperator {

	public VMCM() {
		this(null);
	}
	
	public VMCM(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.oneAST_visitor = new VMCM_Visitor(this);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("VMCM");
		this.mutopproperty.setFullname("Void Method Call Mutator");
		this.mutopproperty.setDescription("Removes method calls to void methods.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setOneAST();
		this.mutopproperty.setDelete();
	}

}
