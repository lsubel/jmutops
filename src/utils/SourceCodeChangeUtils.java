package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.NodeFinder;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

public class SourceCodeChangeUtils {
	
	public static List<SourceCodeChange> summarizeChanges(List<SourceCodeChange> changes, Preperator preperator, Preperator preperator2) {
		// initialize return list		
		ArrayList<SourceCodeChange> returnList = new ArrayList<SourceCodeChange>();
		
		while(!changes.isEmpty()) {
			// removes the first element of the ArrayList
			SourceCodeChange change = changes.remove(0);
			
			
			if(change instanceof Insert){
				for(SourceCodeChange change2: changes) {
					if((change2 instanceof Delete) && (change.getChangeType().isBodyChange() == change2.getChangeType().isBodyChange())) {
						// Insert / Delete ~> Update
						boolean sameLocation = checkLocationInASTS((Delete) change2, (Insert) change, preperator, preperator2);
						if (sameLocation){
							ChangeType new_change_type = getNewChangeType(change.getChangeType(), change2.getChangeType());
							change = new Update(new_change_type, change.getRootEntity(), change2.getChangedEntity(), change.getChangedEntity(), change.getParentEntity());
							changes.remove(change2);
							break;
						}
					}
				}
			}
			else if(change instanceof Delete) {
				for(SourceCodeChange change2: changes) {
					if((change2 instanceof Insert) && (change.getChangeType().isBodyChange() == change2.getChangeType().isBodyChange())) {
						// Insert / Delete ~> Update
						boolean sameLocation = checkLocationInASTS((Delete) change, (Insert) change2, preperator2, preperator);
						if (sameLocation){
							ChangeType new_change_type = getNewChangeType(change.getChangeType(), change2.getChangeType());
							change = new Update(new_change_type, change.getRootEntity(), change.getChangedEntity(), change2.getChangedEntity(), change.getParentEntity());
							changes.remove(change2);
							break;
						}
					}
				}				
			}
			else if(change instanceof Update) {
				// Update of super() ~> Delete
				boolean removeSuper 			= Pattern.matches("super(.+);", ((Update) change).getChangedEntity().getUniqueName()); 
				boolean insertDefaultSuper 		= (((Update) change).getNewEntity().getUniqueName().equals("super();"));
				String substring 				= preperator2.getFileContent().substring(((Update) change).getNewEntity().getStartPosition(), ((Update) change).getNewEntity().getEndPosition());
				boolean differentValueInRange 	= !(substring.equals("super();"));
				if(insertDefaultSuper && differentValueInRange && removeSuper) {
					change = new Delete(ChangeType.STATEMENT_DELETE ,((Update) change).getRootEntity(), ((Update) change).getChangedEntity(), ((Update) change).getParentEntity());
				}
			}
			
			// write the result back to the new list
			returnList.add(change);
		}
		
		// return list
		return returnList;
	}
	
	private static ChangeType getNewChangeType(ChangeType changeType,
			ChangeType changeType2) {

		// both ChangeType's are the same
		if(changeType == changeType2) {
			return changeType;
		}
		
		// Statement Insert ^ Statement Delete -> Statement Update
		if(
			((changeType == ChangeType.STATEMENT_INSERT) && (changeType2 == ChangeType.STATEMENT_DELETE))
			|| ((changeType == ChangeType.STATEMENT_DELETE) && (changeType2 == ChangeType.STATEMENT_INSERT))
		) {
			return ChangeType.STATEMENT_UPDATE;
		}

		// default case
		return ChangeType.UNCLASSIFIED_CHANGE;
	}

	private static boolean checkLocationInASTS(Delete change_delete, Insert change_insert, Preperator preperator_delete, Preperator preperator_insert){
		
		// first check if both changes are occurring in method body; otherwise abort the process
		boolean bothMethodBody = (change_delete.getChangeType().isBodyChange() && change_insert.getChangeType().isBodyChange());
		if(!bothMethodBody) {
			return false;
		}
		
		// first check if these changes are related to the same method; otherwise abort the process
		boolean sameMethod = (change_delete.getRootEntity().equals(change_insert.getRootEntity()));
		if(!sameMethod) {
			return false;
		}
		
		// get the subASTs related to the changes
		int sce_start_delete 	= change_delete.getChangedEntity().getStartPosition();
		int sce_end_delete 		= change_delete.getChangedEntity().getEndPosition();
		NodeFinder nf_delete 	= new NodeFinder(preperator_delete.getAST(), sce_start_delete, sce_end_delete - sce_start_delete + 1);
		
		int sce_start_insert 	= change_insert.getChangedEntity().getStartPosition();
		int sce_end_insert 		= change_insert.getChangedEntity().getEndPosition();
		NodeFinder nf_insert 	= new NodeFinder(preperator_insert.getAST(), sce_start_insert, sce_end_insert - sce_start_insert + 1);
		
		// check if both ASTNodes have the same type
		boolean sameASTNodeType = (nf_delete.getCoveringNode().getNodeType() == nf_insert.getCoveringNode().getNodeType());
		if(!sameASTNodeType) {
			return false;
		}
		
		// calculate the way from the statement to the method declaration
		Stack<Integer> way_insert = PathExtractor.calculatePathToMethod(nf_insert.getCoveringNode());
		Stack<Integer> way_delete = PathExtractor.calculatePathToMethod(nf_delete.getCoveringNode());
		
		// finally compare these stacks and return the result
		return way_insert.equals(way_delete);
	}
	
	
	public static int[] getNodeFinderInput(Insert change) {
		int[] result = new int[2];
		// extract the ranges in the document
		int startPos 	= change.getChangedEntity().getStartPosition();
		int endPos	 	= change.getChangedEntity().getEndPosition();

		result[0] = startPos;
		result[1] = endPos	 - startPos + 1;
		
		return result;
	}
	
