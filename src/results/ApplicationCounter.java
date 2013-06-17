package results;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import org.eclipse.jdt.core.dom.ASTNode;

public class ApplicationCounter extends ResultListener {

	private HashMap<String, Integer> counter;
	
	public ApplicationCounter() {
		this.counter = new HashMap<String, Integer>();
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		String operatorName = operator.getFullname();
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
		return this.counter.get(operator);
	}

}
