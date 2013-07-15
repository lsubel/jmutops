package mutationoperators;

import org.eclipse.jdt.core.dom.ASTNode;

import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import results.JMutOpsEventListenerMulticaster;

public abstract class MutationOperator{

	
	
	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	protected final MutationOperatorLevel level;
	
	protected final MutationOperatorCategory category;
	
	protected final String fullname;
	
	/**
	 * Short cut of the mutation operator.
	 */
	protected final String shortname;
	
	protected final String description;
	
	/**
	 * Reference to JMutOpsEventListenerMulticaster. Required to fire the {@link results.JMutOpsEventListenerMulticaster#OnMatchingFound(MutationOperator, ASTNode, ASTNode) OnMatchingFound()} method. <p>
	 * Will be null and not firing event when object is initialized by default constructor.
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
	
	protected int application_counter;
	
	/////////////////////////////////////////////////
	///	Methods
	/////////////////////////////////////////////////	


	/**
	 * Default constructor.
	 * @param fullname
	 * @param shortname
	 * @param description
	 * @param level
	 * @param eventListener
	 * @param category
	 */
	public MutationOperator(String fullname, String shortname, String description, MutationOperatorLevel level, JMutOpsEventListenerMulticaster eventListener, MutationOperatorCategory category) {
		// check null argument
		if(level == null){
			throw new IllegalArgumentException("MutationOperatorLevel level cannot be null.");
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
		this.level 			= level;
		this.category		= category;
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
	public int check(ASTNode leftNode, ASTNode rightNode){
		// reset application counter
		this.application_counter = 0;
		// start to visit the subAST
		this.visitor.setSecondTree(rightNode);
		leftNode.accept(visitor);
		// return the number of detected matches
		return this.application_counter;
	}
	
	/**
	 * Method called when an application of an mutation operator was found.
	 * 
	 * @param leftNode The prefix code.
	 * @param rightNode The postfix code.
	 */
	public void found(ASTNode leftNode, ASTNode rightNode){
		// increment the number of detected applications
		application_counter += 1;
		// notify other ResultInterfaces
		if(this.eventListener != null){
			this.eventListener.OnMatchingFound(this, leftNode, rightNode);
		}
	}
	
	
	/**
	 * Get the mutation operator's category.
	 * <p>
	 * @return The category associated with the mutation operator.
	 */
	public MutationOperatorLevel getCategory(){
		return this.level;
	}
	

	/**
	 * Get the mutation operator's full name.
	 * <p>
	 * @return The full name.
	 */
	public String getFullname(){
		return this.fullname;
	}

	/**
	 * Get the mutation operator's short cut.
	 * <p>
	 * @return The short cut.
	 */
	public String getShortname() {
		return shortname;
	}

	/**
	 * Get a description of the mutation operator.
	 * <p>
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	

}
