package mutationoperators;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class BaseASTVisitor extends ASTVisitor {
	
	/**
	 * Default ASTMatcher to match general properties
	 */
	protected final ASTMatcher defaultMatcher;
	
	/**
	 * Mutation operator specific matcher.
	 */
	protected final BaseASTMatcher matcher;

	/**
	 * Default constructor.
	 * @param matcher Mutation operator specific matcher.
	 */
	public BaseASTVisitor(BaseASTMatcher matcher) {
		this.matcher = matcher;
		this.defaultMatcher = new ASTMatcher();
	}
}
