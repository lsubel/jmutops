package mutationoperators.jti;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class JTI extends MutationOperator {
	
	public JTI(JMutOpsEventListenerMulticaster eventListener) {
		super("Java this insertion", "JTI", "Inserts the keyword this.", MutationOperatorLevel.METHOD_LEVEL, eventListener, MutationOperatorCategory.JAVA_SPECIFIC);
		this.matcher = new JTI_Matcher(this);
		this.visitor = new JTI_Visitor(matcher);
	}
}
