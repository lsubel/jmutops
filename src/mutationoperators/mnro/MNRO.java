package mutationoperators.mnro;

import org.eclipse.jdt.core.dom.ASTNode;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;
import mutationoperators.MutationOperator.MutationOperatorCategory;
import mutationoperators.aor.AOR_Matcher;
import mutationoperators.aor.AOR_Visitor;
import mutationoperators.jti.JTI_Matcher;
import mutationoperators.jti.JTI_Visitor;

public class MNRO extends MutationOperator {

	public MNRO(MutationOperatorChecker checker) {
		super(checker, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new MNRO_Matcher(this);
		this.visitor = new MNRO_Visitor(this.matcher);
	}

	@Override
	public void check(ASTNode leftNode, ASTNode rightNode) {
		this.visitor.setSecondTree(rightNode);
		leftNode.accept(visitor);
	}

	@Override
	public void found(ASTNode leftNode, ASTNode rightNode) {
		logger.info("Found application of MNRO operator:" + "\n" +
		"\t" + "Prefix version: " + "\n" +
		"\t\t" + "Content: " + leftNode.toString()  + "\n" +
		"\t\t" + "Node type: " + leftNode.getClass().toString() + "\n" +
		"\t\t" + "Range: " + leftNode.getStartPosition() + "-" + (leftNode.getStartPosition() + leftNode.getLength() - 1) + "\n" +
		"\t" + "Postfix version: " + "\n" +
		"\t\t" + "Content: " + rightNode.toString() + "\n" +
		"\t\t" + "Node type: " + rightNode.getClass().toString() + "\n" +
		"\t\t" + "Range: " + rightNode.getStartPosition() + "-" + (rightNode.getStartPosition() + rightNode.getLength() - 1)+ "\n");
	}

}
