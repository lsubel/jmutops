package mutationoperators.methodlevel.vro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class VRO_Visitor extends TwoASTVisitor {

	public VRO_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(SimpleName node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();

		// if the other is a constant, check for a matching
		if(localStoredTree instanceof NumberLiteral){
			// check for an application
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof StringLiteral){
			// check for an application
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof SimpleName) {
			// check for an application
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof FieldAccess) {
			// check for an application
			matcher.match(node, localStoredTree);
		}
		else if(localStoredTree instanceof QualifiedName){
			// check for an application
			matcher.match(node, localStoredTree);
		}
		
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodDeclaration){
			MethodDeclaration md = (MethodDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), md.getJavadoc());
			
			// DO NOT visit the name node
			//visitSubtree(node.getName(), md.getName());
			
			if (node.getAST().apiLevel() == AST.JLS4) {
				// visit the modifier nodes
				visitSubtrees(node.modifiers(), md.modifiers());
				// visit the typeparameter nodes
				visitSubtrees(node.typeParameters(), md.typeParameters());
				// visit the returntype2 node
				visitSubtree(node.getReturnType2(), md.getReturnType2());
			}
			
			// visit the parameters node
			visitSubtrees(node.parameters(), md.parameters());
			
			// vist the exception nodes
			visitSubtrees(node.thrownExceptions(), md.thrownExceptions());
			
			// visit the body node
			visitSubtree(node.getBody(), md.getBody());	
		}
		return false;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), mi.getExpression());;
			
			// visit each type argument
			visitSubtrees(node.typeArguments(), mi.typeArguments());
			
			// DO NOT visit the name node
			//visitSubtree(node.getName(), mi.getName());
			
			// visit each arguments 
			visitSubtrees(node.arguments(), mi.arguments());
		}
		return false;
	}

	
	
}
