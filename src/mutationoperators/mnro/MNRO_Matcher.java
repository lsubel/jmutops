package mutationoperators.mnro;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import utils.Settings;

import mutationoperators.BaseASTMatcher;
import mutationoperators.MutationOperator;

public class MNRO_Matcher extends BaseASTMatcher {

	public MNRO_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(MethodInvocation node, Object other) {
		
		// if the compared AST is  MethodInvocation
		if(other instanceof MethodInvocation){
			MethodInvocation node2 = (MethodInvocation) other;
			
			// check if these the names are different
			boolean differentName = !(this.defaultMatcher.match(node.getName(), node2.getName()));
			// check that the parameters are equal
			boolean sameArgumentLength = (node.arguments().size() == node2.arguments().size());
			// check for the same arguments
			boolean sameArgument = true;
			if(sameArgumentLength){
				sameArgument = this.defaultMatcher.safeSubtreeListMatch(node.arguments(), node2.arguments());
			}
			// check that the return type is the same;
			// in case of unresolved bindings, use default value
			boolean sameReturnType;
			ITypeBinding prefixType 	= node.resolveTypeBinding();
			ITypeBinding postfixType 	= node2.resolveTypeBinding();
			if((prefixType != null) & (postfixType != null)){
				sameReturnType = prefixType.isEqualTo(postfixType);
			}
			else{
				sameReturnType = Settings.BINDING_DEFAULTVALUE;
			}
			// check for all conditions
			if(differentName && sameArgumentLength && sameArgument && sameReturnType){
				this.mutop.found(node, node2);
				return true;
			}
		}
		
		return false;
	}

	
	
}
