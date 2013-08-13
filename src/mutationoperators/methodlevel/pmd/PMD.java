package mutationoperators.methodlevel.pmd;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.pci.PCI_Matcher;
import mutationoperators.methodlevel.pci.PCI_Visitor;
import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class PMD extends MutationOperator {

	public PMD() {
		this(null);
	}
	
	public PMD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new PCI_Matcher(this);
		this.twoAST_visitor = new PCI_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("PMD");
		this.mutopproperty.setFullname("Member variable declaration with parent class type");
		this.mutopproperty.setDescription("Changes the declared type of an object reference to the parent of the original declared type.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}

}
