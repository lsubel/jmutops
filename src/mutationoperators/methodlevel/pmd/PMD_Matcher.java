package mutationoperators.methodlevel.pmd;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import utils.ITypeBindingUtils;

public class PMD_Matcher extends TwoASTMatcher {

	public PMD_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(VariableDeclarationExpression node, Object other) {
		// check if the parallel tree has a valid type
		if(other instanceof VariableDeclarationExpression){
			VariableDeclarationExpression node2 = (VariableDeclarationExpression) other;
			
			// resolve bindings
			ITypeBinding firstBinding = node.resolveTypeBinding(); 
			ITypeBinding secondBinding = node2.resolveTypeBinding();
			
			// check if the new type is a parent class of the old one
			boolean validType = ITypeBindingUtils.isTypeParentOfOtherType(secondBinding, firstBinding);
			boolean differentTypes = !(secondBinding.isEqualTo(firstBinding));
			
			// check for the same variable name
			boolean oneDecleration = (node.fragments().size() == 1) && (node2.fragments().size() == 1);
			boolean sameVariableName = false;
			if(oneDecleration){
				VariableDeclarationFragment firstFragment = (VariableDeclarationFragment) node.fragments().get(0);
				VariableDeclarationFragment secondFragment = (VariableDeclarationFragment) node2.fragments().get(0);
				sameVariableName = (firstFragment.getName().subtreeMatch(defaultMatcher, secondFragment.getName()));
			}
			
			// if all conditions are valid, we notify a match
			if(validType && differentTypes && oneDecleration && sameVariableName){
				this.mutop.found(node, node2);
			}
		}
		return false;
	}

	@Override
	public boolean match(VariableDeclarationStatement node, Object other) {
		// check if the parallel tree has a valid type
		if(other instanceof VariableDeclarationStatement){
			VariableDeclarationStatement node2 = (VariableDeclarationStatement) other;
			
			// resolve bindings
			ITypeBinding firstBinding = node.getType().resolveBinding(); 
			ITypeBinding secondBinding = node2.getType().resolveBinding(); 
			
			// check if the new type is a parent class of the old one
			boolean validType = ITypeBindingUtils.isTypeParentOfOtherType(secondBinding, firstBinding);
			boolean differentTypes = !(secondBinding.isEqualTo(firstBinding));
			
			// check for the same variable name
			boolean oneDecleration = (node.fragments().size() == 1) && (node2.fragments().size() == 1);
			boolean sameVariableName = false;
			if(oneDecleration){
				VariableDeclarationFragment firstFragment = (VariableDeclarationFragment) node.fragments().get(0);
				VariableDeclarationFragment secondFragment = (VariableDeclarationFragment) node2.fragments().get(0);
				sameVariableName = (firstFragment.getName().subtreeMatch(defaultMatcher, secondFragment.getName()));
			}
			
			// if all conditions are valid, we notify a match
			if(validType && differentTypes && oneDecleration && sameVariableName){
				this.mutop.found(node, node2);
			}
		}
		return false;
	}
}
