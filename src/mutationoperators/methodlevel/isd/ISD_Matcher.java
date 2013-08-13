package mutationoperators.methodlevel.isd;

import mutationoperator.MutationOperator;
import mutationoperator.TwoASTMatcher;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class ISD_Matcher extends TwoASTMatcher {

	public ISD_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(SuperFieldAccess node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(other instanceof FieldAccess){
			FieldAccess node2 = (FieldAccess) other;
			
			// check for the same field name
			boolean sameFieldName = node.getName().subtreeMatch(defaultMatcher, node2.getName());
			// check for same quanitifer
			boolean sameQuantifier = ((node.getQualifier() == null) && node2.getExpression() == null) || (node.getQualifier().subtreeMatch(defaultMatcher, node2.getExpression()));
			// if all conditions are true, notifiy a match
			if(sameFieldName && sameQuantifier){
				this.mutop.found(node, node2);
			}
		}
		else if(other instanceof SimpleName){
			SimpleName node2 = (SimpleName) other;
			
			// check for the same name
			boolean sameFieldName = node.getName().subtreeMatch(defaultMatcher, node2);
			// if all conditions are true, notify a match
			if(sameFieldName){
				this.mutop.found(node, node2);
			}
		}
		return false;
	}

	@Override
	public boolean match(SuperMethodInvocation node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(!(other instanceof MethodInvocation)){
			return false;
		}
		MethodInvocation node2 = (MethodInvocation) other;
		
		// check for a same method name
		boolean sameMethodName = node.getName().subtreeMatch(defaultMatcher, node2.getName());
		// check that the parameters are equal
		boolean sameArgumentLength = (node.arguments().size() == node2.arguments().size());
		// check for the same arguments
		boolean sameArgument = true;
		if(sameArgumentLength){
			sameArgument = this.defaultMatcher.safeSubtreeListMatch(node.arguments(), node2.arguments());
		}
		// check for the same quantifier
		boolean sameQuantifier = ((node.getQualifier() == null) && node2.getExpression() == null) || (node.getQualifier().subtreeMatch(defaultMatcher, node2.getExpression()));
		
		// if all conditions are true, notfiy a match
		if(sameMethodName && sameArgumentLength && sameArgument && sameQuantifier){
			this.mutop.found(node, node2);
		}
		
		return false;
	}
	
	

}
