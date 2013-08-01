package utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import results.JMutOpsEventListener;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * ActionListener which counts the number of detected applications of mutation operators.
 * @author Lukas Subel
 *
 */
public class ApplicationCounter implements JMutOpsEventListener {

	/**
	 * A map which stores for every mutation operator shortcut the number of detected applications.
	 */
	private HashMap<String, Integer> counter;
	
	/**
	 * Default constructor.
	 */
	public ApplicationCounter() {
		this.counter = new HashMap<String, Integer>();
	}
	
	@Override
	public void OnMutationOperatorInit(MutationOperator mutop) {
		assert !(this.counter.containsKey(mutop.getShortname()));
		// initialize a new entry in the map for the corresponding MutationOperator
		this.counter.put(mutop.getShortname(), 0);
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		String operatorName = operator.getShortname();
		if(this.counter.containsKey(operatorName)){
			Integer oldValue = this.counter.remove(operatorName);
			this.counter.put(operatorName, oldValue + 1);
		}
		else{
			throw new IllegalStateException("Mutation operator " + operatorName + " is not registered at " + this.getClass().getSimpleName() +  ".");
		}
	}

	@Override
	public void OnCreatingResult() {
		System.out.println("Applications found:" + "\n");
		for(String operatorName: this.counter.keySet()){
			int value = this.counter.get(operatorName);
			System.out.println(operatorName + ": " + value);
		}
	}
	
	public Integer getCount(String operator){
		if(this.counter.containsKey(operator)){
			return this.counter.get(operator);
		} else{
			return 0;
		}
	}

	@Override
	public void OnBugChanged(String officalID, String urlToBugreport) {
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		System.out.println("Error detected at " + location + ": " + errorMessage);
	}

	@Override
	public void OnFileCheckFinished() {
	}

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
	}

	@Override
	public void OnProgramChanged(String newProgramName, String programDescription, String urlToProjectPage, String urlToBugtracker) {
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
	}

}
