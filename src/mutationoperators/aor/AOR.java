package mutationoperators.aor;

import org.eclipse.jdt.core.dom.ASTNode;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BasicTwo_ASTVisitor;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;
import mutationoperators.MutationOperator.MutationOperatorCategory;

public class AOR extends MutationOperator {

	public AOR(MutationOperatorChecker checker) {
		super(checker, MutationOperatorCategory.METHOD_LEVEL);
	}

	@Override
	public void check(ASTNode leftNode, ASTNode rightNode) {
		BaseASTMatcher matcher = new AOR_Matcher(this);
		BasicTwo_ASTVisitor visitor = new AOR_Visitor(matcher, rightNode);
		leftNode.accept(visitor);
	}

	@Override
	public void found(ASTNode leftNode, ASTNode rightNode) {
		logger.info("Found application of AOR operator:" + "\n" +
		"\t" + "Prefix version:" + "\n" +
		"\t\t" + "Content: " + leftNode.toString()  + "\n" +
		"\t\t" + "Node type: " + leftNode.getClass().toString() + "\n" +
		"\t\t" + "Range: " + leftNode.getStartPosition() + "-" + (leftNode.getStartPosition() + leftNode.getLength() - 1) + "\n" +
		"\t" + "Postfix version:" + "\n" +
		"\t\t" + "Content: " + rightNode.toString() + "\n" +
		"\t\t" + "Node type: " + rightNode.getClass().toString() + "\n" +
		"\t\t" + "Range: " + rightNode.getStartPosition() + "-" + (rightNode.getStartPosition() + rightNode.getLength() - 1)+ "\n");
	}

}
