package mutationoperators.pci;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class PCI extends MutationOperator {

	public PCI() {
		this(null);
	}
	
	public PCI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new PCI_Matcher(this);
		this.visitor = new PCI_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("PCI");
		this.mutopproperty.setFullname("Type cast operator insertion");
		this.mutopproperty.setDescription("Changes the actual type of an object reference to the parent or child of the original declared type.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
	
	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		return super.check(rightNode, leftNode);
	}
}