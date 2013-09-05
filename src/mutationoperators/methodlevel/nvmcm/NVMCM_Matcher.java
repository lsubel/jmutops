package mutationoperators.methodlevel.nvmcm;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;

public class NVMCM_Matcher extends TwoASTMatcher {

	public NVMCM_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(MethodInvocation node, Object other) {
		// if the parallel node is NullLiteral
		if(other instanceof NullLiteral) {
			// cast the node
			NullLiteral node2 = (NullLiteral) other;
			
			// check for conditions
			boolean isObjectReturnType =  !(node.resolveMethodBinding().getReturnType().isPrimitive());
			boolean isMethod = !(node.resolveMethodBinding().isConstructor());
			
			// if all conditions are true, notify a matching
			if(isObjectReturnType && isMethod) {
				mutop.found(node, node2);
			}
		}
		// if the parallel node is BooleanLiteral
		else if(other instanceof BooleanLiteral) {
			// cast the node
			BooleanLiteral node2 = (BooleanLiteral) other;
			
			// check for conditions
			boolean isFalse = !(node2.booleanValue());
			boolean isPrimitive =  node.resolveMethodBinding().getReturnType().isPrimitive();
			boolean isBooleanReturnType = node.resolveMethodBinding().getReturnType().getName().equals("boolean");
			boolean isMethod = !(node.resolveMethodBinding().isConstructor());
			
			// if all conditions are true, notify a matching
			if(isFalse && isPrimitive && isBooleanReturnType && isMethod) {
				mutop.found(node, node2);
			}
		}
		// if the parallel node is CharacterLiteral
		else if(other instanceof CharacterLiteral) {
			// cast the node
			CharacterLiteral node2 = (CharacterLiteral) other;
			char node2_value = node2.getEscapedValue().charAt(1);
			
			// check for conditions
			boolean isDefaultCharacterLiteral = (node2_value == '\u0000');
			boolean isPrimitive =  node.resolveMethodBinding().getReturnType().isPrimitive();
			boolean isCharReturnType = node.resolveMethodBinding().getReturnType().getName().equals("char");
			boolean isMethod = !(node.resolveMethodBinding().isConstructor());
			
			// if all conditions are true, notify a matching
			if(isDefaultCharacterLiteral && isPrimitive && isCharReturnType && isMethod) {
				mutop.found(node, node2);
			}
			
		}
		// if the parallel node is CharacterLiteral
		else if(other instanceof NumberLiteral) {
			// cast the node
			NumberLiteral node2 = (NumberLiteral) other; 
			int node2_value = Integer.parseInt(node2.getToken());
			
			// check for conditions
			boolean isDefaultIntLiteral = (node2_value == 0);
			boolean isPrimitive =  node.resolveMethodBinding().getReturnType().isPrimitive();
			boolean isIntReturnType = node.resolveMethodBinding().getReturnType().getName().equals("int");
			boolean isMethod = !(node.resolveMethodBinding().isConstructor());
			
			// if all conditions are true, notify a matching
			if(isDefaultIntLiteral && isPrimitive && isIntReturnType && isMethod) {
				mutop.found(node, node2);
			}
		}
		return false;
	}
}
