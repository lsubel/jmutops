package mutationoperators.eoa;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class EOA_Visitor extends BaseASTVisitor {

	public EOA_Visitor(BaseASTMatcher matcher) {
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
		
		super.visit(node);
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an method call on the parallel tree
		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// check if the corresponding object is the same
			boolean sameObject = (node.subtreeMatch(defaultMatcher, mi.getExpression()));
			
			if(sameObject){
				// check for an application
				matcher.match(node, mi);
			}
		}
		super.visit(node);
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an method call on the parallel tree
		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// check if the corresponding object is the same
			boolean sameObject = (node.subtreeMatch(defaultMatcher, mi.getExpression()));
			
			if(sameObject){
				// check for an application
				matcher.match(node, mi);
			}
		}
		super.visit(node);
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for an method call on the parallel tree
		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// check if the corresponding object is the same
			boolean sameObject = (node.subtreeMatch(defaultMatcher, mi.getExpression()));
			
			if(sameObject){
				// check for an application
				matcher.match(node, mi);
			}
		}
		super.visit(node);
		return false;
	}
	
	
	
	

}