	public static int[] getNodeFinderInput(Delete change) {
		int[] result = new int[2];
		// extract the ranges in the document
		int startPos 	= change.getChangedEntity().getStartPosition();
		int endPos	 	= change.getChangedEntity().getEndPosition();

		result[0] = startPos;
		result[1] = endPos - startPos + 1;
		
		return result;
	}
	
	public static int[] getNodeFinderInput(Update change) {
		int[] result = new int[4];
		// extract the ranges in the document
		int old_start 	= change.getChangedEntity().getStartPosition();
		int old_end 	= change.getChangedEntity().getEndPosition();
		int new_start 	= change.getNewEntity().getStartPosition();
		int new_end 	= change.getNewEntity().getEndPosition();
		
		result[0] = old_start;
		result[1] = old_end - old_start + 1;
		result[2] = new_start;
		result[3] = new_end - new_start + 1;
		return result;
	}
	
	
	public static int[] getNodeFinderInput(Move change) {
		int[] result = new int[4];
		// extract the ranges in the document
		int old_start 	= change.getChangedEntity().getStartPosition();
		int old_end 	= change.getChangedEntity().getEndPosition();
		int new_start 	= change.getNewEntity().getStartPosition();
		int new_end 	= change.getNewEntity().getEndPosition();
		
		result[0] = old_start;
		result[1] = old_end - old_start + 1;
		result[2] = new_start;
		result[3] = new_end - new_start + 1;
		return result;
	}
}


/*
private List<SourceCodeChange> summarizeChanges(
		List<SourceCodeChange> changes) {
	List<SourceCodeChange> newList = new ArrayList<SourceCodeChange>();

	while(changes.size() != 0){
		
		SourceCodeChange change = changes.get(0);
		
		if(change instanceof Insert){
			Insert castedChange = (Insert) change;
			changes.remove(change);
			
			for(SourceCodeChange change2: changes){
				if(change2 instanceof Delete){
					Delete castedChange2 = (Delete) change2;
					
					boolean sameRange = 
							(castedChange.getChangedEntity().getStartPosition() == castedChange2.getChangedEntity().getStartPosition())
							&& (castedChange.getChangedEntity().getEndPosition() == castedChange2.getChangedEntity().getEndPosition());
					boolean sameParentEntity = castedChange.getParentEntity() == castedChange2.getParentEntity();
					boolean sameRootEntity = castedChange.getRootEntity() == castedChange2.getRootEntity();
					if(sameRange && sameParentEntity && sameRootEntity){
						Update newUpdate = new Update(castedChange.getRootEntity(), castedChange2.getChangedEntity(), castedChange.getChangedEntity(), castedChange.getParentEntity());
						changes.remove(change);
						changes.remove(change2);
						castedChange = null;
						newList.add(newUpdate);
						break;
					}
				}
			}
			
			if(castedChange != null){
				newList.add(castedChange);
			}
		}
		
		else if(change instanceof Delete){
			Delete castedChange = (Delete) change;
			changes.remove(change);
			
			for(SourceCodeChange change2: changes){
				if(change2 instanceof Insert){
					Insert castedChange2 = (Insert) change2;
					
					boolean sameRange = 
							(castedChange.getChangedEntity().getStartPosition() == castedChange2.getChangedEntity().getStartPosition())
							&& (castedChange.getChangedEntity().getEndPosition() == castedChange2.getChangedEntity().getEndPosition());
					boolean sameParentEntity = castedChange.getParentEntity() == castedChange2.getParentEntity();
					boolean sameRootEntity = castedChange.getRootEntity() == castedChange2.getRootEntity();
					if(sameRange && sameParentEntity && sameRootEntity){
						Update newUpdate = new Update(castedChange.getRootEntity(), castedChange.getChangedEntity(), castedChange2.getChangedEntity(), castedChange.getParentEntity());
						changes.remove(change2);
						castedChange = null;
						newList.add(newUpdate);
						break;
					}
				}
			}
			
			if(castedChange != null){
				newList.add(castedChange);
			}
		}
		else{
			newList.add(change);
			changes.remove(change);
		}
		
	}
	return newList;
}
*/
