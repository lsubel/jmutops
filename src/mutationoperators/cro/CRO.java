package mutationoperators.cro;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class CRO extends MutationOperator {

	public CRO() {
		this(null);
	}
	
	public CRO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = null;
		this.visitor = null;
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("CRO");
		this.mutopproperty.setFullname("Constructor replacement operator");
		this.mutopproperty.setDescription("This operator replaces a constructor call in ClassInstanceCreationExpression with other constructors of the same class type (i.e., the constructors with different parameters) and the constructors of compatible types.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
	

}
