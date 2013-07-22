package mutationoperators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import results.JMutOpsEventListenerMulticaster;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

/**
 * Class that stores and maintains the implemented MutationOperators. <p>
 * It handles the requests to check for matching mutation operators 
 * and performs some preparing steps.
 * @author Lukas Subel
 * 
 */
public class MutationOperatorChecker {

	//////////////////////////////////////////////////////
	/// Fields
	//////////////////////////////////////////////////////

	/**
	 * Array containing all method level related mutation operators.
	 */
	private ArrayList<MutationOperator> methodlevel_list;

	/**
	 * Array containing all class level related mutation operators.
	 */
	private ArrayList<MutationOperator> classlevel_list;
	
	/**
	 * Multicaster which will talk to all ResultListeners which were added
	 */
	private JMutOpsEventListenerMulticaster listener = new JMutOpsEventListenerMulticaster();
	
	// ////////////////////////////////////////////////////
	// / Methods
	// ////////////////////////////////////////////////////

	/**
	 * Default constructor.
	 * @param listener TODO
	 */
	public MutationOperatorChecker(JMutOpsEventListenerMulticaster listener) {
		this.methodlevel_list = new ArrayList<MutationOperator>();
		this.classlevel_list  = new ArrayList<MutationOperator>();
		this.listener 		  = listener;
	}

	/**
	 * Add another MutationOperator to the Checker.
	 * <p>
	 * 
	 * @param mutop
	 *            MutationOperator which should be used on checked ASTs.
	 * @return True if the MutationOperator was added, otherwise false.
	 */
	public boolean addMutationOperator(MutationOperator mutop) {
		switch (mutop.getCategory()) {
		case CLASS_LEVEL:
			this.listener.OnMutationOperatorInit(mutop);
			// check if this operator was added before
			if (!this.classlevel_list.contains(mutop)) {
				// if not, add it
				this.classlevel_list.add(mutop);
				return true;
			} else {
				return false;
			}
		case METHOD_LEVEL:
			this.listener.OnMutationOperatorInit(mutop);
			// check if this operator was added before
			if (!this.methodlevel_list.contains(mutop)) {
				// if not, add it
				this.methodlevel_list.add(mutop);
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
	}

	public void checkForMutationOperators(ASTNode node, SourceCodeChange change) {
		//
		if (change instanceof Insert) {
			this.check(node, (Insert) change);
		} else if (change instanceof Delete) {
			this.check(node, (Delete) change);
		} else {
			throw new IllegalStateException(
					"Could not found correct subclass for change on a single version.");
		}
	}

	/**
	 * This method runs the following steps:  
	 * <ul>
	 * 	<li> It selects a subset of all registered {@link MutationOperator} 
	 * 		which could occur in this {@link SourceCodeChange}, </li>
	 * 	<li> It tries to select a subAST depending on the the {@link SourceCodeChange}, </li>
	 * 	<li> It calls the {@link MutationOperator} to check for a match. </li>
	 * </ul>	
	 * 
	 * @param leftNode
	 *            AST with the prefix version.
	 * @param rightNode
	 *            AST with the postfix version.
	 * @param change
	 *            ChangeDistiller object describing the change related betweeen
	 *            both AST versions.
	 */
	public void checkForMutationOperators(ASTNode leftNode, ASTNode rightNode,
			SourceCodeChange change) {
		//
		if (change instanceof Update) {
			this.check(leftNode, rightNode, (Update) change);
		} else if (change instanceof Move) {
			this.check(leftNode, rightNode, (Move) change);
		} else {
			throw new IllegalStateException(
					"Could not found correct subclass for change on two versions.");
		}
	}

	private void check(ASTNode node, Insert change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {

			case COMMENT_INSERT:
				// since comment updates do not fix bugs, we ignore this case
				break;
			case ADDITIONAL_CLASS:
			case ADDITIONAL_FUNCTIONALITY:
			case ADDITIONAL_OBJECT_STATE:
			case ALTERNATIVE_PART_INSERT:
			case STATEMENT_INSERT:
			case UNCLASSIFIED_CHANGE:
				// default case means some error
			default:
				throw new IllegalStateException(
						"Expected body insert, but found "
								+ change.getChangeType().toString());
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case DOC_INSERT:
				// since comment updates do not fix bugs, we ignore this case
				break;
			case ADDING_ATTRIBUTE_MODIFIABILITY:
			case ADDING_CLASS_DERIVABILITY:
			case ADDING_METHOD_OVERRIDABILITY:
			case PARAMETER_INSERT:
			case PARENT_CLASS_INSERT:
			case PARENT_INTERFACE_INSERT:
			case RETURN_TYPE_INSERT:
			default:
				throw new IllegalStateException(
						"Expected class insert, but found "
								+ change.getChangeType().toString());
			}
		}
	}

	private void check(ASTNode node, Delete change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {
			case COMMENT_DELETE:
				// since comment updates do not fix bugs, we ignore this case
				break;				
			case ALTERNATIVE_PART_DELETE:
			case REMOVED_CLASS:
			case REMOVED_FUNCTIONALITY:
			case REMOVED_OBJECT_STATE:
			case STATEMENT_DELETE:
			case UNCLASSIFIED_CHANGE:
				// default case means some error
			default:
				throw new IllegalStateException(
						"Expected body delete, but found "
								+ change.getChangeType().toString());
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case DOC_DELETE:
				// since comment updates do not fix bugs, we ignore this case
				break;
			case PARAMETER_DELETE:
			case PARENT_CLASS_DELETE:
			case PARENT_INTERFACE_DELETE:
			case RETURN_TYPE_DELETE:
			default:
				throw new IllegalStateException(
						"Expected class delete, but found "
								+ change.getChangeType().toString());
			}
		}
	}

