package mutationoperators.methodlevel.sco;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SCO_Matcher extends TwoASTMatcher {

	public SCO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(Block node, Object other) {
		if(other instanceof ASTNode){
			// we know that there is a match 
			// iff the depth of the nodes related to the MethodDeclerations differ
			int firstDepth = getDepth(node);
			int secondDepth = getDepth((ASTNode) other);
			boolean differentDepth = (firstDepth != secondDepth) && (firstDepth != -1) && (secondDepth != -1);
		
			if(differentDepth){
				this.mutop.found(node, (ASTNode) other);
			}
			return false;
		}
		return false;
	}
	
	
	private int getDepth(ASTNode node){
		// initialize variables
		int result = 0;
		ASTNode levelToCheck = node;
		// walk upwards until we hit a MethodDecleration
		while(levelToCheck != null){
			if(levelToCheck instanceof MethodDeclaration){
				return result;
			}
			else{
				result += 1;
				levelToCheck = levelToCheck.getParent();
			}
		}
		// if there is no more parent, we have to return a illegal value
		return -1; 
	}
	
	
}
