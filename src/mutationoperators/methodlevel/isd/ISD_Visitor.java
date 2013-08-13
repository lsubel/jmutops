package mutationoperators.methodlevel.isd;

import mutationoperator.TwoASTMatcher;
import mutationoperator.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class ISD_Visitor extends TwoASTVisitor {

	public ISD_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof FieldAccess){
			FieldAccess fa = (FieldAccess) localStoredTree;
			
			// check for an application
			matcher.match(node, fa);
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), fa.getExpression());
			
			// visit the name node
			visitSubtree(node.getName(), fa.getName());
		}
		else if(localStoredTree instanceof SimpleName){
			SimpleName sn = (SimpleName) localStoredTree;
			
			// check for an application
			matcher.match(node, sn);
		}
		return false;
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getParallelTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// check for an application
			matcher.match(node, mi);
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), mi.getExpression());
			
			// visit the typeArgument nodes
			visitSubtrees(node.typeArguments(), mi.typeArguments());
			
			// visit the name node
			visitSubtree(node.getName(), mi.getName());
			
			// visit the argument nodes
			visitSubtrees(node.arguments(), mi.arguments());
		}
		return false;
	}

	
	
}
