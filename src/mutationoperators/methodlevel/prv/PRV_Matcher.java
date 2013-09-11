package mutationoperators.methodlevel.prv;

import mutationoperators.MutationOperator;
import mutationoperators.TwoASTMatcher;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class PRV_Matcher extends TwoASTMatcher {

	public PRV_Matcher(MutationOperator mutop) {
		super(mutop);
	}

	@Override
	public boolean match(FieldAccess node, Object other) {

		// define variables
		boolean differentVariables;
		boolean differentClasses;
		
		// check for an access to an object on the parallel one
		// then check if the references classes are different
		if(other instanceof FieldAccess){
			FieldAccess node2 = (FieldAccess) other;
			differentVariables = !(node.getName().subtreeMatch(defaultMatcher, node2.getName()));
			differentClasses   = !(node.resolveTypeBinding().isEqualTo(node2.resolveTypeBinding()));
		}
		else if(other instanceof QualifiedName){
			QualifiedName node2 = (QualifiedName) other;
			differentVariables = !(node.getName().subtreeMatch(defaultMatcher, node2.getName()));
			differentClasses   = !(node.resolveTypeBinding().isEqualTo(node2.resolveTypeBinding()));
		}
		else if(other instanceof SimpleName){
			SimpleName node2 = (SimpleName) other;
			differentVariables = !(node.subtreeMatch(defaultMatcher, node2));
			differentClasses   = !(node.resolveTypeBinding().isEqualTo(node2.resolveTypeBinding()));
		}
		else{
			return false;
		}
		
		if(differentVariables && differentClasses){
			mutop.found(node, (ASTNode) other);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean match(QualifiedName node, Object other) {
		// define variables
		boolean differentVariables;
		boolean differentClasses;
		
		// check for an access to an object on the parallel one
		// then check if the references classes are different
		if(other instanceof FieldAccess){
			FieldAccess node2 = (FieldAccess) other;
			differentVariables = !(node.getName().subtreeMatch(defaultMatcher, node2.getName()));
			differentClasses   = !(node.resolveTypeBinding().isEqualTo(node2.resolveTypeBinding()));
		}
		else if(other instanceof QualifiedName){
			QualifiedName node2 = (QualifiedName) other;
			differentVariables = !(node.getName().subtreeMatch(defaultMatcher, node2.getName()));
			differentClasses   = !(node.resolveTypeBinding().isEqualTo(node2.resolveTypeBinding()));
		}
		else if(other instanceof SimpleName){
			SimpleName node2 = (SimpleName) other;
			differentVariables = !(node.subtreeMatch(defaultMatcher, node2));
			differentClasses   = !(node.resolveTypeBinding().isEqualTo(node2.resolveTypeBinding()));
		}
		else{
			return false;
		}
		
		if(differentVariables && differentClasses){
			mutop.found(node, (ASTNode) other);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean match(SimpleName node, Object other) {
		// define variables
		boolean differentVariables;
		boolean differentClasses;
		
		// check for an access to an object on the parallel one
		// then check if the references classes are different
		if(other instanceof FieldAccess){
			FieldAccess node2 = (FieldAccess) other;
			differentVariables = !(node.subtreeMatch(defaultMatcher, node2.getName()));
			ITypeBinding firstBinding = node.resolveTypeBinding();
			ITypeBinding secondBinding = node2.resolveTypeBinding();
			differentClasses   = (firstBinding != null) && (secondBinding != null)  && !(firstBinding.isEqualTo(secondBinding));
		}
		else if(other instanceof QualifiedName){
			QualifiedName node2 = (QualifiedName) other;
			differentVariables = !(node.subtreeMatch(defaultMatcher, node2.getName()));
			ITypeBinding firstBinding = node.resolveTypeBinding();
			ITypeBinding secondBinding = node2.resolveTypeBinding();
			differentClasses   = (firstBinding != null) && (secondBinding != null)  && !(firstBinding.isEqualTo(secondBinding));
		}
		else if(other instanceof SimpleName){
			SimpleName node2 = (SimpleName) other;
			differentVariables = !(node.subtreeMatch(defaultMatcher, node2));
			ITypeBinding firstBinding = node.resolveTypeBinding();
			ITypeBinding secondBinding = node2.resolveTypeBinding();
			differentClasses   = (firstBinding != null) && (secondBinding != null)  && !(firstBinding.isEqualTo(secondBinding));
		}
		else{
			return false;
		}
		
		if(differentVariables && differentClasses){
			mutop.found(node, (ASTNode) other);
			return true;
		}
		
		return false;
	}
}
