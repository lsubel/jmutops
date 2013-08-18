package results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import mutationoperators.MutationOperator;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

/**
 * Class which logs all occurred events in chronological order. <p>
 * This class is able to print out the results on console and/or store it in a file. <p>
 * @author Lukas Subel
 *
 */
public class EventLogger implements JMutOpsEventListener {

	private StringBuffer logger = new StringBuffer();
	
	private final boolean shouldOutputFile;
	
	private final boolean shouldOutputConsole;
	
	private final File output_path;
	
	/**
	 * Default constructor. By default, the log is printed out on the console.
	 */
	public EventLogger() {
		this.shouldOutputConsole 	= true;
		this.shouldOutputFile 		= false;
		this.output_path 			= null;
	}
	
	/**
	 * Explicit constructor to set up how the results should be made available.
	 * @param console True iff the results should be print out on console.
	 * @param file True iff the results should be stored in a file.
	 */
	public EventLogger(boolean console, boolean file, File path) {
		this.shouldOutputConsole 	= console;
		this.shouldOutputFile 		= file;
		this.output_path 			= path;
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		Date now = new Date();
		logger.append(now.toString() + " - Matching was detected:"  + "\n");
		logger.append("\t" + "Mutation Operator: " + "\n"); 
		logger.append("\t" + "\t" + operator.getShortname() +  "\n");
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
		logger.append("\t" + "Mutation Operator: " + operator.getShortname() +  "\n");
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
		logger.append(now.toString() + " - Initialized a new bug with ID " + bugID + "." + "\n");
		logger.append("\n");
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
		Date now = new Date();
		logger.append(now.toString() + " - Started to compare two files:" + "\n");
		logger.append("\t" + prefixedFile.getAbsolutePath()  + "\n");
		try {
			List<String> list = FileUtils.readLines(prefixedFile);
			for(String s: list){
				logger.append("\t" + "\t" + s + "\n");	
			}
		} catch (IOException e) {
			logger.append("\t" + "Could not read content!" + "\n" );
		}
		logger.append("\t" + postfixedFile.getAbsolutePath()  + "\n");
		try {
			List<String> list = FileUtils.readLines(postfixedFile);
			for(String s: list){
				logger.append("\t" + "\t" + s + "\n");	
			}
		} catch (IOException e) {
			logger.append("\t" + "Could not read content!" + "\n");
		}
		logger.append("\n");
	}

	@Override
	public void OnFileCheckFinished() {
		Date now = new Date();
		logger.append(now.toString() + " - Finished to compare the two files." + "\n");
		logger.append("\n");
	}

	@Override
	public void OnCreatingResult() {
		// check if they should output the result on console
		if(this.shouldOutputConsole) {
			System.out.println(logger.toString());
		}
		// check if they should output the result in a file
		if(this.shouldOutputFile) {
			File resultingFile = null;
			BufferedWriter bw = null;
			try {
				resultingFile = new File(this.output_path.getAbsolutePath() + File.pathSeparator + "log.txt");
				resultingFile.createNewFile();
				bw = new BufferedWriter(new FileWriter(resultingFile));
			} catch (IOException e) {
				System.out.println("Eventlogger - Could not create.");
				System.exit(0);
			}
			try {
				bw.write(logger.toString());
				bw.close();
			} catch (IOException e) {
				System.out.println("Eventlogger - Could not write all results into file.");
			}
		}
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
		logger.append("\t" + "Category: " + mutop.getCategory().toString()+ "\n");
		logger.append("\n");
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
		Date now = new Date();
		logger.append(now.toString() + " - No matching mutation operator found." + "\n");
		logger.append("\n");
		
	}
}
