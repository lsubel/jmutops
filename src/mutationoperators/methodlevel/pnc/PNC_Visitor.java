package mutationoperators.methodlevel.pnc;

import mutationoperator.TwoASTMatcher;
import mutationoperator.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;

public class PNC_Visitor extends TwoASTVisitor {

	public PNC_Visitor(TwoASTMatcher matcher) {
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
			boolean sameLeftType = (node.getLeftHandSide().resolveTypeBinding().isEqualTo(assign.getLeftHandSide().resolveTypeBinding()));
			
			// check if both assignments are type compatible
			boolean firstVersionCompatible 	= (node.getLeftHandSide().resolveTypeBinding().isSubTypeCompatible(node.getRightHandSide().resolveTypeBinding()));
			boolean secondVersionCompatible = (assign.getLeftHandSide().resolveTypeBinding().isSubTypeCompatible(assign.getRightHandSide().resolveTypeBinding()));
			boolean versionsCompatible = firstVersionCompatible || secondVersionCompatible;
			if(validAssignmentOperator && sameLeftSide && sameLeftType && versionsCompatible){
				// check the right operators
				visitSubtree(node.getRightHandSide(), assign.getRightHandSide());
			}		
		}
		return false;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ClassInstanceCreation){
			ClassInstanceCreation cic = (ClassInstanceCreation) localStoredTree;
			
			// check for an application
			matcher.match(node, cic);
			
			// visit the expression node
			visitSubtree(node.getExpression(), cic.getExpression());
			
			// visit the AnonymousClassDeclaration node
			visitSubtree(node.getAnonymousClassDeclaration(), cic.getAnonymousClassDeclaration());
			
			// visit the type node
			visitSubtree(node.getType(), cic.getType());
			
			// visit the argument nodes
			visitSubtrees(node.arguments(), cic.arguments());
		}
		return false;
	}
	
	
}
