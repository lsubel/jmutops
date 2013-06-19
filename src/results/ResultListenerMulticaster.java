package results;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Casts method calls to all registered {@link ResultListener}.
 * 
 * @author Lukas Subel
 *
 */
public class ResultListenerMulticaster implements ResultListener {
	
	/**
	 * ArrayList containing all registered {@link ResultListener}.
	 */
	private ArrayList<ResultListener> listener = new ArrayList<ResultListener>();
	
	public void add(ResultListener rl){
		if(!(listener.contains(rl))){
			listener.add(rl);
		}
	}
	
	public void remove(ResultListener rl){
		listener.remove(rl);
	}
	
	@Override
	public void OnBugChanged(int officalID) {
		for(ResultListener rl: this.listener){
			rl.OnBugChanged(officalID);
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
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
		for(ResultListener rl: this.listener){
			rl.OnFileCheckStarted(prefixedFile, postfixedFile);
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
	
	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		for(ResultListener rl: this.listener){
			rl.OnErrorDetected(location, errorMessage);
		}
	}

	@Override
	public void OnFileCheckFinished() {
		for(ResultListener rl: this.listener){
			rl.OnFileCheckFinished();
		}
	}

	
}
