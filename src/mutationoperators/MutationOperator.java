package mutationoperators;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorLevel;

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
 */
public abstract class MutationOperator{

	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	protected final MutationOperatorProperty mutopproperty;
	
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
	 * @param eventListener
	 */
	public MutationOperator(JMutOpsEventListenerMulticaster eventListener) {
		// assign fields; set property object
		this.eventListener	= eventListener;
		this.mutopproperty  = new MutationOperatorProperty(); 
		this.setProperties();
	}
	
	/**
	 * Set the field of a {@link MutationOperatorProperty} object for the specific mutation operator.
	 */
	protected abstract void setProperties();
		
	/**
	 * Check prefix and postfix versions of an source file for applied mutation operator.
	 * Always should call {@link MutationOperator#check0(ASTNode, ASTNode)}.
	 * 
	 * @param leftNode The prefix code.
	 * @param rightNode The postfix code.
	 */
	public int check(ASTNode leftNode, ASTNode rightNode){
		return this.check0(leftNode, rightNode);
	}
	
	
	/**
	 * Internal method; Check prefix and postfix versions of an source file for applied mutation operator
	 * and returns the number of detected applications.
	 * @param left The prefix code.
	 * @param right The postfix code. 
	 * @return The number of detected applications.
	 */
	protected final int check0(ASTNode left, ASTNode right){
		// reset application counter
		this.application_counter = 0;
		// start to visit the subAST
		this.visitor.setParallelTree(right);
		left.accept(visitor);
		// return the number of detected matches
		return this.application_counter;		
	}
	
	/** 
	 * Method called when an application of an mutation operator was found with two ASTs.
	 * <p>
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
	 * Method called when an application of an mutation operator was found with one AST.
	 * <p>
	 * @param leftNode The prefix code.
	 * @param rightNode The postfix code.
	 */
	public void found(ASTNode node){
		// increment the number of detected applications
		application_counter += 1;
		// notify other ResultInterfaces
		if(this.eventListener != null){
			this.eventListener.OnMatchingFound(this, node);
		}
	}
	
	/**
	 * Get the mutation operator's category.
	 * <p>
	 * @return The category associated with the mutation operator.
	 */
	public MutationOperatorLevel getCategory(){
		return this.mutopproperty.getLevel();
	}
	

	/**
	 * Get the mutation operator's full name.
	 * <p>
	 * @return The full name.
	 */
	public String getFullname(){
		return this.mutopproperty.getFullname();
	}

	/**
	 * Get the mutation operator's short cut.
	 * <p>
	 * @return The short cut.
	 */
	public String getShortname() {
		return this.mutopproperty.getShortname();
	}

	/**
	 * Get a description of the mutation operator.
	 * <p>
	 * @return The description.
	 */
	public String getDescription() {
		return this.mutopproperty.getDescription();
	}
	
	

}
