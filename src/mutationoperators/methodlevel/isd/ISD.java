package mutationoperators.methodlevel.isd;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class ISD extends MutationOperator {

	public ISD() {
		this(null);
	}
	
	public ISD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new ISD_Matcher(this);
		this.twoAST_visitor = new ISD_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ISD");
		this.mutopproperty.setFullname("Super keyword deletion");
		this.mutopproperty.setDescription("Deletes occurrences of the super keyword so that a reference to the variable or the method goes to the overriding instance variable or method.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}

}
