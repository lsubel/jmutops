package jmutops;

import java.util.ArrayList;
import java.util.List;

import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorProperty;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import results.JMutOpsEventListenerMulticaster;
import utils.Preperator;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

/**
 * Class that stores and maintains the implemented MutationOperators.
 * <p>
 * It handles the requests to check for matching mutation operators and performs
 * some preparing steps.
 * 
 * @author Lukas Subel
 * 
 */
public class MutationOperatorTester {

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
	 * Array containing all class and method level related mutation operators.
	 */
	private ArrayList<MutationOperator> bothlevel_list;
	
	/**
	 * Multicaster which will talk to all ResultListeners which were added
	 */
	private JMutOpsEventListenerMulticaster listener = new JMutOpsEventListenerMulticaster();

	// ////////////////////////////////////////////////////
	// / Methods
	// ////////////////////////////////////////////////////

	/**
	 * Default constructor.
	 * 
	 * @param listener Reference to the event multi caster so it can fire events.
	 */
	public MutationOperatorTester(JMutOpsEventListenerMulticaster listener) {
		this.methodlevel_list 	= new ArrayList<MutationOperator>();
		this.classlevel_list 	= new ArrayList<MutationOperator>();
		this.bothlevel_list 	= new ArrayList<MutationOperator>();
		this.listener = listener;
	}

	/**
	 * Add another {@link MutationOperator} to be included in testings.
	 * <p>
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
		case BOTH_LEVELS:
			this.listener.OnMutationOperatorInit(mutop);
			// check if this operator was added before
			if (!this.bothlevel_list.contains(mutop)) {
				// if not, add it
				this.bothlevel_list.add(mutop);
				return true;
			} else {
				return false;
			}
			
		default:
			// default case means some error
			String error_message = "Unexpected MutationOperatorCategory in input MutationOperator: " + mutop.getCategory().toString();
			this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - addMutationOperator(MutationOperator)" , error_message);
			throw new IllegalArgumentException(error_message);
		}
	}

	/**
	 * Check for any applied {@link MutationOperator} which occurred in the {@link ASTNode} {@code node} 
	 * related to the {@link SourceCodeChange} {@code change}.
	 * @param node The {@link ASTNode} related to the {@code change}.
	 * @param change The {@link SourceCodeChange} retrieved from ChangeDistiller.
	 */
	public void checkForMutationOperators(ASTNode node, SourceCodeChange change) {
		if (change instanceof Insert) {
			this.check(node, (Insert) change);
		} else if (change instanceof Delete) {
			this.check(node, (Delete) change);
		} else {
			// default case means some error
			String error_message = "Unexpected subclass for change on one version.: " + change.getClass().getSimpleName();
			this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - checkForMutationOperators(ASTNode, SourceCodeChange)" , error_message);
			throw new IllegalArgumentException(error_message);
		}
	}

	/**
	 * This method runs the following steps:
	 * <ul>
	 * <li>It selects a subset of all registered {@link MutationOperator} which
	 * could occur in this {@link SourceCodeChange},</li>
	 * <li>It tries to select a subAST depending on the the
	 * {@link SourceCodeChange},</li>
	 * <li>It calls the {@link MutationOperator} to check for a match.</li>
	 * </ul>
	 * 
	 * @param leftNode
	 *            {@link ASTNode} with the prefix version.
	 * @param rightNode
	 *            {@link ASTNode} with the postfix version.
	 * @param change
	 *            ChangeDistiller object describing the change related betweeen
	 *            both {@link ASTNode} versions.
	 */
	public void checkForMutationOperators(ASTNode leftNode, ASTNode rightNode,
			SourceCodeChange change) {
		if (change instanceof Update) {
			this.check(leftNode, rightNode, (Update) change);
		} else if (change instanceof Move) {
			this.check(leftNode, rightNode, (Move) change);
		} else {
			// default case means some error
			String error_message = "Unexpected subclass for change on two versions.: " + change.getClass().getSimpleName();
			this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - checkForMutationOperators(ASTNode, ASTNode, SourceCodeChange)" , error_message);
			throw new IllegalArgumentException(error_message);
		}
	}


