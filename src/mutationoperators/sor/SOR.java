package mutationoperators.sor;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.cor.COR_Matcher;
import mutationoperators.cor.COR_Visitor;

public class SOR extends MutationOperator {

	public SOR() {
		this(null);
	}
	
	public SOR(JMutOpsEventListenerMulticaster eventListener) {
		super("Conditional Operator Replacement", "COR", "Replace binary conditional operators with other binary conditional operators.", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new SOR_Matcher(this);
		this.visitor = new SOR_Visitor(this.matcher);
	}
	
}
