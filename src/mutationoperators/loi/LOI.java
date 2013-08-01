package mutationoperators.loi;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class LOI extends MutationOperator {

	public LOI() {
		this(null);
	}
	
	public LOI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new LOI_Matcher(this);
		this.visitor = new LOI_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("LOI");
		this.mutopproperty.setFullname("Logical Operator Insertion");
		this.mutopproperty.setDescription("Delete unary logical operator.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
	
	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		return super.check(rightNode, leftNode);
	}
}
