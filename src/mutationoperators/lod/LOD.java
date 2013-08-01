package mutationoperators.lod;

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
		this.matcher = new LOD_Matcher(this);
		this.visitor = new LOD_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("LOD");
		this.mutopproperty.setFullname("Logical Operator Deletion");
		this.mutopproperty.setDescription("Delete unary logical operator.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
}
