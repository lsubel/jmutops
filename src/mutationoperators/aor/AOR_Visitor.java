package mutationoperators.aor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class AOR_Visitor extends BaseASTVisitor {

	public AOR_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	protected void visitSubtree(ASTNode left, ASTNode right) {
		if(left != null){
			this.secondTree = right;
			left.accept(this);
		}
	}

	@Override
	protected void visitSubtrees(List list1, List list2) {
		if(list1.size() == list2.size()){
			for(int i=0; i < list1.size(); i++){
				visitSubtree((ASTNode) list1.get(i), (ASTNode) list2.get(i));
			}
		}
	}

	@Override
	public boolean visit(InfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, ie);
			
			// visit the left operator
			visitSubtree(node.getLeftOperand(), ie.getLeftOperand());
			
			// visit the right operator
			visitSubtree(node.getRightOperand(), ie.getRightOperand());
			
			// visit the extended expression
			visitSubtrees(node.extendedOperands(), ie.extendedOperands());			
		}
		
		super.visit(node);
		return true;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PostfixExpression){
			PostfixExpression pe = (PostfixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, pe);
			
			// visit the operand
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		
		super.visit(node);
		return true;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PrefixExpression){
			PrefixExpression pe = (PrefixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, pe);
			
			// visit the operand
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		
		super.visit(node);
		return true;
	}
	
	
	
	

}
