package mutationoperators.asr;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class ASR extends MutationOperator {

	public ASR() {
		this(null);
	}
	
	public ASR(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new ASR_Matcher(this);
		this.visitor = new ASR_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ASR");
		this.mutopproperty.setFullname("Assignment Operator Replacement");
		this.mutopproperty.setDescription("Replace short-cut assignment operators with other short-cut operators of the same kind");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
}