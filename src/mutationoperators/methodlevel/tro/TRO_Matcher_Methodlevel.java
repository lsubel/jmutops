package mutationoperators.methodlevel.tro;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import utils.ITypeBindingUtils;

public class TRO_Matcher_Methodlevel extends TwoASTMatcher {

	public TRO_Matcher_Methodlevel(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(VariableDeclarationExpression node, Object other) {
		// check if the other node is also a VariableDeclarationExpression
		if(!(other instanceof VariableDeclarationExpression)) {
			return false;
		}
		
		// extract values
		VariableDeclarationExpression node2 = (VariableDeclarationExpression) other;
		ITypeBinding node_type_binding = node.getType().resolveBinding();
		ITypeBinding node2_type_binding = node2.getType().resolveBinding();
		
		// check for conditions
		boolean differentTypes = !(node.getType().subtreeMatch(defaultMatcher, node2.getType()));
		boolean sameModifiers = node.modifiers().equals(node2.modifiers());
		boolean compatibleTypes = ITypeBindingUtils.isTypeParentOfOtherType(node_type_binding, node2_type_binding);
		
		// if all conditions are true, notifiy a matching
		if(differentTypes && sameModifiers && compatibleTypes) {
			this.mutop.found(node, node2);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean match(VariableDeclarationStatement node, Object other) {
		// check if the other node is also a VariableDeclarationExpression
		if(!(other instanceof VariableDeclarationStatement)) {
			return false;
		}
		
		// extract values
		VariableDeclarationStatement node2 = (VariableDeclarationStatement) other;
		ITypeBinding node_type_binding = node.getType().resolveBinding();
		ITypeBinding node2_type_binding = node2.getType().resolveBinding();
		
		// check for conditions
		boolean differentTypes = !(node.getType().subtreeMatch(defaultMatcher, node2.getType()));
		boolean sameModifiers = node.modifiers().equals(node2.modifiers());
		boolean compatibleTypes = ITypeBindingUtils.isTypeParentOfOtherType(node_type_binding, node2_type_binding);

		// if all conditions are true, notifiy a matching
		if(differentTypes && sameModifiers && compatibleTypes) {
			this.mutop.found(node, node2);
			return true;
		}
		
		return false;
	}
	
	

}
