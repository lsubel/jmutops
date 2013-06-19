package results;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Casts method calls to all registered {@link JMutOpsEventListener}.
 * 
 * @author Lukas Subel
 *
 */
public class JMutOpsEventListenerMulticaster implements JMutOpsEventListener {
	
	/**
	 * ArrayList containing all registered {@link JMutOpsEventListener}.
	 */
	private ArrayList<JMutOpsEventListener> listener = new ArrayList<JMutOpsEventListener>();
	
	public void add(JMutOpsEventListener rl){
		if(!(listener.contains(rl))){
			listener.add(rl);
		}
	}
	
	public void remove(JMutOpsEventListener rl){
		listener.remove(rl);
	}
	
	@Override
	public void OnBugChanged(int officalID) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnBugChanged(officalID);
		}
	}
	
	@Override
	public void OnMatchingFound(MutationOperator operator, ASTNode prefix,
			ASTNode postfix) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnMatchingFound(operator, prefix, postfix);
		}
	}
	
	@Override
	public void OnFileCheckStarted(File prefixedFile, File postfixedFile) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnFileCheckStarted(prefixedFile, postfixedFile);
		}
	}
	
	@Override
	public void OnProgramChanged(String newProgramName) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnProgramChanged(newProgramName);
		}
	}
	
	@Override
	public void OnCreatingResult() {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnCreatingResult();
		}
	}
	
	@Override
	public void OnErrorDetected(String location, String errorMessage) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnErrorDetected(location, errorMessage);
		}
	}

	@Override
	public void OnFileCheckFinished() {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnFileCheckFinished();
		}
	}

	
}
