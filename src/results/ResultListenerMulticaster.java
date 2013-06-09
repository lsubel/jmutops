package results;

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
public class ResultListenerMulticaster extends ResultListener {

	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger(ResultListenerMulticaster.class.getName());
	
	/**
	 * ArrayList containing all registered {@link ResultListener}.
	 */
	private ArrayList<ResultListener> listener = new ArrayList<ResultListener>();
	
	public void add(ResultListener rl){
		if(!(listener.contains(rl))){
			listener.add(rl);
			ResultListenerMulticaster.logger.fine("ResultListener " + rl.getClass().getSimpleName() + " was added.");
		}
	}
	
	public void remove(ResultListener rl){
		listener.remove(rl);
		ResultListenerMulticaster.logger.fine("ResultListener " + rl.getClass().getSimpleName() + " was removed.");
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
	
	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		for(ResultListener rl: this.listener){
			rl.OnErrorDetected(location, errorMessage);
		}
	}

	
}
