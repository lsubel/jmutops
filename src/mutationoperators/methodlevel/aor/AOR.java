package mutationoperators.methodlevel.aor;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class AOR extends MutationOperator {

	public AOR() {
		this(null);
	}
	
	public AOR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new AOR_Matcher(this);
		this.twoAST_visitor = new AOR_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("AOR");
		this.mutopproperty.setFullname("Arithmetic Operator Replacement");
		this.mutopproperty.setDescription("Replace basic binary/unary/short-cut arithmetic operators with other binary/unary arithmetic operators.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
