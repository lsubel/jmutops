package mutationoperators;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import mutationoperators.jti.JTI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import enums.MutationOperatorCategory;

import results.DatabaseResults;
import results.ResultListenerMulticaster;

public abstract class MutationOperator{

	
	
	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	protected final MutationOperatorCategory category;
	
	/**
	 * TODO: add javadoc
	 */
	protected final ResultListenerMulticaster eventListener;
	
	/**
	 * Logger
	 */
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Reference to the ASTVisitor related to this Mutation Operator
	 */
	protected BaseASTVisitor visitor;
	
	/**
	 * Reference to the ASTMatcher related to this Mutation Operator
	 */
	protected BaseASTMatcher matcher;
		
	/**
	 * Full name of this mutation operator
	 */
	protected final String fullname;
	
	/////////////////////////////////////////////////
	///	Methods
	/////////////////////////////////////////////////	

	/**
	 * Default constructor, initializing the logger.
	 */
	public MutationOperator(ResultListenerMulticaster eventListener, MutationOperatorCategory category, String fullname) {
		// check null argument
		if(eventListener == null){
			throw new IllegalArgumentException("ResultListenerMulticaster eventListener cannot be null.");
		}
		if(category == null){
			throw new IllegalArgumentException("MutationOperatorCategory category cannot be null.");
		}
		// assign fields
		this.eventListener	= eventListener;
		this.category 		= category;
		this.fullname 		= fullname;
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
		// generate log message 
		logger.fine("Found application of " + this.getClass().getSimpleName() + " operator:" + "\n" +
		"\t" + "Prefix version: " + "\n" +
		"\t\t" + "Content: " + leftNode.toString()  + "\n" +
		"\t\t" + "Node type: " + leftNode.getClass().toString() + "\n" +
		"\t\t" + "Range: " + leftNode.getStartPosition() + "-" + (leftNode.getStartPosition() + leftNode.getLength() - 1) + "\n" +
		"\t" + "Postfix version: " + "\n" +
		"\t\t" + "Content: " + rightNode.toString() + "\n" +
		"\t\t" + "Node type: " + rightNode.getClass().toString() + "\n" +
		"\t\t" + "Range: " + rightNode.getStartPosition() + "-" + (rightNode.getStartPosition() + rightNode.getLength() - 1)+ "\n");
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
	public String getFullname() {
		return fullname;
	}

}