	private void check(ASTNode leftNode, ASTNode rightNode, Move change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {

			case COMMENT_MOVE:
				// since comment updates do not fix bugs, we ignore this case
				break;

			case STATEMENT_ORDERING_CHANGE:
			case STATEMENT_PARENT_CHANGE:
			case UNCLASSIFIED_CHANGE:
				break;
				
			default:
				throw new IllegalStateException(
						"Expected body move, but found "
								+ change.getChangeType().toString());
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			default:
				throw new IllegalStateException(
						"Expected class move, but found "
								+ change.getChangeType().toString());
			}
		}
	}

	private void check(ASTNode leftNode, ASTNode rightNode, Update change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {

			// in case of a body change
			switch (change.getChangeType()) {

			case COMMENT_UPDATE:
				// since comment updates do not fix bugs, we ignore this case
				break;

			case CONDITION_EXPRESSION_CHANGE:
				// check only the condition of the node
				if ((leftNode instanceof IfStatement)
						&& (rightNode instanceof IfStatement)) {
					runMutationOperators(this.methodlevel_list,
							((IfStatement) leftNode).getExpression(),
							((IfStatement) rightNode).getExpression());
				} else if ((leftNode instanceof WhileStatement)
						&& (rightNode instanceof WhileStatement)) {
					runMutationOperators(this.methodlevel_list,
							((WhileStatement) leftNode).getExpression(),
							((WhileStatement) rightNode).getExpression());
				} else if ((leftNode instanceof DoStatement)
						&& (rightNode instanceof DoStatement)) {
					runMutationOperators(this.methodlevel_list,
							((DoStatement) leftNode).getExpression(),
							((DoStatement) rightNode).getExpression());
				} else if ((leftNode instanceof ForStatement)
						&& (rightNode instanceof ForStatement)) {
					runMutationOperators(this.methodlevel_list,
							((ForStatement) leftNode).getExpression(),
							((ForStatement) rightNode).getExpression());
				} else {
					throw new IllegalStateException(
							"Could not found ASTNode with condition: "
									+ leftNode.getClass().getName() + " & "
									+ rightNode.getClass().getName());
				}
				break;

			case STATEMENT_UPDATE:
			case UNCLASSIFIED_CHANGE:
				// in this case, we cannot specify the area to search
				runMutationOperators(this.methodlevel_list, leftNode, rightNode);
				break;

			default:
				throw new IllegalStateException(
						"Expected body update, but found "
								+ change.getChangeType().toString());
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case DOC_UPDATE:
				// since comment updates do not fix bugs, we ignore this case
				break;
				
			case ATTRIBUTE_RENAMING:
			case ATTRIBUTE_TYPE_CHANGE:
				
			case CLASS_RENAMING:
				
			case DECREASING_ACCESSIBILITY_CHANGE:


			case INCREASING_ACCESSIBILITY_CHANGE:
				
			case METHOD_RENAMING:

			case PARAMETER_ORDERING_CHANGE:
			case PARAMETER_RENAMING:
			case PARAMETER_TYPE_CHANGE:
				
			case PARENT_CLASS_CHANGE:
			case PARENT_INTERFACE_CHANGE:

			case REMOVING_ATTRIBUTE_MODIFIABILITY:
			case REMOVING_CLASS_DERIVABILITY:
			case REMOVING_METHOD_OVERRIDABILITY:
				
			case RETURN_TYPE_CHANGE:
				
			case UNCLASSIFIED_CHANGE:
				break;
			default:
				throw new IllegalStateException(
						"Expected class change, but found "
								+ change.getChangeType().toString());
			}
		}

	}

	/**
	 * Helper method. Check for each MutationOperator in operatorlist, if
	 * leftNode to rightNode applied the corresponding operator.
	 * 
	 * @param operatorlist
	 *            A {@link List} of {@link MutationOperator} containing all
	 *            MutationOperators to check in this case.
	 * @param leftNode
	 *            The prefixed version.
	 * @param rightNode
	 *            The postfixed version.
	 */
	private void runMutationOperators(List<MutationOperator> operatorlist,
			ASTNode leftNode, ASTNode rightNode) {
		// initialize a variable which counts the number of detected applications
		int detected_applications = 0;
		// check all mutation operators
		for (MutationOperator operator : operatorlist) {
			detected_applications += operator.check(leftNode, rightNode);
		}
		// fire event when there was no matching detected
		if(detected_applications == 0){
			this.listener.OnNoMatchingFound(operatorlist);
		}
	}	
}
