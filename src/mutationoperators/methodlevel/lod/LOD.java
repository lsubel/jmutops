package mutationoperators.methodlevel.lod;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class LOD extends MutationOperator {
	
	public LOD() {
		this(null);
	}
	
	public LOD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new LOD_Matcher(this);
		this.twoAST_visitor = new LOD_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("LOD");
		this.mutopproperty.setFullname("Logical Operator Deletion");
		this.mutopproperty.setDescription("Delete unary logical operator.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
}
