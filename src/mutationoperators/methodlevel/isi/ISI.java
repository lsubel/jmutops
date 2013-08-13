package mutationoperators.methodlevel.isi;

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
		this.twoAST_matcher = new ISI_Matcher(this);
		this.twoAST_visitor = new ISI_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("ISI");
		this.mutopproperty.setFullname("Super keyword insertion");
		this.mutopproperty.setDescription("Inserts the super keyword so that a reference to the variable or the method goes to the overridden instance variable or method.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setUpdate();
	}

	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		return super.check(rightNode, leftNode);
	}
}
