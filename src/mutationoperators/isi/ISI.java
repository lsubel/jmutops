package mutationoperators.isi;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class ISI extends MutationOperator {

	public ISI() {
		this(null);
	}
	
	public ISI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new ISI_Matcher(this);
		this.visitor = new ISI_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ISI");
		this.mutopproperty.setFullname("Super keyword insertion");
		this.mutopproperty.setDescription("Inserts the super keyword so that a reference to the variable or the method goes to the overridden instance variable or method.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}

	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		return super.check(rightNode, leftNode);
	}
}