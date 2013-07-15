package mutationoperators;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.StructureEntityVersion;

import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;
import results.JMutOpsEventListenerMulticaster;

/**
 * General (abstract) representation of a mutation operator.<p>
 * <p>
 * Each mutation operator has a
 * <ul>
 * <li>{@link BaseAstVisitor} <code>visitor</code> that traverse in a mutation operator specific way 
 * over the prefix and postfix ASTs. During the traversing there are some conditional checked 
 * and when the results are valid, the {@link BaseASTMatcher} <code>matcher</code> will be called. </li>
 * <li>{@link BaseASTMatcher} <code>matcher</code> that search for a mutation operator specific matching in two ASTNodes. 
 * Fires an event {@link results.JMutOpsEventListener#OnMatchingFound(MutationOperator, ASTNode, ASTNode) OnMatchingFound(MutationOperator, ASTNode, ASTNode)} when a matching was detected. 
 * </ul>
 * @author Lukas Subel
 *
 */
public abstract class MutationOperator{

	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	protected final MutationOperatorLevel level;
	
	/**
	 * Stores the category of the mutation operator.
	 */
	protected final MutationOperatorCategory category;
	
	/**
	 * Stores the full name of the mutation operator.
	 */
	protected final String fullname;
	
	/**
	 * Short cut of the mutation operator.
	 */
	protected final String shortname;
	
	/**
	 * A description of the mutation operator's effect.
	 */
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
	
	/**
	 * Internal counter which collects the number of detected matchings within a {@link mutationoperators.MutationOperator#check(ASTNode, ASTNode) check(ASTNode, ASTNode)} call.
	 */
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
