package mutationoperators.methodlevel.vro;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class VRO extends MutationOperator {
	
	public VRO() {
		this(null);
	}
	
	public VRO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new VRO_Matcher(this);
		this.twoAST_visitor = new VRO_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("VRO");
		this.mutopproperty.setFullname("Variable replacement operator");
		this.mutopproperty.setDescription("Replaces a variable name with other names of the same type and of the compatible types in a program. It includes replacing an operand in expression such as object name replacement in access and/or method invocation expressions and replacing variables with constant.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
