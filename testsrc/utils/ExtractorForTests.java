package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListener;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ExtractorForTests implements JMutOpsEventListener {

	public ArrayList<MutationOperator> mutationOperatorList = new ArrayList<MutationOperator>();
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
	}

	@Override
	public void OnProgramChanged(String newProgramName,
			String programDescription, String urlToProjectPage,
			String urlToBugtracker) {
	}

	@Override
	public void OnBugChanged(String bugID, String urlToBugreport) {
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
	}

	@Override
	public void OnFileCheckFinished() {
	}

	@Override
	public void OnCreatingResult() {
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
	}

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
	}

	@Override
	public void OnMutationOperatorInit(MutationOperator mutop) {
		mutationOperatorList.add(mutop);
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
	}

	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode node) {
	}

}
