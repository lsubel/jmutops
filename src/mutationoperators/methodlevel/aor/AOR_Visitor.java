package mutationoperators.methodlevel.aor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

public class AOR_Visitor extends TwoASTVisitor {

	public AOR_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(InfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
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
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PostfixExpression){
			PostfixExpression pe = (PostfixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, pe);
			
			// visit the operand
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		else if(localStoredTree instanceof PrefixExpression){
			PrefixExpression pe = (PrefixExpression) localStoredTree;
				
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
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PrefixExpression){
			PrefixExpression pe = (PrefixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, pe);
			
			// visit the operand
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		else if(localStoredTree instanceof PostfixExpression){
			PostfixExpression pe = (PostfixExpression) localStoredTree;
			
			// check for an application
			matcher.match(node, pe);
			
			// visit the operand
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		
		super.visit(node);
		return true;
	}
	
	
	
	

}
