package mutationoperators;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import mutationoperators.jti.JTI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import enums.MutationOperatorCategory;

import results.DatabaseResults;

public abstract class MutationOperator{

	
	
	/////////////////////////////////////////////////
	///	Fields
	/////////////////////////////////////////////////	
	
	/**
	 * Stores an enum to distinguish between classlevel operators and methodlevel operators.
	 */
	protected final MutationOperatorCategory category;
	
	/**
	 * Reference to the owner to get run specific information, f.e. the tested file.
	 */
	protected final MutationOperatorChecker mutopscheck;
	
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
		
	/////////////////////////////////////////////////
	///	Methods
	/////////////////////////////////////////////////	
	
	
	/**
	 * Default constructor, initializing the logger.
	 */
	public MutationOperator(MutationOperatorChecker checker, MutationOperatorCategory category) {
		// check null argument
		if(checker == null){
			throw new IllegalArgumentException("MutationOperatorChecker checker cannot be null.");
		}
		if(category == null){
			throw new IllegalArgumentException("MutationOperatorCategory category cannot be null.");
		}
		// assign fields
		this.mutopscheck = checker;
		this.category = category;
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
		this.mutopscheck.foundMatching(this, leftNode, rightNode);
	}
	
	
	/**
	 * Get the mutation operator's category.
	 * 
	 * @return The category associated with the mutation operator.
	 */
	public MutationOperatorCategory getCategory(){
		return this.category;
	}
}
