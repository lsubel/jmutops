package results;

import java.util.ArrayList;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

public class ResultListenerMulticaster extends ResultListener {

	protected ArrayList<ResultListener> listener = new ArrayList<ResultListener>();
	
	public void add(ResultListener rl){
		if(!(listener.contains(rl))){
			listener.add(rl);
		}
	}
	
	public void remove(ResultListener rl){
		listener.remove(rl);
	}
	
	@Override
	public void OnBugChanged() {
		for(ResultListener rl: this.listener){
			rl.OnBugChanged();
		}
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		for(ResultListener rl: this.listener){
			rl.OnMatchingFound(operator, prefix, postfix);
		}
	}
	
	@Override
	public void OnNewFileStarted() {
		for(ResultListener rl: this.listener){
			rl.OnNewFileStarted();
		}
	}
	
	@Override
	public void OnProgramChanged(String newProgramName) {
		for(ResultListener rl: this.listener){
			rl.OnProgramChanged(newProgramName);
		}
	}
	
	@Override
	public void OnCreatingResult() {
		for(ResultListener rl: this.listener){
			rl.OnCreatingResult();
		}
	}

	
}
