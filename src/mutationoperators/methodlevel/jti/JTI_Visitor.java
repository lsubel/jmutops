package mutationoperators.methodlevel.jti;



import mutationoperator.TwoASTMatcher;
import mutationoperator.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;


public class JTI_Visitor extends TwoASTVisitor {
	
	public JTI_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// apply matcher
		matcher.match(node, parallelTree);
		
		// store tree locally
		ASTNode tempStore = getParallelTree();
		
		// call visitor on subelements
		if(tempStore instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) tempStore;
			
			// check for any subexpression
			visitSubtree(node.getExpression(), mi.getExpression());
			
			// check for the same number of arguments
			if(node.arguments().size() == mi.arguments().size()){
				visitSubtrees(node.arguments(), mi.arguments());
			}
		}
		// return result
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		matcher.match(node, parallelTree);
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		// locally store the second tree
		ASTNode tempStore = this.parallelTree;

		// check case: a.b and this.a.b
		if(tempStore instanceof FieldAccess){
			// cast node to type
			// extract attributes from ASTNodes
			FieldAccess fa = (FieldAccess) tempStore;
			SimpleName name = node.getName();
			Expression expr = node.getQualifier();
			SimpleName name2 = fa.getName();
			Expression expr2 = fa.getExpression();
			
			// if the names are equal
			boolean haveSameName = defaultMatcher.match(name, name2);
			if(haveSameName){
				// check the next stage
				visitSubtree(expr, expr2);
			}
			return false;
		}
		
		return false;
	}
	
	

	@Override
	public boolean visit(SimpleName node) {
		matcher.match(node, this.parallelTree);
		return false;
	}	
}
