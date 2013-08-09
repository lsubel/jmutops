package results;

import java.io.File;
import java.util.Date;
import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

public class EventLogger implements JMutOpsEventListener {

	StringBuffer logger = new StringBuffer();
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		Date now = new Date();
		logger.append(now.toString() + " - Matching was detected:"  + "\n");
		logger.append("\t" + "Prefix version: " + "\n");
		logger.append("\t\t" + "Content: " + prefix.toString()  + "\n");
		logger.append("\t\t" + "Node type: " + prefix.getClass().toString() + "\n");
		logger.append("\t\t" + "Range: " + prefix.getStartPosition() + "-" + (prefix.getStartPosition() + prefix.getLength() - 1) + "\n");
		logger.append("\t" + "Postfix version: " + "\n");
		logger.append("\t\t" + "Content: " + postfix.toString() + "\n");
		logger.append("\t\t" + "Node type: " + postfix.getClass().toString() + "\n");
		logger.append("\t\t" + "Range: " + postfix.getStartPosition() + "-" + (postfix.getStartPosition() + postfix.getLength() - 1) + "\n");
		logger.append("\n");
	}

	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode node) {
		Date now = new Date();
		logger.append(now.toString() + " - Matching was detected:"  + "\n");
		logger.append("\t" + "Version: " + "\n");
		logger.append("\t\t" + "Content: " + node.toString()  + "\n");
		logger.append("\t\t" + "Node type: " + node.getClass().toString() + "\n");
		logger.append("\t\t" + "Range: " + node.getStartPosition() + "-" + (node.getStartPosition() + node.getLength() - 1) + "\n");
		logger.append("\n");
	}
	
	@Override
	public void OnProgramChanged(String newProgramName, String programDescription, String urlToProjectPage, String urlToBugtracker) {
		Date now = new Date();
		logger.append(now.toString() + " - Initialized a new program with name " + newProgramName + ".\n");
		logger.append("\t" + "Program description: " + programDescription + "\n");
		logger.append("\t" + "URL to project page: " + urlToProjectPage + "\n");
		logger.append("\t" + "URL to bug tracker: "  + urlToBugtracker + "\n");
		logger.append("\n");
	}

	@Override
	public void OnBugChanged(String bugID, String urlToBugreport) {
		Date now = new Date();
		logger.append(now.toString() + " - Initialized a new bug with ID " + bugID + ".");
		logger.append("\n");
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
		Date now = new Date();
		logger.append(now.toString() + " - Started to compare two files:" + "\n");
		logger.append("\t" + prefixedFile.getAbsolutePath()  + "\n");
		logger.append("\t" + postfixedFile.getAbsolutePath()  + "\n");
		logger.append("\n");
	}

	@Override
	public void OnFileCheckFinished() {
		Date now = new Date();
		logger.append(now.toString() + " - Finished to compare the two files.");
		logger.append("\n");
	}

	@Override
	public void OnCreatingResult() {
		System.out.println(logger.toString());
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		Date now = new Date();
		logger.append(now.toString() + " - Error detected:" + "\n");
		logger.append("\t" + "Location: " + location + "\n");
		logger.append("\t" + "Message: " + errorMessage + "\n");
		logger.append("\n");
	}

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
		Date now = new Date();
		logger.append(now.toString() + " - Started to check a new change." +  "\n");
		logger.append("\t" + "Type of change: " + change.getChangeType().toString() + "\n");
		if(change instanceof Insert){
			Insert casted = (Insert) change;
			logger.append("\t" + "Parent entity: " + casted.getParentEntity().toString() + "\n");
			logger.append("\t" + "Node of changed entity: " + casted.getChangedEntity().toString() + "\n");
			logger.append("\t" + "Range of changed entity: " + casted.getChangedEntity().getSourceRange().toString() + "\n");
		}
		if(change instanceof Delete){
			Delete casted = (Delete) change;
			logger.append("\t" + "Parent entity: " + casted.getParentEntity().toString() + "\n");
			logger.append("\t" + "Node of changed entity: " + casted.getChangedEntity().toString() + "\n");
			logger.append("\t" + "Range of changed entity: " + casted.getChangedEntity().getSourceRange().toString() + "\n");
		}
		if(change instanceof Update){
			Update casted = (Update) change;
			logger.append("\t" + "Parent entity: " + casted.getParentEntity().toString() + "\n");
			logger.append("\t" + "Node of changed entity: " + casted.getChangedEntity().toString() + "\n");
			logger.append("\t" + "Range of changed entity: " + casted.getChangedEntity().getSourceRange().toString() + "\n");
			logger.append("\t" + "Node of new entity: " + casted.getNewEntity().toString() + "\n");
			logger.append("\t" + "Range of new entity: " + casted.getNewEntity().getSourceRange().toString() + "\n");
			
		}
		if(change instanceof Move){
			Move casted = (Move) change;
			logger.append("\t" + "Parent entity: " + casted.getParentEntity().toString() + "\n");
			logger.append("\t" + "Node of changed entity: " + casted.getChangedEntity().toString() + "\n");
			logger.append("\t" + "Range of changed entity: " + casted.getChangedEntity().getSourceRange().toString() + "\n");
			logger.append("\t" + "Node of new entity: " + casted.getNewEntity().toString() + "\n");
			logger.append("\t" + "Range of new entity: " + casted.getNewEntity().getSourceRange().toString() + "\n");
			
		}
		logger.append("\n");
	}

	@Override
	public void OnMutationOperatorInit(MutationOperator mutop) {
		Date now = new Date();
		logger.append(now.toString() + " - Mutation operator initialized:" + "\n");
		logger.append("\t" + "Short name: " + mutop.getShortname() + "\n");
		logger.append("\t" + "Full name: " + mutop.getFullname() + "\n");
		logger.append("\t" + "Category: " + mutop.getCategory().toString());
		logger.append("\n");
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
		Date now = new Date();
		logger.append(now.toString() + " - No matching mutation operator found." + "\n");
		logger.append("\n");
		
	}
}
