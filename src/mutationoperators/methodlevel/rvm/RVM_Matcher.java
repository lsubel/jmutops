package mutationoperators.methodlevel.rvm;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ReturnStatement;

import utils.LiteralValueExtractor;

public class RVM_Matcher extends TwoASTMatcher {

	public RVM_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(ReturnStatement node, Object other) {
		// if the other object is no ReturnStatement, 
		// we cannot match anything
		if(!(other instanceof ReturnStatement)) {
			return false;
		}
		
		ReturnStatement node2 = (ReturnStatement) other;
		
		// get the expressions of both ReturnStatements
		Expression node_expr = node.getExpression();
		Expression node2_expr = node2.getExpression();
		
		// if any of the Expression is null, abort
		if((node_expr == null) || (node2_expr == null)) {
			return false;
		}
		
		// 1) boolean
		if((node_expr instanceof BooleanLiteral) && (node2_expr instanceof BooleanLiteral)) {
			// cast the nodes
			BooleanLiteral node_bool  = (BooleanLiteral) node_expr;
			BooleanLiteral node2_bool = (BooleanLiteral) node2_expr;
			
			// calculate conditions
			boolean differentReturnValue = (node_bool.booleanValue() ^ node2_bool.booleanValue());
			
			// if all conditions are true, notify a matching
			if(differentReturnValue) {
				mutop.found(node, node2);
			}
		}
		// 2) integer, byte, short, long, float, double
		else if((node_expr instanceof NumberLiteral) || (node2_expr instanceof NumberLiteral)){
			
			ITypeBinding node_binding = node_expr.resolveTypeBinding();
			ITypeBinding node2_binding = node2_expr.resolveTypeBinding();
			double node_value = 0;
			double node2_value = 0;
			
			// try to calculcate the value for the literal node
			try {
				node_value = LiteralValueExtractor.getValue(node_expr);
			} catch (Exception e) {
				return false;
			}
			try {
				node2_value = LiteralValueExtractor.getValue(node2_expr);
			} catch (Exception e) {
				return false;
			}
			
			// cast the nodes
//			NumberLiteral node_number  = (NumberLiteral) node_expr;
//			NumberLiteral node2_number = (NumberLiteral) node2_expr;

			
			// calculate conditions
			boolean bothPrimitive = ((node_binding.isPrimitive()) && (node2_binding.isPrimitive()));
			boolean sameType 	  = ((node_binding.isEqualTo(node2_binding)));
			
			if(bothPrimitive && sameType) {
				// 2.1) integer, byte, short
				boolean isInt = node_binding.getName().equals("int");
				boolean isByte = node_binding.getName().equals("byte");
				boolean isShort = node_binding.getName().equals("short");
				if(isInt || isByte || isShort) {
					// check for a correct transformation
					boolean isZeroToOne = ((node_value == 0) && (node2_value == 1));
					boolean isElseToZero = ((node_value != 0) && (node2_value == 0));
				
					// if all conditions are true, notify a matching
					if(isZeroToOne || isElseToZero) {
						mutop.found(node, node2);
					}
				}
				
				// 2.2) long
				boolean isLong = node_binding.getName().equals("long");
				if(isLong) {
					// check for a correct transformation
					boolean correctTransform = (node2_value == (node_value - 1));
					
					// if all conditions are true, notify a matching
					if(correctTransform) {
						mutop.found(node, node2);
					}
				}
				
				// 2.3) float, double
				boolean isFloat = node_binding.getName().equals("float");
				boolean isDouble = node_binding.getName().equals("double");
				if(isFloat || isDouble) {
					// check for a correct transformation
					boolean correctTransform = (node2_value == -(node_value + 1.0));
					
					// if all conditions are true, notify a matching
					if(correctTransform) {
						mutop.found(node, node2);
					}
				}
			}
		}
		// 3) object
		if(!(node_expr instanceof NullLiteral) && (node2_expr instanceof NullLiteral)) {
			
			// calculate conditions
			boolean isObject = !(node_expr.resolveTypeBinding().isPrimitive());
			boolean isNotNull = !(node_expr instanceof NullLiteral);
			
			// if all conditions are true, notify a matching
			if(isObject && isNotNull) {
				mutop.found(node, node2);
			}
		}
		
		return false;
	}
	
	

}
