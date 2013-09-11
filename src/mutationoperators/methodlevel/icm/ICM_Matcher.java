package mutationoperators.methodlevel.icm;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;

import utils.LiteralValueExtractor;

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
				// check some conditions
				boolean correctLeftNode = (node_right_expr instanceof BooleanLiteral);
				boolean correctRightNode = (node2_right_expr instanceof BooleanLiteral);
				
				if(correctLeftNode && correctRightNode) {
					boolean differentValues = (((BooleanLiteral) node_right_expr).booleanValue() ^ ((BooleanLiteral) node2_right_expr).booleanValue());
					
					// if all conditions are valid, notify a matching
					if(differentValues) {
						this.mutop.found(node, node2);
					}
				}
				
			}
			// 2) int, byte, short
			else if(node_left_type.equals("int") || node_left_type.equals("byte") || node_left_type.equals("short") ) {
				// get the right handed value
				int node_value = 0;
				int node2_value = 0;
				
				// try to calculcate the value for the literal node
				try {
					node_value = (int) LiteralValueExtractor.getValue(node_right_expr);
				} catch (Exception e) {
					return false;
				}
				try {
					node2_value = (int) LiteralValueExtractor.getValue(node2_right_expr);
				} catch (Exception e) {
					return false;
				}
				
				// check for any of the defined transformation
				boolean OneToZero = ((node_value == 1) && (node2_value == 0));
				boolean MinusOneToOne = ((node_value == -1) && (node2_value == 1));
				boolean FiveToMinusOne = ((node_value == 5) && (node2_value == -1));
				boolean OthersToIncrement = ((node_value != 1) && (node_value != -1) && (node_value != 5) && ((node_value + 1) == node2_value));
				
				// if all conditions are valid, notify a matching
				if(OneToZero || MinusOneToOne || FiveToMinusOne || OthersToIncrement) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("long")) {
				// get the right handed value
				long node_value = 0;
				long node2_value = 0;
				
				// try to calculcate the value for the literal node
				try {
					node_value = (long) LiteralValueExtractor.getValue(node_right_expr);
				} catch (Exception e) {
					return false;
				}
				try {
					node2_value = (long) LiteralValueExtractor.getValue(node2_right_expr);
				} catch (Exception e) {
					return false;
				}
				
				// check for any of the defined transformation
				boolean OneToZero = ((node_value == 1) && (node2_value == 0));
				boolean OthersToIncrement = ((node_value != 1) && ((node_value + 1) == node2_value));
				
				// if all conditions are valid, notify a matching
				if(OneToZero || OthersToIncrement) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("float")) {
				// get the right handed value
				float node_value = 0;
				float node2_value = 0;
				
				// try to calculcate the value for the literal node
				try {
					node_value = (float) LiteralValueExtractor.getValue(node_right_expr);
				} catch (Exception e) {
					return false;
				}
				try {
					node2_value = (float) LiteralValueExtractor.getValue(node2_right_expr);
				} catch (Exception e) {
					return false;
				}
				
				// check for any of the defined transformation
				boolean OneToZero = ((node_value == 1.0) && (node2_value == 0.0));
				boolean TwoToZero = ((node_value == 2.0) && (node2_value == 0.0));
				boolean OthersToIncrement = ((node_value != 1.0) && (node_value != 2.0) && (node2_value == 1.0));
				
				// if all conditions are valid, notify a matching
				if(OneToZero || TwoToZero || OthersToIncrement) {
					this.mutop.found(node, node2);
				}
			}
			else if(node_left_type.equals("double")) {
				// get the right handed value
				double node_value = 0;
				double node2_value = 0;
				
				// try to calculcate the value for the literal node
				try {
					node_value = LiteralValueExtractor.getValue(node_right_expr);
				} catch (Exception e) {
					return false;
				}
				try {
					node2_value = LiteralValueExtractor.getValue(node2_right_expr);
				} catch (Exception e) {
					return false;
				}
				
				// check for any of the defined transformation
				boolean OneToZero = ((node_value == 1.0) && (node2_value == 0.0));
				boolean OthersToIncrement = ((node_value != 1.0) && (node2_value == 1.0));
				
				// if all conditions are valid, notify a matching
				if(OneToZero || OthersToIncrement) {
					this.mutop.found(node, node2);
				}
			}
		}
		
		
		return false;
	}

}
