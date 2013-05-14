package mutationoperators;
import java.util.logging.Logger;

import mutationoperators.jti.JTI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import results.DatabaseResults;

public abstract class MutationOperator {

	public enum MutationOperatorCategory {
		METHOD_LEVEL("Method-level operator"), 
		CLASS_LEVEL("Class-level operator");
		
		private final String label;
		
		private MutationOperatorCategory(String label){
			this.label = label;
		}
		
		public String toString(){
			return this.label;
		}
	}
	
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
	public abstract void check(ASTNode leftNode, ASTNode rightNode);
	
	/**
	 * Method called when an application of an mutation operator was found.
	 * 
	 * @param leftNode The prefix code.
	 * @param rightNode The postfix code.
	 */
	public abstract void found(ASTNode leftNode, ASTNode rightNode);
	
	
	/**
	 * Get the mutation operator's category.
	 * 
	 * @return The category associated with the mutation operator.
	 */
	public MutationOperatorCategory getCategory(){
		return this.category;
	}
}
