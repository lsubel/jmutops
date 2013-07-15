package mutationoperators.cod;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.asr.ASR_Matcher;
import mutationoperators.asr.ASR_Visitor;

public class COD extends MutationOperator {

	public COD() {
		this(null);
	}
	
	public COD(JMutOpsEventListenerMulticaster eventListener) {
		super("Conditional Operator Deletion", "COD", "Delete unary conditional operators", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new COD_Matcher(this);
		this.visitor = new COD_Visitor(this.matcher);
	}
}
