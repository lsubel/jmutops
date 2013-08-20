package utils;

import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

public class PathExtractor extends ASTVisitor {

	public static Stack<Integer> calculatePathToMethod(ASTNode node) {
		
		PathExtractor pe = new PathExtractor(node);
		node.getParent().accept(pe);
		
		return pe.stack;
	}
	
	private Stack<Integer> stack = new Stack<Integer>();
	
	private ASTMatcher defaultMatcher = new ASTMatcher();
	
	private ASTNode child = null;
	
	public PathExtractor(ASTNode node) {
		child = node;
	}

	@Override
	public boolean visit(AssertStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;	
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}
	
	@Override
	public boolean visit(Block node) {
		List stmts = node.statements();
		boolean found = false;
		for(int i = 0; i < stmts.size(); i++) {
			if(this.child.subtreeMatch(defaultMatcher, stmts.get(i))) {
				stack.push(new Integer(i));
				this.child = node;
				found = true;
				break;
			}
		}
		if(!found) {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(BreakStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getLabel())) {
			stack.push(0);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}
	
	@Override
	public boolean visit(ContinueStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getLabel())) {
			stack.push(0);
			this.child = node;
		}
		else{
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}
	
	@Override
	public boolean visit(DoStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(EmptyStatement node) {
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement node) { 
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(ForStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(IfStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getThenStatement())) {
			stack.push(1);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getElseStatement())) {
			stack.push(2);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(LabeledStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getLabel())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		boolean found = false;
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node; 
			found = true;
		}
		else {
			List stmts = node.statements();
			for(int i = 0; i < stmts.size(); i++) {
				if(this.child.subtreeMatch(defaultMatcher, stmts.get(i))) {
					stack.push(new Integer(1 + i));
					this.child = node;
					found = true;
				}
			}
		}
		if(!found) {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(ThrowStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(TryStatement node) {
		boolean found = false;
		if(this.child.subtreeMatch(defaultMatcher, node.getBody() )) {
			stack.push(0);
			this.child = node;
			found = true;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getFinally())) {
			stack.push(1);
			this.child = node;
			found = true;
		}
		else{ 
			List stmts = node.catchClauses();
			for(int i = 0; i < stmts.size(); i++) {
				if(this.child.subtreeMatch(defaultMatcher, stmts.get(i))) {
					stack.push(new Integer(2 + i));
					this.child = node;
					found = true;
					break;
				}
			}
		}
		if(!found) {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getDeclaration())) {
			stack.push(0);
			this.child = node;
			
		}
		else{
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		stack.push(new Integer(-1));
		this.child = node;
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(WhileStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
		}
		else if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
		}
		else {
			stack.push(new Integer(-1));
			this.child = node;
		}
		if(node.getParent() != null) {
			node.getParent().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(ArrayAccess node) {
		return false;
	}

	@Override
	public boolean visit(ArrayCreation node) {
		return false;
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		return false;
	}

	@Override
	public boolean visit(ArrayType node) {
		return false;
	}

	@Override
	public boolean visit(Assignment node) {
		return false;
	}

	@Override
	public boolean visit(BlockComment node) {
		return false;
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		return false;
	}

	@Override
	public boolean visit(CastExpression node) {
		return false;
	}

	@Override
	public boolean visit(CatchClause node) {
		return false;
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		return false;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		return false;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		return false;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		return false;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		return false;
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		return false;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(InfixExpression node) {
		return false;
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		return false;
	}

	@Override
	public boolean visit(Initializer node) {
		return false;
	}

	@Override
	public boolean visit(Javadoc node) {
		return false;
	}

	@Override
	public boolean visit(LineComment node) {
		return false;
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		return false;
	}

	@Override
	public boolean visit(MemberRef node) {
		return false;
	}

	@Override
	public boolean visit(MemberValuePair node) {
		return false;
	}

	@Override
	public boolean visit(MethodRef node) {
		return false;
	}

	@Override
	public boolean visit(MethodRefParameter node) {
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		return false;
	}

	@Override
	public boolean visit(Modifier node) {
		return false;
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		return false;
	}

	@Override
	public boolean visit(NullLiteral node) {
		return false;
	}

	@Override
	public boolean visit(NumberLiteral node) {
		return false;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(ParameterizedType node) {
		return false;
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		return false;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		return false;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		return false;
	}

	@Override
	public boolean visit(PrimitiveType node) {
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		return false;
	}

	@Override
	public boolean visit(QualifiedType node) {
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		return false;
	}

	@Override
	public boolean visit(SimpleType node) {
		return false;
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		return false;
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(StringLiteral node) {
		return false;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		return false;
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		return false;
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		return false;
	}

	@Override
	public boolean visit(SwitchCase node) {
		return false;
	}

	@Override
	public boolean visit(TagElement node) {
		return false;
	}

	@Override
	public boolean visit(TextElement node) {
		return false;
	}

	@Override
	public boolean visit(ThisExpression node) {
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(TypeLiteral node) {
		return false;
	}

	@Override
	public boolean visit(TypeParameter node) {
		return false;
	}

	@Override
	public boolean visit(UnionType node) {
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		return false;
	}

	@Override
	public boolean visit(WildcardType node) {
		return false;
	}
	
	
}
