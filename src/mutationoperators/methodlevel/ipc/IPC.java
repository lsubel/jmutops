package mutationoperators.methodlevel.ipc;

import mutationoperator.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class IPC extends MutationOperator {

	public IPC() {
		this(null);
	}
	
	public IPC(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.oneAST_visitor = new IPC_Visitor(this);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("IPC");
		this.mutopproperty.setFullname("Explicit call to a parent's constructor deletion");
		this.mutopproperty.setDescription("Deletes super constructor calls, causing the default constructor of the parent class to be called.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanOneAST();
		this.mutopproperty.setDelete();
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=25665

}
