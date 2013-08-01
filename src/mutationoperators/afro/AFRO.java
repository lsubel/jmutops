package mutationoperators.afro;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class AFRO extends MutationOperator {
	
	public AFRO() {
		this(null);
	}
	
	public AFRO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new AFRO_Matcher(this);
		this.visitor = new AFRO_Visitor(matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("AFRO");
		this.mutopproperty.setFullname("Accessed field replacement operator");
		this.mutopproperty.setDescription("Replaces a field name in FieldAccessExpression with other field names in the class");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
}