	/**
	 * Check for {@link MutationOperator} which implement the method 
	 * {@link MutationOperator#check(List, Preperator, Preperator)}. 
	 * @param changes All detected {@link SourceCodeChange} found in the file under test.
	 * @param prefixed_preperator The {@link Preperator} related to the prefix file.
	 * @param postfixed_preperator The {@link Preperator} related to the postfix file. 
	 */
	public void checkForMutationOperators(List<SourceCodeChange> changes,
			Preperator prefixed_preperator, Preperator postfixed_preperator) {
		// get the correct list of MutationOperator's to check
		ArrayList<MutationOperator> mutationOperatorList = getInitialMutationOperators(changes);
		// filter out all mutation operator which do not implement it
		filterPreCheck(mutationOperatorList, true);
		
		// perform the pre check on all remaining mutation operators
		preRunMutationOperators(mutationOperatorList, changes, prefixed_preperator, postfixed_preperator);
	}
	
	private void check(ASTNode node, Insert change) {
		// get the correct list of MutationOperator's to check
		ArrayList<MutationOperator> mutationOperatorList = getInitialMutationOperators(change);
		// filter the list of mutationOperators
		filterOneAST(mutationOperatorList, true);
		filterInsert(mutationOperatorList, true);
		
		
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {

			case COMMENT_INSERT:
				// since comment updates do not fix bugs, 
				// we cannot find possible matches to this case
				// and therefore return a "no matching found"
				this.listener.OnNoMatchingFound(mutationOperatorList);
				break;
			case ADDITIONAL_CLASS:
			case ADDITIONAL_FUNCTIONALITY:
			case ADDITIONAL_OBJECT_STATE:
			case ALTERNATIVE_PART_INSERT:
			case STATEMENT_INSERT:
			case UNCLASSIFIED_CHANGE:
				runMutationOperators(mutationOperatorList, node);
				break;
				
			default:
				// default case means some error
				String error_message = "Expected body insert, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, Insert)" , error_message);
				throw new IllegalStateException(error_message);
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case DOC_INSERT:
				// since comment updates do not fix bugs, we ignore this case
				// break;
			case ADDING_CLASS_DERIVABILITY:
			case ADDING_METHOD_OVERRIDABILITY:
			case PARAMETER_INSERT:
			case PARENT_CLASS_INSERT:
			case PARENT_INTERFACE_INSERT:
			case REMOVING_ATTRIBUTE_MODIFIABILITY:
			case RETURN_TYPE_INSERT:
				runMutationOperators(mutationOperatorList, node);
				break;
			default:
				String error_message = "Expected class insert, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, Insert)" , error_message);
				throw new IllegalStateException(error_message);
			}
		}
	}

	private void check(ASTNode node, Delete change) {
		// get the correct list of MutationOperator's to check
		ArrayList<MutationOperator> mutationOperatorList = getInitialMutationOperators(change);
		// filter the list of mutationOperators
		filterOneAST(mutationOperatorList, true);
		filterDelete(mutationOperatorList, true);
		
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {
			case COMMENT_DELETE:
				// since comment updates do not fix bugs, 
				// we cannot find possible matches to this case
				// and therefore return a "no matching found"
				this.listener.OnNoMatchingFound(mutationOperatorList);
				break;
			case ALTERNATIVE_PART_DELETE:
			case REMOVED_CLASS:
			case REMOVED_FUNCTIONALITY:
			case REMOVED_OBJECT_STATE:
			case STATEMENT_DELETE:
			case UNCLASSIFIED_CHANGE:
				runMutationOperators(mutationOperatorList, node);
				break;
			
			default:
				// default case means some error
				String error_message = "Expected body delete, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, Delete)" , error_message);
				throw new IllegalStateException(error_message);
						
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case ADDING_ATTRIBUTE_MODIFIABILITY:
			case DOC_DELETE:
			case PARAMETER_DELETE:
			case PARENT_CLASS_DELETE:
			case PARENT_INTERFACE_DELETE:
			case RETURN_TYPE_DELETE:
				runMutationOperators(mutationOperatorList, node);
				break;
			default:
				String error_message = "Expected class delete, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, Delete)" , error_message);
				throw new IllegalStateException(error_message);
			}
		}
	}

	private void check(ASTNode leftNode, ASTNode rightNode, Move change) {
		// get the correct list of MutationOperator's to check
		ArrayList<MutationOperator> mutationOperatorList = getInitialMutationOperators(change);
		// filter the list of mutationOperators
		filterTwoAST(mutationOperatorList, true);
		filterMove(mutationOperatorList, true);
		
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {

			case COMMENT_MOVE:
				// since comment updates do not fix bugs, 
				// we cannot find possible matches to this case
				// and therefore return a "no matching found"
				this.listener.OnNoMatchingFound(mutationOperatorList);
				break;
			case STATEMENT_ORDERING_CHANGE:
			case STATEMENT_PARENT_CHANGE:
			case UNCLASSIFIED_CHANGE:
				runMutationOperators(mutationOperatorList, leftNode, rightNode);
				break;
			default:
				String error_message = "Expected body move, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, ASTNode, Move)" , error_message);
				throw new IllegalStateException(error_message);
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case PARAMETER_ORDERING_CHANGE:
				runMutationOperators(mutationOperatorList, leftNode, rightNode);
				break;
			default:
				String error_message = "Expected class move, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, ASTNode, Move)" , error_message);
				throw new IllegalStateException(error_message);
			}
		}
	}

	private void check(ASTNode leftNode, ASTNode rightNode, Update change) {
		// get the correct list of MutationOperator's to check
		ArrayList<MutationOperator> mutationOperatorList = getInitialMutationOperators(change);
		// filter the list of mutationOperators
		filterTwoAST(mutationOperatorList, true);
		filterUpdate(mutationOperatorList, true);
		
		// check the location of update
		if (change.getChangeType().isBodyChange()) {

			// in case of a body change
			switch (change.getChangeType()) {

			case COMMENT_UPDATE:
				// since comment updates do not fix bugs, 
				// we cannot find possible matches to this case
				// and therefore return a "no matching found"
				this.listener.OnNoMatchingFound(mutationOperatorList);
				break;
			
			case CONDITION_EXPRESSION_CHANGE:
				// check only the condition of the node
				// TODO check why arg.equals("-d") ~> arg.equals("-encoding") does not match ACO
				if ((leftNode instanceof IfStatement)
						&& (rightNode instanceof IfStatement)) {
					runMutationOperators(mutationOperatorList,
							((IfStatement) leftNode).getExpression(),
							((IfStatement) rightNode).getExpression());
				} else if ((leftNode instanceof WhileStatement)
						&& (rightNode instanceof WhileStatement)) {
					runMutationOperators(mutationOperatorList,
							((WhileStatement) leftNode).getExpression(),
							((WhileStatement) rightNode).getExpression());
				} else if ((leftNode instanceof DoStatement)
						&& (rightNode instanceof DoStatement)) {
					runMutationOperators(mutationOperatorList,
							((DoStatement) leftNode).getExpression(),
							((DoStatement) rightNode).getExpression());
				// TODO check why j.hasNext() ~> it.hasNext() does not match VRO
				} else if ((leftNode instanceof ForStatement)
						&& (rightNode instanceof ForStatement)) {
					runMutationOperators(mutationOperatorList,
							((ForStatement) leftNode).getExpression(),
							((ForStatement) rightNode).getExpression());
				} else {
					String error_message = "Could not find ASTNode with same nodetype on changetype CONDITION_EXPRESSION_CHANGE: " + leftNode.getClass().getName() + " & " + rightNode.getClass().getName();
					this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, ASTNode, Update)" , error_message);
					throw new IllegalStateException(error_message);
				}
				break;
			case STATEMENT_UPDATE:
			case UNCLASSIFIED_CHANGE:
				// in this case, we cannot specify the area to search
				runMutationOperators(mutationOperatorList, leftNode, rightNode);
				break;

			default:
				String error_message = "Expected body update, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, ASTNode, Update)" , error_message);
				throw new IllegalStateException(error_message);
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case DOC_UPDATE:
				// since comment updates do not fix bugs, we ignore this case
				// break;

			case ATTRIBUTE_RENAMING:
			case ATTRIBUTE_TYPE_CHANGE:

			case CLASS_RENAMING:

			case DECREASING_ACCESSIBILITY_CHANGE:

			case INCREASING_ACCESSIBILITY_CHANGE:

			case METHOD_RENAMING:

			case PARAMETER_RENAMING:
			case PARAMETER_TYPE_CHANGE:

			case PARENT_CLASS_CHANGE:
			case PARENT_INTERFACE_CHANGE:

			case REMOVING_CLASS_DERIVABILITY:
			case REMOVING_METHOD_OVERRIDABILITY:

			case RETURN_TYPE_CHANGE:

			case UNCLASSIFIED_CHANGE:
				runMutationOperators(mutationOperatorList, leftNode, rightNode);
				break;
			default:
				String error_message = "Expected class update, but found " + change.getChangeType().toString();
				this.listener.OnErrorDetected(this.getClass().getSimpleName() + " - check(ASTNode, Update)" , error_message);
				throw new IllegalStateException(error_message);
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
	 * @param prefix
	 *            The prefixed version.
	 * @param postfix
	 *            The postfixed version.
	 */
	private void runMutationOperators(List<MutationOperator> operatorlist, ASTNode prefix, ASTNode postfix) {
		// initialize a variable which counts the number of detected
		// applications
		int detected_applications = 0;
		// check all mutation operators
		for (MutationOperator operator : operatorlist) {
			try {
				detected_applications += operator.check(prefix, postfix);
			} catch (Exception e) {
				String errorMessage = 
					"Exception thrown while checking mutation operator " + operator.getShortname() + ": " 
					+ e.toString();
				this.listener.OnErrorDetected("MutationOperatorTester - runMutationOperators(List<MutationOperator>, ASTNode, ASTNode)", errorMessage);
				continue;
			}
		}
		// fire event when there was no matching detected
		if (detected_applications == 0) {
			this.listener.OnNoMatchingFound(operatorlist);
		}
	}
	
	/**
	 * Helper method. Check for each MutationOperator in {@code operatorlist}, 
	 * if one of the {@ink MutationOperator} in {@code operatorlist} could be applied to {@code node}.
	 * @param operatorlist
	 * 		A {@link List} of {@link MutationOperator} containing all
	 *      MutationOperators to check in this case.
	 * @param node
	 * 		The version to check
	 */
	private void runMutationOperators(List<MutationOperator> operatorlist, ASTNode node) {
		// initialize a variable which counts the number of detected
		// applications
		int detected_applications = 0;
		// check all mutation operators
		for (MutationOperator operator : operatorlist) {
			try {
				detected_applications += operator.check(node);
			} catch (Exception e) {
				String errorMessage = 
						"Exception thrown while checking mutation operator " + operator.getShortname() + ": " 
						+ e.toString();
				this.listener.OnErrorDetected("MutationOperatorTester - runMutationOperators(List<MutationOperator>, ASTNode)", errorMessage);
				continue;
			}
		}
		// fire event when there was no matching detected
		if (detected_applications == 0) {
			this.listener.OnNoMatchingFound(operatorlist);
		}
	}
	
	private void preRunMutationOperators(
			ArrayList<MutationOperator> operatorlist,
			List<SourceCodeChange> changes, Preperator prefixed_preperator,
			Preperator postfixed_preperator) {
		// initialize a variable which counts the number of detected
		// applications
		int detected_applications = 0;
		// check all mutation operators
		for (MutationOperator operator : operatorlist) {
			try {
				operator.check(changes, prefixed_preperator, postfixed_preperator);
			} catch (Exception e) {
				String errorMessage = 
						"Exception thrown while checking mutation operator " + operator.getShortname() + ": " + "\n" 
						+ e.toString();
				this.listener.OnErrorDetected("MutationOperatorTester - preRunMutationOperators(ArrayList<MutationOperator>, List<SourceCodeChange>, Preperator, Preperator)", errorMessage);
				continue;
			}
		}
		// fire event when there was no matching detected
		if (detected_applications == 0) {
			this.listener.OnNoMatchingFound(operatorlist);
		}
	}	
	
	private ArrayList<MutationOperator> getInitialMutationOperators(SourceCodeChange change) {
		ArrayList<MutationOperator> mutationOperatorList;
		// get level based operators
		if (change.getChangeType().isBodyChange()) {
			mutationOperatorList = getCopyOfList(methodlevel_list);
		} else {
			mutationOperatorList = getCopyOfList(classlevel_list);
		}
		// at both leveled operators to it
		mutationOperatorList.addAll(bothlevel_list);
		return mutationOperatorList;
	}
	
	private ArrayList<MutationOperator> getInitialMutationOperators(List<SourceCodeChange> changes) {
		ArrayList<MutationOperator> mutationOperatorList = new ArrayList<MutationOperator>();
		boolean isMethod = false;
		boolean isClass = false;
		for(SourceCodeChange change: changes) {
			if(change.getChangeType().isBodyChange() && (!isMethod)){
				isMethod = true;
				mutationOperatorList.addAll(getCopyOfList(methodlevel_list));
			}
			else if(!change.getChangeType().isBodyChange() && (!isClass)){
				isClass = true;
				mutationOperatorList.addAll(getCopyOfList(classlevel_list));
			}
			if(isMethod && isClass) {
				break;
			}
		}
		// at both leveled operators to it
		mutationOperatorList.addAll(bothlevel_list);
		return mutationOperatorList;
	}

	private ArrayList<MutationOperator> getCopyOfList(
			ArrayList<MutationOperator> list) {
		return (ArrayList<MutationOperator>) list.clone();
	}
	
	/**
	 * Removes all {@link MutationOperator} from {@code list} where the move property is equal to {@code moveValue}.
	 * @param list The list of {@link MutationOperator} to filter.
	 * @param moveValue The expected value of {@link MutationOperatorProperty} related to the move field.
	 */
	private void filterMove(ArrayList<MutationOperator> list, boolean moveValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canMove() != moveValue){
				list.remove(mutop);
			}
		}
	}
	
	private void filterInsert(ArrayList<MutationOperator> list, boolean insertValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canInsert() != insertValue){
				list.remove(mutop);
			}
		}
	}
	
	private void filterDelete(ArrayList<MutationOperator> list, boolean deleteValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canDelete() != deleteValue){
				list.remove(mutop);
			}
		}
	}
	
	private void filterUpdate(ArrayList<MutationOperator> list, boolean updateValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canUpdate() != updateValue){
				list.remove(mutop);
			}
		}
	}
	
	/**
	 * Removes all {@link MutationOperator} from {@code list} where the canOneAST property is not equal to {@code oneASTValue}.
	 * @param list The list of {@link MutationOperator} to filter.
	 * @param moveValue The expected value of {@link MutationOperatorProperty} related to the canOneAST field.
	 */
	private void filterOneAST(ArrayList<MutationOperator> list, boolean oneASTValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canHandleOneAST() != oneASTValue){
				list.remove(mutop);
			}
		}
	}
	
	/**
	 * Removes all {@link MutationOperator} from {@code list} where the canTwoAST property is not equal to {@code oneTwoValue}.
	 * @param list The list of {@link MutationOperator} to filter.
	 * @param moveValue The expected value of {@link MutationOperatorProperty} related to the oneTwoValue field.
	 */
	private void filterTwoAST(ArrayList<MutationOperator> list, boolean oneTwoValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canHandleTwoASTs() != oneTwoValue){
				list.remove(mutop);
			}
		}
	}

	/**
	 * Removes all {@link MutationOperator} from {@code list} where the preCheck property is not equal to {@code precheckValue}.
	 * @param list The list of {@link MutationOperator} to filter.
	 * @param moveValue The expected value of {@link MutationOperatorProperty} related to the precheckValue field.
	 */
	private void filterPreCheck(ArrayList<MutationOperator> list, boolean precheckValue){
		ArrayList<MutationOperator> copy = (ArrayList<MutationOperator>) list.clone();
		for(MutationOperator mutop: copy){
			if(mutop.getProperty().canHandleMultipleChanges() != precheckValue){
				list.remove(mutop);
			}
		}
	}
}
