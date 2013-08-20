package mutationoperators.methodlevel.loi;

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
		this.twoAST_matcher = new LOI_Matcher(this);
		this.twoAST_visitor = new LOI_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("LOI");
		this.mutopproperty.setFullname("Logical Operator Insertion");
		this.mutopproperty.setDescription("Delete unary logical operator.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setTwoAST();
		this.mutopproperty.setUpdate();
	}
	
	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		return super.check(rightNode, leftNode);
	}
}
