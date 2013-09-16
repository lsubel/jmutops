package mutationoperators.methodlevel.tro;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import utils.JDT_Utils;

public class TRO_Matcher_Methodlevel extends TwoASTMatcher {

	public TRO_Matcher_Methodlevel(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(CastExpression node, Object other) {
		// check if the other node is also a VariableDeclarationExpression
		if(!(other instanceof CastExpression)) {
			return false;
		}
		
		// extract values
		CastExpression node2 = (CastExpression) other;
		ITypeBinding node_type_binding = node.getType().resolveBinding();
		ITypeBinding node2_type_binding = node2.getType().resolveBinding();
		
		// check for conditions
		boolean differentTypes = !(node.getType().subtreeMatch(defaultMatcher, node2.getType()));
		boolean differentTypeBindings = !(node_type_binding.isEqualTo(node2_type_binding));
		boolean sameExpressionType = node.getExpression().resolveTypeBinding().isEqualTo(node2.getExpression().resolveTypeBinding());
		boolean compatibleTypes = JDT_Utils.isTypeParentOfOtherType(node_type_binding, node2_type_binding) || JDT_Utils.isTypeParentOfOtherType(node2_type_binding, node_type_binding);
		
		// if all conditions are true, notify a matching
		if(differentTypes && differentTypeBindings && sameExpressionType && compatibleTypes) {
			this.mutop.found(node, node2);
			return false;
		}
		
		return false;
	}

	@Override
	public boolean match(SingleVariableDeclaration node, Object other) {
		// if the compared AST is no SingleVariableDeclaration,
		// we cannot compare them
		if(!(other instanceof SingleVariableDeclaration)){
			return false;
		}
		
		// retrieve variables
		SingleVariableDeclaration node2 = (SingleVariableDeclaration) other;
		ITypeBinding firstBinding = node.getType().resolveBinding();
		ITypeBinding secondBinding = node2.getType().resolveBinding();
		
		// check if these are compatible types, so one type is the parent of the other one
		boolean isCompatible = JDT_Utils.isTypeParentOfOtherType(firstBinding, secondBinding) || JDT_Utils.isTypeParentOfOtherType(secondBinding, firstBinding); 
		boolean sameVariableName = node.getName().subtreeMatch(defaultMatcher, node2.getName());
		boolean differentTypes = !(firstBinding.isEqualTo(secondBinding));
		
		// if all conditions are true, we have a match
		if(sameVariableName && isCompatible && differentTypes){
			this.mutop.found(node, node2);
			return false;
		}
		
		return false;
	}
	
	@Override
	public boolean match(ThrowStatement node, Object other) {
		// if the compared AST is no ThrowStatement,
		// we cannot compare them
		if(!(other instanceof ThrowStatement)){
			return false;
		}
		
		// retrieve variables
		ThrowStatement node2 = (ThrowStatement) other;
		ITypeBinding firstBinding = node.getExpression().resolveTypeBinding();
		ITypeBinding secondBinding = node2.getExpression().resolveTypeBinding();
		
		// check conditions
		boolean differentExpressions = !(node.getExpression().subtreeMatch(defaultMatcher, node2.getExpression()));
		boolean differentBindings	 = !(firstBinding.isEqualTo(secondBinding));
		
		if(differentExpressions && differentBindings) {
			this.mutop.found(node, node2);
			return false;
		}
		
		return false;
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
		boolean compatibleTypes = JDT_Utils.isTypeParentOfOtherType(node_type_binding, node2_type_binding);
		
		// if all conditions are true, notify a matching
		if(differentTypes && sameModifiers && compatibleTypes) {
			this.mutop.found(node, node2);
			return false;
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
		boolean compatibleTypes = JDT_Utils.isTypeParentOfOtherType(node_type_binding, node2_type_binding);

		// if all conditions are true, notify a matching
		if(differentTypes && sameModifiers && compatibleTypes) {
			this.mutop.found(node, node2);
			return false;
		}
		
		return false;
	}
	
	

}
