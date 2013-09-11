package mutationoperators.methodlevel.prv;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
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
			
			ITypeBinding type_prefix_left = node.getLeftHandSide().resolveTypeBinding();
			ITypeBinding type_prefix_right = node.getRightHandSide().resolveTypeBinding();
			ITypeBinding type_postfix_left = assign.getLeftHandSide().resolveTypeBinding();
			ITypeBinding type_postfix_right = assign.getRightHandSide().resolveTypeBinding();
			
			// check for an valid assignment operator (=> normal assignment "=")
			boolean validAssignmentOperator = (node.getOperator() == Assignment.Operator.ASSIGN) && (assign.getOperator() == Assignment.Operator.ASSIGN);
			
			// check if the left sides of both versions are the same
			boolean sameLeftSide = (node.getLeftHandSide().subtreeMatch(defaultMatcher, assign.getLeftHandSide()));
			
			// TODO check if both right sides are compatible to their left sides
			boolean sameLeftSideTypes 			= type_prefix_left.isEqualTo(type_postfix_left);
			boolean differentRightSideTypes 	= !(type_prefix_right.isEqualTo(type_postfix_right));
			boolean firstObjectCompatibleToVar 	= type_prefix_right.isSubTypeCompatible(type_prefix_left);
			boolean secondObjectCompatibleToVar = type_postfix_right.isSubTypeCompatible(type_postfix_left);
			
			if(validAssignmentOperator && sameLeftSide && sameLeftSideTypes && differentRightSideTypes && firstObjectCompatibleToVar && secondObjectCompatibleToVar){
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
