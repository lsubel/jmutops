package mutationoperators.methodlevel.icm;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ICM_Matcher extends TwoASTMatcher {

	public ICM_Matcher(MutationOperator mutop) {
		super(mutop);
	}
	
	@Override
	public boolean match(Assignment node, Object other) {
		// cast the postfix object
		if(!(other instanceof Assignment)) {
			return false;
		}
		Assignment node2 = (Assignment) other;
				
		// resolve the left side type
		String node_left_type  = node.getLeftHandSide().resolveTypeBinding().getName();
		String node2_left_type = node2.getLeftHandSide().resolveTypeBinding().getName();
		boolean sameTypeName = node_left_type.equals(node2_left_type);
		
		if(sameTypeName) {
			// depending on the type if the left side,
			// test for the specified mutations
			Expression node_right_expr  = node.getRightHandSide();
			Expression node2_right_expr = node2.getRightHandSide();
			
			// 1) boolean
			if(node_left_type.equals("boolean")) {
				// check for a matching
				if(checkBooleanMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			// 2) int, byte, short
			else if(node_left_type.equals("int") || node_left_type.equals("byte") || node_left_type.equals("short") ) {
				// check for a matching
				if(checkIntByteShortMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("long")) {
				// check for a matching
				if(checkLongMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("float")) {
				// check for a matching
				if(checkFloatMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("double")) {
				// check for a matching
				if(checkDoubleMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
		}		
		return false;
	}
	
	@Override
	public boolean match(VariableDeclarationFragment node, Object other) {
		// cast the postfix object
		if(!(other instanceof VariableDeclarationFragment)) {
			return false;
		}
		VariableDeclarationFragment node2 = (VariableDeclarationFragment) other;
				
		// resolve the left side type
		String node_left_type  = node.getName().resolveTypeBinding().getName();
		String node2_left_type = node2.getName().resolveTypeBinding().getName();
		boolean sameTypeName = node_left_type.equals(node2_left_type);
		
		if(sameTypeName) {
			// depending on the type if the left side,
			// test for the specified mutations
			Expression node_right_expr  = node.getInitializer();
			Expression node2_right_expr = node2.getInitializer();
			
			// check if both expressions have initializer;
			// in this case abort
			if((node_right_expr == null) || (node2_right_expr == null)) {
				return false;
			}
			
			// 1) boolean
			if(node_left_type.equals("boolean")) {
				// check for a matching
				if(checkBooleanMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			// 2) int, byte, short
			else if(node_left_type.equals("int") || node_left_type.equals("byte") || node_left_type.equals("short") ) {
				// check for a matching
				if(checkIntByteShortMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("long")) {
				// check for a matching
				if(checkLongMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("float")) {
				// check for a matching
				if(checkFloatMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("double")) {
				// check for a matching
				if(checkDoubleMatch(node_right_expr, node2_right_expr)) {
					this.mutop.found(node, node2);
				}
			}
		}		
		return false;
	}


	//////////////////////////////////////////
	/// private methods
	//////////////////////////////////////////
	
	

	private boolean checkBooleanMatch(Expression node_right_expr, Expression node2_right_expr) {
		// check some conditions
		boolean correctLeftNode = (node_right_expr instanceof BooleanLiteral);
		boolean correctRightNode = (node2_right_expr instanceof BooleanLiteral);
		
		if(correctLeftNode && correctRightNode) {
			boolean differentValues = (((BooleanLiteral) node_right_expr).booleanValue() ^ ((BooleanLiteral) node2_right_expr).booleanValue());
		
			return differentValues;
			
		}
		return false;
	}

	private boolean checkIntByteShortMatch(Expression node_right_expr,
			Expression node2_right_expr) {
		// get the right handed value
		int node_value = 0;
		int node2_value = 0;
		
		// try to calculcate the value for the literal node
		Object node_obj_value = node_right_expr.resolveConstantExpressionValue();
		Object node2_obj_value = node2_right_expr.resolveConstantExpressionValue();
		
		boolean node_valid_object = ((node_obj_value instanceof Integer) || (node_obj_value instanceof Byte) || (node_obj_value instanceof Short));
		boolean node2_valid_object = ((node2_obj_value instanceof Integer) || (node2_obj_value instanceof Byte) || (node2_obj_value instanceof Short));
		boolean compatibleType = node_right_expr.resolveTypeBinding().isCastCompatible(node2_right_expr.resolveTypeBinding());
		
		if(node_valid_object && node2_valid_object && compatibleType) {
			// extract the values
			if(node_obj_value instanceof Integer) {
				node_value	= ((Integer) node_obj_value).intValue(); 
			}
			else if(node_obj_value instanceof Byte) {
				node_value	= ((Byte) node_obj_value).byteValue();
			}
			else if(node_obj_value instanceof Short) {
				node_value	= ((Short) node_obj_value).shortValue();
			}
			else {
				throw new IllegalStateException("This cannot happen!");
			}
			
			if(node2_obj_value instanceof Integer) {
				node2_value	= ((Integer) node2_obj_value).intValue(); 
			}
			else if(node2_obj_value instanceof Byte) {
				node2_value	= ((Byte) node2_obj_value).byteValue();
			}
			else if(node2_obj_value instanceof Short) {
				node2_value	= ((Short) node2_obj_value).shortValue();
			}
			else {
				throw new IllegalStateException("This cannot happen!");
			}
		}
		else {
			return false;
		}

		// check for any of the defined transformation
		boolean OneToZero = ((node_value == 1) && (node2_value == 0));
		boolean MinusOneToOne = ((node_value == -1) && (node2_value == 1));
		boolean FiveToMinusOne = ((node_value == 5) && (node2_value == -1));
		boolean OthersToIncrement = ((node_value != 1) && (node_value != -1) && (node_value != 5) && ((node_value + 1) == node2_value));
		
		// if all conditions are valid, notify a matching
		if(OneToZero || MinusOneToOne || FiveToMinusOne || OthersToIncrement) {
			return true;
		}
		
		return false;
	}
	
	private boolean checkLongMatch(Expression node_right_expr,
			Expression node2_right_expr) {
		// get the right handed value
		long node_value = 0;
		long node2_value = 0;
		
		// try to calculcate the value for the literal node
		Object node_obj_value = node_right_expr.resolveConstantExpressionValue();
		Object node2_obj_value = node2_right_expr.resolveConstantExpressionValue();
		
		boolean node_valid_object = (node_obj_value instanceof Long) || (node_obj_value instanceof Integer);
		boolean node2_valid_object = (node2_obj_value instanceof Long) || (node2_obj_value instanceof Integer);
		boolean sameType = node_obj_value.getClass().equals(node2_obj_value.getClass());
		
		if(node_valid_object && node2_valid_object && sameType) {
			// extract the values
			if(node_obj_value instanceof Long) {
				node_value	= ((Long) node_obj_value).longValue();
				node2_value = ((Long) node2_obj_value).longValue();
			}
			else if(node_obj_value instanceof Integer) {
				node_value	= ((Integer) node_obj_value).longValue();
				node2_value = ((Integer) node2_obj_value).longValue();
			}
			else {
				throw new IllegalStateException("This cannot happen!");
			}
		}
		else {
			return false;
		}
		
		// check for any of the defined transformation
		boolean OneToZero = ((node_value == 1) && (node2_value == 0));
		boolean OthersToIncrement = ((node_value != 1) && ((node_value + 1) == node2_value));
		
		// if all conditions are valid, notify a matching
		if(OneToZero || OthersToIncrement) {
			return true;
		}
		return false;
	}
	
	private boolean checkDoubleMatch(Expression node_right_expr,
			Expression node2_right_expr) {
		// get the right handed value
		double node_value = 0;
		double node2_value = 0;
		
		// try to calculcate the value for the literal node
		Object node_obj_value = node_right_expr.resolveConstantExpressionValue();
		Object node2_obj_value = node2_right_expr.resolveConstantExpressionValue();
		
		boolean node_valid_object = (node_obj_value instanceof Double);
		boolean node2_valid_object = (node2_obj_value instanceof Double);
		boolean sameType = node_obj_value.getClass().equals(node2_obj_value.getClass());
		
		if(node_valid_object && node2_valid_object && sameType) {
			// extract the values
			node_value	= ((Double) node_obj_value).doubleValue();
			node2_value = ((Double) node2_obj_value).doubleValue();
		}
		else {
			return false;
		}
		
		// check for any of the defined transformation
		boolean OneToZero = ((node_value == 1.0) && (node2_value == 0.0));
		boolean OthersToIncrement = ((node_value != 1.0) && (node2_value == 1.0));
		
		// if all conditions are valid, notify a matching
		if(OneToZero || OthersToIncrement) {
			return true;
		}
		return false;
	}

	private boolean checkFloatMatch(Expression node_right_expr,
			Expression node2_right_expr) {
		// get the right handed value
		double node_value = 0;
		double node2_value = 0;
		
		// try to calculcate the value for the literal node
		Object node_obj_value = node_right_expr.resolveConstantExpressionValue();
		Object node2_obj_value = node2_right_expr.resolveConstantExpressionValue();
		
		boolean node_valid_object = (node_obj_value instanceof Float) || ((node_obj_value instanceof Double));
		boolean node2_valid_object = (node2_obj_value instanceof Float) || ((node2_obj_value instanceof Double));
		boolean compatibleType = node_right_expr.resolveTypeBinding().isCastCompatible(node2_right_expr.resolveTypeBinding());
		
		if(node_valid_object && node2_valid_object && compatibleType) {
			if(node_obj_value instanceof Float) {
				node_value	= ((Float) node_obj_value).floatValue();
			}
			else if(node_obj_value instanceof Double) {
				node_value	= ((Double) node_obj_value).doubleValue();
			}
			else {
				throw new IllegalStateException("This cannot happen!");
			}
			
			if(node2_obj_value instanceof Float) {
				node2_value	= ((Float) node2_obj_value).floatValue();
			}
			else if(node2_obj_value instanceof Double) {
				node2_value	= ((Double) node2_obj_value).doubleValue();
			}
			else {
				throw new IllegalStateException("This cannot happen!");
			}
		}
		else {
			return false;
		}
		
		// check for any of the defined transformation
		boolean OneToZero = ((node_value == 1.0) && (node2_value == 0.0));
		boolean TwoToZero = ((node_value == 2.0) && (node2_value == 0.0));
		boolean OthersToIncrement = ((node_value != 1.0) && (node_value != 2.0) && (node2_value == 1.0));
		
		// if all conditions are valid, notify a matching
		if(OneToZero || TwoToZero || OthersToIncrement) {
			return true;
		}
		return false;
	}
	
}
