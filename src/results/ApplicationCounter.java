package results;

import java.io.File;
import java.util.HashMap;

import mutationoperators.MutationOperator;
import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ApplicationCounter implements JMutOpsEventListener {

	private HashMap<String, Integer> counter;
	
	public ApplicationCounter() {
		this.counter = new HashMap<String, Integer>();
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		String operatorName = operator.getClass().getSimpleName();
		if(this.counter.containsKey(operatorName)){
			Integer oldValue = this.counter.remove(operatorName);
			this.counter.put(operatorName, oldValue + 1);
		}
		else{
			this.counter.put(operatorName, 1);
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
	public void OnProgramChanged(String newProgramName) {	
	}

	@Override
	public void OnBugChanged(int officalID) {
	}

	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
	}

	@Override
	public void OnErrorDetected(String location, String errorMessage) {
	}

	@Override
	public void OnFileCheckFinished() {
	}

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
		// TODO Auto-generated method stub
		
	}

}
