package mutationoperators.jti;



import java.util.logging.Logger;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperator.MutationOperatorCategory;
import mutationoperators.MutationOperatorChecker;

import org.eclipse.jdt.core.dom.ASTNode;

import results.DatabaseResults;


public class JTI extends MutationOperator {

	public JTI(MutationOperatorChecker checker) {
		super(checker, MutationOperatorCategory.METHOD_LEVEL);
	}

	@Override
	public void check(ASTNode leftNode, ASTNode rightNode) {
		BaseASTMatcher matcher = new JTI_Matcher(this);
		BaseASTVisitor visitor = new JTI_Visitor(matcher, rightNode);
		leftNode.accept(visitor);
	}

	@Override
	public void found(ASTNode leftNode, ASTNode rightNode) {
		logger.info("Found application of JTI operator:" + "\n" +
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
