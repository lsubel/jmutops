package mutationoperators;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;

/**
 * Class storing the implemented MutationOperators, calling the correct
 * operators
 * 
 * @author Lukas Subel
 * 
 */
public class MutationOperatorChecker {

	ArrayList<MutationOperator> methodlevel_list;
	ArrayList<MutationOperator> classlevel_list;

	public MutationOperatorChecker() {
		this.methodlevel_list = new ArrayList<MutationOperator>();
		this.classlevel_list = new ArrayList<MutationOperator>();
	}

	public boolean addMutationOperator(MutationOperator mutop) {
		switch (mutop.getCategory()) {
		case CLASS_LEVEL:
			throw new UnsupportedOperationException(
					"This method has not yet been implemented.");
		case METHOD_LEVEL:
			if (!this.methodlevel_list.contains(mutop)) {
				this.methodlevel_list.add(mutop);
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
	}

	public void check(ASTNode leftNode, ASTNode rightNode, SourceCodeChange change) {	
		if(change instanceof Insert){
			this.check(leftNode,rightNode, (Insert) change);
		}
		else if(change instanceof Delete){
			this.check(leftNode,rightNode, (Delete) change);	
		}
		else if(change instanceof Update){
			this.check(leftNode,rightNode, (Update) change);	
		}
		else if(change instanceof Move){
			this.check(leftNode,rightNode, (Move) change);	
		}
		else{
			throw new IllegalStateException("Could not found correct subclass for change.");
		}
	}
		
	public void check(ASTNode leftNode, ASTNode rightNode, Insert change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {
			case ADDITIONAL_CLASS:
			case ADDITIONAL_FUNCTIONALITY:
			case ADDITIONAL_OBJECT_STATE:
			case ALTERNATIVE_PART_INSERT:
			case COMMENT_INSERT:
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
			case ADDING_ATTRIBUTE_MODIFIABILITY:
			case ADDING_CLASS_DERIVABILITY:
			case ADDING_METHOD_OVERRIDABILITY:
			case DOC_INSERT:
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

	public void check(ASTNode leftNode, ASTNode rightNode, Delete change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {
			case ALTERNATIVE_PART_DELETE:
			case COMMENT_DELETE:
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

	public void check(ASTNode leftNode, ASTNode rightNode, Move change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			// in case of a body change
			switch (change.getChangeType()) {
			
			// since comment updates do not fix bugs, we ignore this case
			case COMMENT_MOVE:
				break;

			case STATEMENT_ORDERING_CHANGE:
			case STATEMENT_PARENT_CHANGE:
			case UNCLASSIFIED_CHANGE:
			// default case means some error
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

	public void check(ASTNode leftNode, ASTNode rightNode, Update change) {
		// check the location of update
		if (change.getChangeType().isBodyChange()) {
			
			// in case of a body change
			switch (change.getChangeType()) {
			
			case COMMENT_UPDATE:
				// since comment updates do not fix bugs, we ignore this case
				break;
				
			case CONDITION_EXPRESSION_CHANGE:
				// check only the condition of the node
				if((leftNode instanceof IfStatement) && (rightNode instanceof IfStatement)){
					for(MutationOperator mutops: this.methodlevel_list){
						mutops.check( ((IfStatement) leftNode).getExpression(), ((IfStatement) rightNode).getExpression());
					}
				}
				else if((leftNode instanceof WhileStatement) && (rightNode instanceof WhileStatement)){
					for(MutationOperator mutops: this.methodlevel_list){
						mutops.check( ((WhileStatement) leftNode).getExpression(), ((WhileStatement) rightNode).getExpression());
					}
				}
				else if((leftNode instanceof DoStatement) && (rightNode instanceof DoStatement)){
					for(MutationOperator mutops: this.methodlevel_list){
						mutops.check( ((DoStatement) leftNode).getExpression(), ((DoStatement) rightNode).getExpression());
					}
				}
				break;
				
			case STATEMENT_UPDATE:
			case UNCLASSIFIED_CHANGE:				
				// in this case, we cannot specify the searchable area
				for(MutationOperator mutops: this.methodlevel_list){
					mutops.check(leftNode, rightNode);
				}
				break;
				
			default:
				throw new IllegalStateException(
						"Expected body update, but found "
								+ change.getChangeType().toString());
			}
		} else {
			// in case of a class change
			switch (change.getChangeType()) {
			case ATTRIBUTE_RENAMING:
			case ATTRIBUTE_TYPE_CHANGE:
			case CLASS_RENAMING:
			case DECREASING_ACCESSIBILITY_CHANGE:

			case DOC_UPDATE:
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

				break;
			default:
				throw new IllegalStateException(
						"Expected class change, but found "
								+ change.getChangeType().toString());
			}
		}

	}
}
