package mutationoperators.jti;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class JTI extends MutationOperator {
	
	public JTI() {
		this(null);
	}
	
	public JTI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new JTI_Matcher(this);
		this.visitor = new JTI_Visitor(matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("JTI");
		this.mutopproperty.setFullname("This Keyword Insertion");
		this.mutopproperty.setDescription("Inserts the keyword this.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setMove(false);
	}
}
