package mutationoperators.lod;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.lco.LCO_Matcher;
import mutationoperators.lco.LCO_Visitor;

public class LOD extends MutationOperator {
	
	public LOD() {
		this(null);
	}
	
	public LOD(JMutOpsEventListenerMulticaster eventListener) {
		super("Logical Operator Deletion", "LOD", "Delete unary logical operator", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new LOD_Matcher(this);
		this.visitor = new LOD_Visitor(this.matcher);
	}
}
