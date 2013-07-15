package results;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

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
	public void OnBugChanged(int officalID, String urlToBugreport) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnBugChanged(officalID, urlToBugreport);
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
	public void OnProgramChanged(String newProgramName, String programDescription, String urlToProjectPage, String urlToBugtracker) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnProgramChanged(newProgramName, programDescription, urlToProjectPage, urlToBugtracker);
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

	@Override
	public void OnChangeChecked(SourceCodeChange change) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnChangeChecked(change);
		}
	}

	@Override
	public void OnMutationOperatorInit(MutationOperator mutop) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnMutationOperatorInit(mutop);
		}
	}

	@Override
	public void OnNoMatchingFound(List<MutationOperator> operatorlist) {
		for(JMutOpsEventListener rl: this.listener){
			rl.OnNoMatchingFound(operatorlist);
		}
	}	
}
