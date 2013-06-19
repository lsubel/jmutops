package mutationoperators;

import org.eclipse.jdt.core.dom.ASTNode;
import enums.MutationOperatorCategory;
import results.ResultListenerMulticaster;

public abstract class MutationOperator{

	
	
	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	protected final MutationOperatorCategory category;
	
	protected final String fullname;
	
	/**
	 * TODO: add javadoc
	 */
	protected final ResultListenerMulticaster eventListener;
	

	
	/**
	 * Reference to the ASTVisitor related to this Mutation Operator
	 */
	protected BaseASTVisitor visitor;
	
	/**
	 * Reference to the ASTMatcher related to this Mutation Operator
	 */
	protected BaseASTMatcher matcher;
	
	/////////////////////////////////////////////////
	///	Methods
	/////////////////////////////////////////////////	

	/**
	 * Default constructor, initializing the logger.
	 */
	public MutationOperator(ResultListenerMulticaster eventListener, MutationOperatorCategory category, String name) {
		// check null argument
		if(eventListener == null){
			throw new IllegalArgumentException("ResultListenerMulticaster eventListener cannot be null.");
		}
		if(category == null){
			throw new IllegalArgumentException("MutationOperatorCategory category cannot be null.");
		}
		if(name == null){
			throw new IllegalArgumentException("String name cannot be null.");
		}
		// assign fields
		this.eventListener	= eventListener;
		this.category 		= category;
		this.fullname 		= name;
	}
		
	/**
	 * Check prefix and postfix versions of an source file for applied mutation operator.
	 * 
	 * @param leftNode The prefix code.
	 * @param rightNode The postfix code.
	 */
	public void check(ASTNode leftNode, ASTNode rightNode){
		// start to visit the subAST
		this.visitor.setSecondTree(rightNode);
		leftNode.accept(visitor);
	}
	
	/**
	 * Method called when an application of an mutation operator was found.
	 * 
	 * @param leftNode The prefix code.
	 * @param rightNode The postfix code.
	 */
	public void found(ASTNode leftNode, ASTNode rightNode){
		// notify other ResultInterfaces
		this.eventListener.OnMatchingFound(this, leftNode, rightNode);
	}
	
	
	/**
	 * Get the mutation operator's category.
	 * <p>
	 * @return The category associated with the mutation operator.
	 */
	public MutationOperatorCategory getCategory(){
		return this.category;
	}
	

	/**
	 * Get the mutation operator's full name.
	 * <p>
	 * @return The full name.
	 */
	public String getFullname(){
		return this.fullname;
	}

}
