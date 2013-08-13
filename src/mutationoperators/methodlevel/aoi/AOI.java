package mutationoperators.methodlevel.aoi;

import mutationoperator.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class AOI extends MutationOperator {

	public AOI() {
		this(null);
	}
	
	public AOI(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new AOI_Matcher(this);
		this.twoAST_visitor = new AOI_Visitor(this.twoAST_matcher);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("AOI");
		this.mutopproperty.setFullname("Arithmetic Operator Insertion");
		this.mutopproperty.setDescription("Insert basic unary/short-cut arithmetic operators.");
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
