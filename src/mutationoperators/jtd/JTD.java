package mutationoperators.jtd;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class JTD extends MutationOperator {

	public JTD() {
		this(null);
	}
	
	public JTD(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new JTD_Matcher(this);
		this.twoAST_visitor = new JTD_Visitor(twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("JTD");
		this.mutopproperty.setFullname("This keyword deletion");
		this.mutopproperty.setDescription("Deletes uses of the keyword this.");
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
