package mutationoperators.methodlevel.swo;

import java.util.List;

import mutationoperators.MutationOperator;
import results.JMutOpsEventListenerMulticaster;
import utils.Preperator;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class SWO extends MutationOperator {

	
	
	public SWO() {
		this(null);
	}
	
	public SWO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("SWO");
		this.mutopproperty.setFullname("Statements swap operator");
		this.mutopproperty.setDescription("Changes the order of statements (Case-block-statements, statements in if-then-else, expressions before & after condition operator).");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setPreCheck();
	}
	
	@Override
	public int preCheck(List<SourceCodeChange> changes,
			Preperator prefixed_preperator, Preperator postfixed_preperator) {
		// since this mutation operator may need multiple changes for one application, 
		// we have to implement a special case for him
		
		// first extract all nodes where such a reorder might occur
		// which are: 
		// * switch-case
		// * if-then-else
		// therefore, extract all these node with help of an ASTVisitor 
		
		
		
		
		
		// TODO Auto-generated method stub
		return super.preCheck(changes, prefixed_preperator, postfixed_preperator);
	}
}
