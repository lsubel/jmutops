import java.util.List;

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


public class JTI_Visitor extends ASTVisitor {

	ASTNode secondTree;
	JTI_Matcher matcher;
	ASTMatcher defaultMatcher;
	
	public JTI_Visitor(ASTNode secondTree, JTI_Matcher matcher) {
		this.secondTree = secondTree;
		this.matcher = matcher;
		this.defaultMatcher = new ASTMatcher();
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		// init return value
		boolean result = false;
		// apply matcher
		if(matcher.match(node, secondTree)){
			System.out.println("FOUND METHODINVOCATION");
			result = true;
		}
		// store tree locally
		ASTNode tempStore = secondTree;
		// call visitor on subelements
		if(tempStore instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) tempStore;
			
			// check for any subexpression
			Expression expr1 = node.getExpression();
			Expression expr2 = mi.getExpression();
			if(expr1 != null){
				this.secondTree = expr2;
				expr1.accept(this);
			}
			
			// check for the same number of arguments
			if(node.arguments().size() == mi.arguments().size()){
				List list1 = node.arguments();
				List list2 = mi.arguments();
				
				for(int i = 0; i < list1.size(); i++){
					expr1 = (Expression) list1.get(i);
					expr2 = (Expression) list2.get(i);
					if(expr1 != null){
						this.secondTree = expr2;
						expr1.accept(this);
					}
				} // of for-loop	
			}
		}
		// reset locally stored tree
		this.secondTree = tempStore;
		
		// return result
		return result;
	}

	@Override
	public boolean visit(FieldAccess node) {
		if(matcher.match(node, secondTree)){
			System.out.println("FOUND FIELDACCESS");
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public boolean visit(QualifiedName node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		// init result variable
		boolean result = false;
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
				this.secondTree = expr2;
				expr.accept(this);
			}
			// TODO: add exception
		}
		
		// restore the second tree
		this.secondTree = tempStore;
		
		return result;
	}

	@Override
	public boolean visit(ArrayAccess node) {
		// init bool var containing the result
		boolean result = false;
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if(tempStore instanceof ArrayAccess){
			ArrayAccess ac = (ArrayAccess) tempStore;
			
			// check the array property
			Expression expr_array_1 = node.getArray();
			Expression expr_array_2 = ac.getArray();
			if(expr_array_1 != null){
				this.secondTree = expr_array_2;
				expr_array_1.accept(this);
			}
			
			// check index property
			Expression expr_index_1 = node.getIndex();
			Expression expr_index_2 = ac.getIndex();
			if(expr_index_1 != null){
				this.secondTree = expr_index_2;
				expr_index_1.accept(this);
			}
		}

		// restore the second tree
		this.secondTree = tempStore;
		
		return result;
	}

	@Override
	public boolean visit(ArrayCreation node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if (tempStore instanceof ArrayCreation){
			
			ArrayCreation ac = (ArrayCreation) tempStore;
			
			// check each expression of the dimensions (if equal number of dimensions)
			List list1 = node.dimensions();
			List list2 = ac.dimensions();
			
			if(list1.size() == list2.size()){
				
				for(int i = 0; i < list1.size(); i++){
					Expression expr1 = (Expression) list1.get(i);
					Expression expr2 = (Expression) list2.get(i);
					if(expr1 != null){
						this.secondTree = expr2;
						expr1.accept(this);
					}
				} // of for-loop	
			}
		}
		
		// restore the second tree
		this.secondTree = tempStore;
	
		return super.visit(node);
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if(tempStore instanceof ArrayInitializer){
			ArrayInitializer ai = (ArrayInitializer) tempStore;
			
			// check each expression of the expressionList (if equal number of dimensions)
			List list1 = node.expressions();
			List list2 = ai.expressions();
			
			if(list1.size() == list2.size()){
				
				for(int i = 0; i < list1.size(); i++){
					Expression expr1 = (Expression) list1.get(i);
					Expression expr2 = (Expression) list2.get(i);
					if(expr1 != null){
						this.secondTree = expr2;
						expr1.accept(this);
					}
				} // of for-loop	
			}
		}
		
		// restore the second tree
		this.secondTree = tempStore;
		
		return super.visit(node);
	}

	@Override
	public boolean visit(Assignment node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if(tempStore instanceof Assignment){
			Assignment assign = (Assignment) tempStore;
			
			// check the left side of the assignment operator
			Expression expr_left_1 = node.getLeftHandSide();
			Expression expr_left_2 = ((Assignment) tempStore).getLeftHandSide();
			if(expr_left_1 != null){
				this.secondTree = expr_left_2;
				expr_left_1.accept(this);
			}
			
			// check the right side of the assignment operator
			Expression expr_right_1 = node.getRightHandSide();
			Expression expr_right_2 = ((Assignment) tempStore).getRightHandSide();
			if(expr_right_1 != null){
				this.secondTree = expr_right_2;
				expr_right_1.accept(this);
			}
		}
		// restore the second tree
		this.secondTree = tempStore;
		
		return super.visit(node);
	}

	@Override
	public boolean visit(CastExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if(tempStore instanceof CastExpression){
			CastExpression ce = (CastExpression) tempStore;
			
			// check for expression in CastExpression
			Expression expr_1 = node.getExpression();
			Expression expr_2 = ce.getExpression();
			if(expr_1 != null){
				this.secondTree = expr_2;
				expr_1.accept(this);
			}
		}
		
		// restore the second tree
		this.secondTree = tempStore;
		
		return super.visit(node);
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		// restore the second tree
		this.secondTree = tempStore;
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if(tempStore instanceof ConditionalExpression){
			ConditionalExpression ce = (ConditionalExpression) tempStore;
			
			// check condition
			Expression expr_if_1 = node.getExpression();
			Expression expr_if_2 = ce.getExpression();
			if(expr_if_1 != null){
				this.secondTree = expr_if_2;
				expr_if_1.accept(this);
			}
			
			// check then expression
			Expression expr_then_1 = node.getThenExpression();
			Expression expr_then_2 = ce.getThenExpression();
			if(expr_then_1 != null){
				this.secondTree = expr_then_2;
				expr_then_1.accept(this);
			}
			
			// check else expression
			Expression expr_else_1 = node.getElseExpression();
			Expression expr_else_2 = ce.getElseExpression();
			if(expr_else_1 != null){
				this.secondTree = expr_else_2;
				expr_else_1.accept(this);
			}
		}
		
		// restore the second tree
		this.secondTree = tempStore;
		
		return super.visit(node);
	}

	@Override
	public boolean visit(InfixExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		// restore the second tree
		this.secondTree = tempStore;
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		// restore the second tree
		this.secondTree = tempStore;
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		if(tempStore instanceof ParenthesizedExpression){
			ParenthesizedExpression pe = (ParenthesizedExpression) tempStore;
			
			// check the inner expression
			Expression expr_1 = node.getExpression();
			Expression expr_2 = pe.getExpression();
			if(expr_1 != null){
				this.secondTree = expr_2;
				expr_1.accept(this);
			}
			
		}
		
		// restore the second tree
		this.secondTree = tempStore;
		
		return super.visit(node);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		// restore the second tree
		this.secondTree = tempStore;
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		// restore the second tree
		this.secondTree = tempStore;
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		// locally store the second tree
		ASTNode tempStore = this.secondTree;
		
		// restore the second tree
		this.secondTree = tempStore;
		
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		if(matcher.match(node, secondTree)){
			System.out.println("FOUND SIMPLENAME");
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
