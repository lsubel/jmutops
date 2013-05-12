package mutationoperators.jti;



import java.util.List;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;


public class JTI_Visitor extends BaseASTVisitor {
	
	public JTI_Visitor(BaseASTMatcher matcher, ASTNode secondTree) {
		super(matcher, secondTree);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		// apply matcher
		matcher.match(node, secondTree);
		
		// store tree locally
		ASTNode tempStore = secondTree;
		
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
		return true;
	}

	@Override
	public boolean visit(FieldAccess node) {
		matcher.match(node, secondTree);
		return true;
	}

	@Override
	public boolean visit(QualifiedName node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;

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
			// TODO: add exception
		}
		
		return true;
	}
	
	

	@Override
	public boolean visit(SimpleName node) {
		matcher.match(node, this.secondTree);
		return true;
	}

	@Override
	protected void visitSubtree(ASTNode left, ASTNode right){
		if(left != null){
			this.secondTree = right;
			left.accept(this);
		}
	}

	@Override
	protected void visitSubtrees(List list1, List list2) {
		if(list1.size() == list2.size()){
			for(int i=0; i < list1.size(); i++){
				visitSubtree((ASTNode) list1.get(i), (ASTNode) list2.get(i));
			}
		}
	}
	
	
}
