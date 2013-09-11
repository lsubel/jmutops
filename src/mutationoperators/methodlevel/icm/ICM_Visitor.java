package mutationoperators.methodlevel.icm;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ICM_Visitor extends TwoASTVisitor {

	public ICM_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(Assignment node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Assignment){
			Assignment assign = (Assignment) localStoredTree;
			
			ITypeBinding type_prefix_left 	= node.getLeftHandSide().resolveTypeBinding();
			ITypeBinding type_prefix_right 	= node.getRightHandSide().resolveTypeBinding();
			ITypeBinding type_postfix_left 	= assign.getLeftHandSide().resolveTypeBinding();
			ITypeBinding type_postfix_right = assign.getRightHandSide().resolveTypeBinding();
			
			// check for some preconditions
			// TODO resolve Binding for left side
			boolean isNotFinal       	= (!Modifier.isFinal(type_prefix_left.getModifiers()) && !Modifier.isFinal(type_postfix_left.getModifiers()));
			boolean leftIsPrimitive  	= (type_prefix_left.isPrimitive() && type_postfix_left.isPrimitive());
			boolean rightIsPrimitive 	= (type_prefix_right.isPrimitive() && type_postfix_right.isPrimitive());
			boolean sameLeftSide 		= node.getLeftHandSide().subtreeMatch(defaultMatcher, assign.getLeftHandSide());
			boolean sameLeftType 		= type_prefix_left.isEqualTo(type_postfix_left);
			boolean sameRightType 		= type_prefix_right.isEqualTo(type_postfix_right) || (type_postfix_right.isCastCompatible(type_postfix_left));
			
			// if all conditions are valid,
			// try to match it
			if(isNotFinal && leftIsPrimitive && rightIsPrimitive && sameLeftSide && sameLeftType && sameRightType) {
				this.matcher.match(node, assign);
			}
		}
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof VariableDeclarationFragment){
			VariableDeclarationFragment vdf = (VariableDeclarationFragment) localStoredTree;
			
			// if there is no initializer, abort
			if((node.getInitializer() == null) || (vdf.getInitializer() == null)) {
				return false;
			}
			
			ITypeBinding type_prefix_left 	= node.getName().resolveTypeBinding();
			ITypeBinding type_prefix_right 	= node.getInitializer().resolveTypeBinding();
			ITypeBinding type_postfix_left 	= vdf.getName().resolveTypeBinding();
			ITypeBinding type_postfix_right = vdf.getInitializer().resolveTypeBinding();
			
			// check for some preconditions
			boolean isNotFinal       	= (!Modifier.isFinal(node.getName().resolveBinding().getModifiers()) && !Modifier.isFinal(node.getName().resolveBinding().getModifiers()));
			boolean leftIsPrimitive  	= (type_prefix_left.isPrimitive() && type_postfix_left.isPrimitive());
			boolean rightIsPrimitive 	= (type_prefix_right.isPrimitive() && type_postfix_right.isPrimitive());
			boolean sameLeftSide 		= node.getName().subtreeMatch(defaultMatcher, vdf.getName());
			boolean sameLeftType 		= type_prefix_left.isEqualTo(type_postfix_left);
			boolean sameRightType 		= type_prefix_right.isEqualTo(type_postfix_right) || (type_postfix_right.isCastCompatible(type_postfix_left));
			
			// if all conditions are valid,
			// try to match it
			if(isNotFinal && leftIsPrimitive && rightIsPrimitive && sameLeftSide && sameLeftType && sameRightType) {
				this.matcher.match(node, vdf);
			}
		}
		return false;
	}
	
	
}
