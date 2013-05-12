package mutationoperators;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

public abstract class MutationOperator {

	public enum MutationOperatorCategory {
		METHOD_LEVEL("Method-level operator"), 
		CLASS_LEVEL("Class-level operator");
		
		private final String label;
		
		private MutationOperatorCategory(String label){
			this.label = label;
		}
		
		public String toString(){
			return this.label;
		}
	}
	
	protected MutationOperatorCategory category;
	
	
	/////////////////////////////////////////////////
	///	methods
	/////////////////////////////////////////////////	
	
	// method to check for applied mutation operator 
	public abstract void check(ASTNode leftNode, ASTNode rightNode);
	
	// method describing the action which should happen when an application was found
	public abstract void found(ASTNode leftNode, ASTNode rightNode);
	
	public MutationOperatorCategory getCategory(){
		return this.category;
	}
}
