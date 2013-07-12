package mutationoperators;

import org.eclipse.jdt.core.dom.ASTNode;
import enums.MutationOperatorCategory;
import results.JMutOpsEventListenerMulticaster;

public abstract class MutationOperator{

	
	
	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	protected final MutationOperatorCategory category;
	
	protected final String fullname;
	
	protected final String shortname;
	
	protected final String description;
	
	/**
	 * TODO: add javadoc
	 */
	protected final JMutOpsEventListenerMulticaster eventListener;
	
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
	 * @param shortname TODO
	 * @param description TODO
	 */
	public MutationOperator(JMutOpsEventListenerMulticaster eventListener, MutationOperatorCategory category, String fullname, String shortname, String description) {
		// check null argument
		if(eventListener == null){
			throw new IllegalArgumentException("ResultListenerMulticaster eventListener cannot be null.");
		}
		if(category == null){
			throw new IllegalArgumentException("MutationOperatorCategory category cannot be null.");
		}
		if(fullname == null){
			throw new IllegalArgumentException("String name fullname be null.");
		}
		if(shortname == null){
			throw new IllegalArgumentException("String shortname cannot be null.");
		}
		if(description == null){
			throw new IllegalArgumentException("String description cannot be null.");
		}
		// assign fields
		this.eventListener	= eventListener;
		this.category 		= category;
		this.fullname 		= fullname;
		this.shortname		= shortname;
		this.description    = description;
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
