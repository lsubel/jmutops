package results;

import java.io.File;
import java.util.Date;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

public class OwnLogger implements ResultListener {

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
	public void OnProgramChanged(String newProgramName) {
		Date now = new Date();
		logger.append(now.toString() + " - Initialized a new program with name " + newProgramName + ".\n");
		logger.append("\n");
	}

	@Override
	public void OnBugChanged(int officialID) {
		Date now = new Date();
		logger.append(now.toString() + " - Initialized a new bug with ID " + officialID + ".");
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

}
