package mutationoperators.sco;

import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;

import results.JMutOpsEventListenerMulticaster;
import enums.MutationOperatorCategory;
import enums.MutationOperatorLevel;

public class SCO extends MutationOperator {

	public SCO() {
		this(null);
	}
	
	public SCO(JMutOpsEventListenerMulticaster eventListener) {
		super(eventListener);
		this.twoAST_matcher = new SCO_Matcher(this);
		this.twoAST_visitor = null;
	}

	@Override
	protected void setProperties() {
		this.mutopproperty.setShortname("SCO");
		this.mutopproperty.setFullname("Scope change operator");
		this.mutopproperty.setDescription("Moves the location of local variable declaration to outer/inner blocks.");
		this.mutopproperty.setLevel(MutationOperatorLevel.METHOD_LEVEL);
		this.mutopproperty.setCategory(MutationOperatorCategory.METHOD_LEVEL);
		this.mutopproperty.setCanTwoAST();
		this.mutopproperty.setMove();
	}
	
	@Override
	public int check(ASTNode leftNode, ASTNode rightNode) {
		// reset application counter
		this.application_counter = 0;
		ASTNode leftParent = leftNode.getParent();
		ASTNode rightParent = rightNode.getParent();
		// since we only have to compare the parent nodes,
		// we directly call the matcher with the parent nodes
		if(leftParent instanceof Block){
			Block leftBlock = (Block) leftParent; 
			this.twoAST_matcher.match(leftBlock, rightParent);
		}
		else if (rightParent instanceof Block){
			Block rightBlock = (Block) rightParent; 
			this.twoAST_matcher.match(rightBlock, leftParent);
		}
		// return the number of detected matches
		return this.application_counter;		
	}
}
