package mutationoperators.methodlevel.prv;

import mutationoperator.TwoASTMatcher;
import mutationoperator.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class PRV_Visitor extends TwoASTVisitor {

	public PRV_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(Assignment node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Assignment){
			Assignment assign = (Assignment) localStoredTree;
			
			// check for an valid assignment operator (=> normal assignment "=")
			boolean validAssignmentOperator = (node.getOperator() == Assignment.Operator.ASSIGN) && (assign.getOperator() == Assignment.Operator.ASSIGN);
			
			// check if the left sides of both versions are the same
			boolean sameLeftSide = (node.getLeftHandSide().subtreeMatch(defaultMatcher, assign.getLeftHandSide()));
			
			if(validAssignmentOperator && sameLeftSide){
				// check the right operators
				visitSubtree(node.getRightHandSide(), assign.getRightHandSide());
			}		
		}
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an access to an object on the parallel one
		if(localStoredTree instanceof FieldAccess){
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof QualifiedName){
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof SimpleName){
			matcher.match(node, localStoredTree);
		}
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an access to an object on the parallel one
		if(localStoredTree instanceof FieldAccess){
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof QualifiedName){
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof SimpleName){
			matcher.match(node, localStoredTree);
		}
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an access to an object on the parallel one
		if(localStoredTree instanceof FieldAccess){
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof QualifiedName){
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof SimpleName){
			matcher.match(node, localStoredTree);
		}
		return false;
	}
}
