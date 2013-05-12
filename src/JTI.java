import org.eclipse.jdt.core.dom.ASTNode;


public class JTI extends MutationOperator {

	@Override
	public void check(ASTNode leftNode, ASTNode rightNode) {
		BaseASTMatcher matcher = new JTI_Matcher(this);
		BaseASTVisitor visitor = new JTI_Visitor(matcher, rightNode);
		leftNode.accept(visitor);
	}

	@Override
	public void found(ASTNode leftNode, ASTNode rightNode) {
		System.out.println("Found application:");
		System.out.println("\t" + "Prefix version:");
		System.out.println("\t\t" + "Content:" + leftNode.toString());
		System.out.println("\t\t" + "Node type:" + leftNode.getClass().toString());
		System.out.println("\t\t" + "Range: " + leftNode.getStartPosition() + "-" + (leftNode.getStartPosition() + leftNode.getLength()));
		System.out.println("\t" + "Postfix version:");
		System.out.println("\t\t" + "Content:" + rightNode.toString());
		System.out.println("\t\t" + "Node type:" + rightNode.getClass().toString());
		System.out.println("\t\t" + "Range: " + rightNode.getStartPosition() + "-" + (rightNode.getStartPosition() + rightNode.getLength()));
	}

}
