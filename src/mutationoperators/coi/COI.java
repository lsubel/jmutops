package mutationoperators.coi;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class COI extends MutationOperator {

	public COI() {
		this(null);
	}
	
	public COI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.matcher = new COI_Matcher(this);
		this.visitor = new COI_Visitor(this.matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("COI");
		this.mutopproperty.setFullname("Conditional Operator Insertion");
		this.mutopproperty.setDescription("Insert unary conditional operators.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
	}
	
	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		return super.check(rightNode, leftNode);
	}
}