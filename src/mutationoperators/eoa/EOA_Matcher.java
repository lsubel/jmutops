package mutationoperators.eoa;

import mutationoperators.TwoASTMatcher;
import mutationoperators.MutationOperator;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class EOA_Matcher extends TwoASTMatcher {

	public EOA_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(FieldAccess node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(!(other instanceof MethodInvocation)){
			return false;
		}
		MethodInvocation node2 = (MethodInvocation) other;
		
		// check if the method is a clone()
		boolean isClone = (node2.getName().getIdentifier().equals("clone") && (node2.typeArguments() == null || node2.typeArguments().size() == 0));
		// check if the corresponding object is the same
		boolean sameObject = (node.subtreeMatch(defaultMatcher, node2.getExpression()));
		
		// if both conditions are true
		// we have found a matching
		if(isClone && sameObject){
			mutop.found(node, node2);
			return true;			
		}
		return false;
	}

	@Override
	public boolean match(QualifiedName node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(!(other instanceof MethodInvocation)){
			return false;
		}
		MethodInvocation node2 = (MethodInvocation) other;
		
		// check if the method is a clone()
		boolean isClone = (node2.getName().getIdentifier().equals("clone") && (node2.typeArguments() == null || node2.typeArguments().size() == 0));
		// check if the corresponding object is the same
		boolean sameObject = (node.subtreeMatch(defaultMatcher, node2.getExpression()));
		
		// if both conditions are true
		// we have found a matching
		if(isClone && sameObject){
			mutop.found(node, node2);
			return true;			
		}
		return false;
	}

	@Override
	public boolean match(SimpleName node, Object other) {
		// if the compared AST is no MethodInvocation,
		// we cannot compare them
		if(!(other instanceof MethodInvocation)){
			return false;
		}
		MethodInvocation node2 = (MethodInvocation) other;
		
		// check if the method is a clone()
		boolean isClone = (node2.getName().getIdentifier().equals("clone") && (node2.typeArguments() == null || node2.typeArguments().size() == 0));
		// check if the corresponding object is the same
		boolean sameObject = (node.subtreeMatch(defaultMatcher, node2.getExpression()));
		
		// if both conditions are true
		// we have found a matching
		if(isClone && sameObject){
			mutop.found(node, node2);
			return true;			
		}
		return false;
	}

	
}
