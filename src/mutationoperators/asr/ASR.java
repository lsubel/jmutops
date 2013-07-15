package mutationoperators.asr;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.lco.LCO_Matcher;
import mutationoperators.lco.LCO_Visitor;

public class ASR extends MutationOperator {

	public ASR() {
		this(null);
	}
	
	public ASR(JMutOpsEventListenerMulticaster eventListener) {
		super("Assignment Operator Replacement", "ASR", "Replace short-cut assignment operators with other short-cut operators of the same kind", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new LCO_Matcher(this);
		this.visitor = new LCO_Visitor(this.matcher);
	}
}
