package mutationoperators;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
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
import org.eclipse.jdt.core.dom.Expression;
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


public abstract class BaseASTVisitor extends ASTVisitor {

	// second AST which will be traversed in parallel
	protected ASTNode secondTree;
	
	// default ASTMatcher to match general properties
	protected ASTMatcher defaultMatcher;
	
	// mutation operator specific matcher
	protected BaseASTMatcher matcher;
	
	public BaseASTVisitor(BaseASTMatcher matcher) {
		this.matcher = matcher;
		this.defaultMatcher = new ASTMatcher();
	}
	
	/////////////////////////////////////////////////////////////
	//
	//	internal methods
	//
	/////////////////////////////////////////////////////////////
	
	public ASTNode getSecondTree(){
		return this.secondTree;
	}
	
	public void setSecondTree(ASTNode ast){
		this.secondTree = ast;
	}
	
	protected abstract void visitSubtree(ASTNode left, ASTNode right);
	
	protected abstract void visitSubtrees(List list1, List list2);
	
	/////////////////////////////////////////////////////////////
	//
	//	visit methods
	//
	/////////////////////////////////////////////////////////////	
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof AnnotationTypeDeclaration){
			// cast tree to correct type
			AnnotationTypeDeclaration atd = (AnnotationTypeDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), atd.getJavadoc());
			
			// visit the annotations name node
			visitSubtree(node.getName(), atd.getName());
		
			// visit each of the sub bodies
			visitSubtrees(node.bodyDeclarations(), atd.bodyDeclarations());
		}
		
		return false;
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof AnnotationTypeMemberDeclaration){
			// cast tree to correct type
			AnnotationTypeMemberDeclaration atmd = (AnnotationTypeMemberDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), atmd.getJavadoc());
			
			// visit the type node
			visitSubtree(node.getType(), atmd.getType());
			
			// visit the annotations name node
			visitSubtree(node.getName(), atmd.getName());
			
			// visit the default value node
			visitSubtree(node.getDefault(), atmd.getDefault());	
		}
		
		return false;
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof AnonymousClassDeclaration){
			// cast tree to correct type
			AnonymousClassDeclaration acd = (AnonymousClassDeclaration) localStoredTree;
			
			// visit each of the body declarations
			visitSubtrees(node.bodyDeclarations(), acd.bodyDeclarations());
		}
		
		return false;
	}

	@Override
	public boolean visit(ArrayAccess node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ArrayAccess){
			// cast tree to correct type
			ArrayAccess aa = (ArrayAccess) localStoredTree;
			
			// visit the array node
			visitSubtree(node.getArray(), aa.getArray());
			
			// visit the index node
			visitSubtree(node.getIndex(), aa.getIndex());
		}
		
		return false;
	}

	@Override
	public boolean visit(ArrayCreation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ArrayCreation){
			// cast tree to correct type
			ArrayCreation ac = (ArrayCreation) localStoredTree;
			
			// visit the type node
			visitSubtree(node.getType(), ac.getType());
			
			// visit the initializer node
			visitSubtree(node.getInitializer(), ac.getInitializer());
			
			// visit each of the dimensions
			visitSubtrees(node.dimensions(), ac.dimensions());
			
		}	
		return false;
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ArrayInitializer){
			// cast tree to correct type
			ArrayInitializer ai = (ArrayInitializer) localStoredTree;
		
			// visit each subexpression
			visitSubtrees(node.expressions(), ai.expressions());
		}
		
		return false;
	}

	@Override
	public boolean visit(ArrayType node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ArrayType){
			// cast tree to correct type
			ArrayType at = (ArrayType) localStoredTree;
			
			// visit the type node
			visitSubtree(node.getElementType(), at.getElementType());
			
			// visit the component type
			visitSubtree(node.getComponentType(), at.getComponentType());
		}
		
		return false;
	}

	@Override
	public boolean visit(AssertStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof AssertStatement){
			// cast tree to correct type
			AssertStatement as = (AssertStatement) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), as.getExpression());
			
			// visit the message node
			visitSubtree(node.getMessage(), as.getMessage());
		}
		return false;
	}

	@Override
	public boolean visit(Assignment node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Assignment){
			Assignment assign = (Assignment) localStoredTree;
			
			// visit the left expression node
			visitSubtree(node.getLeftHandSide(), assign.getLeftHandSide());
			
			// visit the right expression node
			visitSubtree(node.getRightHandSide(), assign.getRightHandSide());
		}
		return false;
	}

	@Override
	public boolean visit(Block node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Block){
			Block block = (Block) localStoredTree;
			
			// visit each statement of the block
			visitSubtrees(node.statements(), block.statements());
		}
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
	public boolean visit(BreakStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof BreakStatement){
			BreakStatement bs = (BreakStatement) localStoredTree;
			
			// visit the label node
			visitSubtree(node.getLabel(), bs.getLabel());
		}
		return false;
	}

	@Override
	public boolean visit(CastExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CastExpression){
			CastExpression ce = (CastExpression) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), ce.getExpression());
			
			// visit the type node
			visitSubtree(node.getType(), ce.getType());
		}
		return false;
	}

	@Override
	public boolean visit(CatchClause node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CatchClause){
			CatchClause cc = (CatchClause) localStoredTree;
			
			// visit the FormalParameter node
			visitSubtree(node.getException(), cc.getException());
			
			// visit the block node
			visitSubtree(node.getBody(), cc.getBody());
		}
		return false;
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		return false;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ClassInstanceCreation){
			ClassInstanceCreation cic = (ClassInstanceCreation) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), cic.getExpression());
			
			// visit the AnonymousClassDeclaration node
			visitSubtree(node.getAnonymousClassDeclaration(), cic.getAnonymousClassDeclaration());
			
			// visit the type node
			visitSubtree(node.getType(), cic.getType());
			
			// visit ich argument node
			visitSubtrees(node.arguments(), cic.arguments());
		}
		return false;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof CompilationUnit){
			CompilationUnit cic = (CompilationUnit) localStoredTree;
			
			// visit the package node
			visitSubtree(node.getPackage(), cic.getPackage());

			// visit all import nodes
			visitSubtrees(node.imports(), cic.imports());
			
			// visit all type nodes
			visitSubtrees(node.types(), cic.types());
		}
		return false;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ConditionalExpression){
			ConditionalExpression ce = (ConditionalExpression) localStoredTree;
			
			// visit the conditional node
			visitSubtree(node.getExpression(), ce.getExpression());
			
			// visit the then node
			visitSubtree(node.getThenExpression(), ce.getThenExpression());
			
			// visit the else node
			visitSubtree(node.getElseExpression(), ce.getElseExpression());
		}
		return false;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ConstructorInvocation){
			ConstructorInvocation ci = (ConstructorInvocation) localStoredTree;
			
			// visit all argument nodes
			visitSubtrees(node.arguments(),  ci.arguments());
		}
		return false;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ContinueStatement){
			ContinueStatement ci = (ContinueStatement) localStoredTree;
			
			// visit the label node
			visitSubtree(node.getLabel(), ci.getLabel());
		}
		return false;
	}

	@Override
	public boolean visit(DoStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof DoStatement){
			DoStatement ds = (DoStatement) localStoredTree;
			
			// visit the body node
			visitSubtree(node.getBody(), ds.getBody());
			
			// visit the expression node
			visitSubtree(node.getExpression(), ds.getExpression());
		}
		return false;
	}

	@Override
	public boolean visit(EmptyStatement node) {
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof EnhancedForStatement){
			EnhancedForStatement efs = (EnhancedForStatement) localStoredTree;
			
			// visit the parameter node
			visitSubtree(node.getParameter(), efs.getParameter());
			
			// visit the expression node
			visitSubtree(node.getExpression(), efs.getExpression());
			
			// visit the body node
			visitSubtree(node.getBody(), efs.getBody());
		}
		return false;
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof EnumConstantDeclaration){
			EnumConstantDeclaration ecd = (EnumConstantDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), ecd.getJavadoc());
			
			// visit the declaration node
			visitSubtree(node.getAnonymousClassDeclaration(), ecd.getAnonymousClassDeclaration());
			
			// visit the name node
			visitSubtree(node.getName(), ecd.getName());
			
			// visit all argument nodes
			visitSubtrees(node.arguments(),  ecd.arguments());
		}
		return false;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof EnumDeclaration){
			EnumDeclaration ed = (EnumDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), ed.getJavadoc());
			
			// visit the name node
			visitSubtree(node.getName(), ed.getName());
			
			// visit all body decleration nodes
			visitSubtrees(node.bodyDeclarations(), ed.bodyDeclarations());
			
			// visit all super interface types nodes
			visitSubtrees(node.superInterfaceTypes(), ed.superInterfaceTypes());
			
			// visit all enum constant decleration nodes
			visitSubtrees(node.enumConstants(), ed.enumConstants());
					
			// visit all modifiers nodes
			visitSubtrees(node.modifiers(), ed.modifiers());
		}
		return false;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ExpressionStatement){
			ExpressionStatement es = (ExpressionStatement) localStoredTree;
			
			// visit the statement node
			visitSubtree(node.getExpression(), es.getExpression());
		}
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof FieldAccess){
			FieldAccess fa = (FieldAccess) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), fa.getExpression());
			
			// visit the name node
			visitSubtree(node.getName(), fa.getName());
		}
		return false;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof FieldDeclaration){
			FieldDeclaration fd = (FieldDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), fd.getJavadoc());
			
			// visit the type node
			visitSubtree(node.getType(), fd.getType());
			
			// visit all fragments nodes
			visitSubtrees(node.fragments(), fd.fragments());
			
			// visit all modifiers nodes
			visitSubtrees(node.modifiers(), fd.modifiers());
		}
		return false;
	}

	@Override
	public boolean visit(ForStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ForStatement){
			ForStatement fs = (ForStatement) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), fs.getExpression());
			
			// visit the body node
			visitSubtree(node.getBody(), fs.getBody());
			
			// visit all updaters nodes
			visitSubtrees(node.updaters(), fs.updaters());

			// visit all initializer nodes
			visitSubtrees(node.initializers(), fs.initializers());
		}
		return false;
	}

	@Override
	public boolean visit(IfStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof IfStatement){
			IfStatement is = (IfStatement) localStoredTree;
		
			// visit the condition node
			visitSubtree(node.getExpression(), is.getExpression());
			
			// visit the then statement node
			visitSubtree(node.getThenStatement(), is.getThenStatement());
			
			// visit the else statement node
			visitSubtree(node.getElseStatement(), is.getElseStatement());
			
		}
		return false;
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ImportDeclaration){
			ImportDeclaration id = (ImportDeclaration) localStoredTree;
			
			// visit the name node
			visitSubtree(node.getName(), id.getName());
		}
		return false;
	}

	@Override
	public boolean visit(InfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof InfixExpression){
			InfixExpression ie = (InfixExpression) localStoredTree;
			
			// visit the left operator
			visitSubtree(node.getLeftOperand(), ie.getLeftOperand());
			
			// visit the right operator
			visitSubtree(node.getRightOperand(), ie.getRightOperand());
			
			// visit the extended expression
			visitSubtrees(node.extendedOperands(), ie.extendedOperands());			
		}
		return false;
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof InstanceofExpression){
			InstanceofExpression id = (InstanceofExpression) localStoredTree;
			
			// visit the left node
			visitSubtree(node.getLeftOperand(), id.getLeftOperand());
			
			// visit the right node
			visitSubtree(node.getRightOperand(), id.getRightOperand());
		}
		return false;
	}

	@Override
	public boolean visit(Initializer node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Initializer){
			Initializer init = (Initializer) localStoredTree;
		
			// visit the block node
			visitSubtree(node.getBody(), init.getBody());
		}
		return false;
	}

	@Override
	public boolean visit(Javadoc node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof Javadoc){
			Javadoc jd = (Javadoc) localStoredTree;
			
			// visit each tag of the javadoc
			visitSubtrees(node.tags(), jd.tags());
		}
		return false;
	}

	@Override
	public boolean visit(LabeledStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof LabeledStatement){
			LabeledStatement ls = (LabeledStatement) localStoredTree;
			
			// visit the label node
			visitSubtree(node.getLabel(), ls.getLabel());
			
			// visit the body node
			visitSubtree(node.getBody(), ls.getBody());
		}
		return false;
	}

	@Override
	public boolean visit(LineComment node) {
		return false;
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MarkerAnnotation){
			MarkerAnnotation ma = (MarkerAnnotation) localStoredTree;
			
			// visit the name node
			visitSubtree(node.getTypeName(), ma.getTypeName());
		}
		return false;
	}

	@Override
	public boolean visit(MemberRef node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MemberRef){
			MemberRef mr = (MemberRef) localStoredTree;
			
			// visit the name node
			visitSubtree(node.getName(), mr.getName());
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), mr.getQualifier());
		}
		return false;
	}

	@Override
	public boolean visit(MemberValuePair node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MemberValuePair){
			MemberValuePair mvp = (MemberValuePair) localStoredTree;
			
			// visit the name node
			visitSubtree(node.getName(), mvp.getName());
			
			// visit the value node 
			visitSubtree(node.getValue(), mvp.getValue());
		}
		return false;
	}

	@Override
	public boolean visit(MethodRef node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodRef){
			MethodRef mr = (MethodRef) localStoredTree;

			//	visit the name node
			visitSubtree(node.getName(), mr.getName());
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), mr.getQualifier());
			
			// visit each parameter of the method ref
			visitSubtrees(node.parameters(), mr.parameters());
		}
		return false;
	}

	@Override
	public boolean visit(MethodRefParameter node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodRefParameter){
			MethodRefParameter mrp = (MethodRefParameter) localStoredTree;
		
			// visit the name node
			visitSubtree(node.getName(), mrp.getName());
			
			// visit the type node
			visitSubtree(node.getType(), mrp.getType());
		}
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodDeclaration){
			MethodDeclaration md = (MethodDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), md.getJavadoc());
			
			// visit the name node
			visitSubtree(node.getName(), md.getName());
			
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
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof MethodInvocation){
			MethodInvocation mi = (MethodInvocation) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), mi.getExpression());;
			
			// visit each type argument
			visitSubtrees(node.typeArguments(), mi.typeArguments());
			
			// visit the name node
			visitSubtree(node.getName(), mi.getName());
			
			// visit each arguments 
			visitSubtrees(node.arguments(), mi.arguments());
		}
		return false;
	}

	@Override
	public boolean visit(Modifier node) {
		return false;
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof NormalAnnotation){
			NormalAnnotation na = (NormalAnnotation) localStoredTree;
			
			// visit the typename node
			visitSubtree(node.getTypeName(), na.getTypeName());
			
			// visit the value nodes
			visitSubtrees(node.values(), na.values());
		}
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
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PackageDeclaration){
			PackageDeclaration pd = (PackageDeclaration) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getJavadoc(), pd.getJavadoc());
			
			// visit the name node
			visitSubtree(node.getName(), pd.getName());
			
			// visit each annotation
			List list1 = node.annotations();
			List list2 = pd.annotations();
			if(list1.size() == list2.size()){
				for(int i=0; i < list1.size(); i++){
					visitSubtree((ASTNode) list1.get(i), (ASTNode) list2.get(i));
				}
			}
		}
		return false;
	}

	@Override
	public boolean visit(ParameterizedType node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ParameterizedType){
			ParameterizedType pt = (ParameterizedType) localStoredTree;
			
			// visit the type node
			visitSubtree(node.getType(), pt.getType());
			
			// visit the argument nodes
			visitSubtrees(node.typeArguments(), pt.typeArguments());
			
		}
		return false;
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ParenthesizedExpression){
			ParenthesizedExpression pe = (ParenthesizedExpression) localStoredTree;
			
			// visit the javadoc node
			visitSubtree(node.getExpression(), pe.getExpression());	
		}
		return false;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PostfixExpression){
			PostfixExpression pe = (PostfixExpression) localStoredTree;
			
			// visit the operand node
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		return false;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof PrefixExpression){
			PrefixExpression pe = (PrefixExpression) localStoredTree;
			
			// visit the operand node
			visitSubtree(node.getOperand(), pe.getOperand());
		}
		return false;
	}

	@Override
	public boolean visit(PrimitiveType node) {
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof QualifiedName){
			QualifiedName qn = (QualifiedName) localStoredTree;
		
			// visit the name node
			visitSubtree(node.getName(), qn.getName());
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), qn.getQualifier());
		}
		return false;
	}

	@Override
	public boolean visit(QualifiedType node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof QualifiedType){
			QualifiedType qt = (QualifiedType) localStoredTree;
		
			// visit the name node
			visitSubtree(node.getName(), qt.getName());
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), qt.getQualifier());
		}
		return false;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ReturnStatement){
			ReturnStatement rs = (ReturnStatement) localStoredTree;
		
			// visit the name node
			visitSubtree(node.getExpression(), rs.getExpression());
		}
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
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SingleMemberAnnotation){
			SingleMemberAnnotation sma = (SingleMemberAnnotation) localStoredTree;
			
			// visit the typename node
			visitSubtree(node.getTypeName(), sma.getTypeName());
			
			// visit the value node
			visitSubtree(node.getValue(), sma.getValue());
		}
		return false;
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SingleVariableDeclaration){
			SingleVariableDeclaration svd = (SingleVariableDeclaration) localStoredTree;
			
			// visit the modifier nodes
			visitSubtrees(node.modifiers(), svd.modifiers());
			
			// visit the name node
			visitSubtree(node.getName(), svd.getName());
			
			// visit the type node
			visitSubtree(node.getType(), svd.getType());
			
			// visit the initializer node
			visitSubtree(node.getInitializer(), svd.getInitializer());
		}
		return false;
	}

	@Override
	public boolean visit(StringLiteral node) {
		return false;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SuperConstructorInvocation){
			SuperConstructorInvocation sci = (SuperConstructorInvocation) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), sci.getExpression());
			
			// visist the typeargument nodes
			visitSubtrees(node.typeArguments(), sci.typeArguments());
			
			// visit the argument nodes
			visitSubtrees(node.arguments(), sci.arguments());
		}
		return false;
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SuperFieldAccess){
			SuperFieldAccess sfa = (SuperFieldAccess) localStoredTree;
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), sfa.getQualifier());
			
			// visit the name node
			visitSubtree(node.getName(), sfa.getName());
		}
		return false;
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SuperMethodInvocation){
			SuperMethodInvocation smi = (SuperMethodInvocation) localStoredTree;
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), smi.getQualifier());
			
			// visit the typeArgument nodes
			visitSubtrees(node.typeArguments(), smi.typeArguments());
			
			// visit the name node
			visitSubtree(node.getName(), smi.getName());
			
			// visit the argument nodes
			visitSubtrees(node.arguments(), smi.arguments());
		}
		return false;
	}

	@Override
	public boolean visit(SwitchCase node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SwitchCase){
			SwitchCase sc = (SwitchCase) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), sc.getExpression());
		}
		return false;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SwitchStatement){
			SwitchStatement ss = (SwitchStatement) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), ss.getExpression());
			
			// visit the statement nodes
			visitSubtrees(node.statements(), ss.statements());
		}
		return false;
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof SynchronizedStatement){
			SynchronizedStatement ss = (SynchronizedStatement) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), ss.getExpression());
			
			// visit the bode node
			visitSubtree(node.getBody(), ss.getBody());
		}
		return false;
	}

	@Override
	public boolean visit(TagElement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof TagElement){
			TagElement te = (TagElement) localStoredTree;
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), te.fragments());
		}
		return false;
	}

	@Override
	public boolean visit(TextElement node) {
		return false;
	}

	@Override
	public boolean visit(ThisExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ThisExpression){
			ThisExpression ts = (ThisExpression) localStoredTree;
			
			// visit the qualifier node
			visitSubtree(node.getQualifier(), ts.getQualifier());
		}
		return false;
	}

	@Override
	public boolean visit(ThrowStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof ThrowStatement){
			ThrowStatement ts = (ThrowStatement) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), ts.getExpression());
		}
		return false;
	}

	@Override
	public boolean visit(TryStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof TryStatement){
			TryStatement ts = (TryStatement) localStoredTree;
			
			// visit the resource nodes
			visitSubtrees(node.resources(), ts.resources());
			
			// visit the body expression
			visitSubtree(node.getBody(), ts.getBody());
			
			// visit the catch clause nodes
			visitSubtrees(node.catchClauses(), ts.catchClauses());
			
			// visit the finally node
			visitSubtree(node.getFinally(), ts.getFinally());
		}
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof TypeDeclaration){
			TypeDeclaration td = (TypeDeclaration) localStoredTree;
			
			if (node.getAST().apiLevel() >= AST.JLS3) {
				// visit the javadoc node
				visitSubtree(node.getJavadoc(), td.getJavadoc());
				// visit the modifier nodes
				visitSubtrees(node.modifiers(), td.modifiers());
				// visit the name node
				visitSubtree(node.getName(), td.getName());
				// visit the typeparameter nodes
				visitSubtrees(node.typeParameters(), td.typeParameters());
				// visit the superclasstype node
				visitSubtree(node.getSuperclassType(), td.getSuperclassType());
				// visist the superinterface types
				visitSubtrees(node.superInterfaceTypes(), td.superInterfaceTypes());
				// visit the body declaration nodes
				visitSubtrees(node.bodyDeclarations(), td.bodyDeclarations());
			}
		}
		return false;
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof TypeDeclarationStatement){
			TypeDeclarationStatement tds = (TypeDeclarationStatement) localStoredTree;

			// visit the declaration node
			visitSubtree(node.getDeclaration(), tds.getDeclaration());
			
			// visit the typedelcaration node
			visitSubtree(node.getTypeDeclaration(), tds.getTypeDeclaration());
		}
		return false;
	}

	@Override
	public boolean visit(TypeLiteral node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof TypeLiteral){
			TypeLiteral tl = (TypeLiteral) localStoredTree;

			// visit the expression node
			visitSubtree(node.getType(), tl.getType());
		}
		return false;
	}

	@Override
	public boolean visit(TypeParameter node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof TypeParameter){
			TypeParameter tp = (TypeParameter) localStoredTree;
			
			// visit the name node
			visitSubtree(node.getName(), tp.getName());
			
			// visit the typebound node
			visitSubtrees(node.typeBounds(), tp.typeBounds());
		}
		return false;
	}

	@Override
	public boolean visit(UnionType node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof UnionType){
			UnionType ut = (UnionType) localStoredTree;
			
			// visit the type nodes
			visitSubtrees(node.types(), ut.types());
		}
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof VariableDeclarationExpression){
			VariableDeclarationExpression vde = (VariableDeclarationExpression) localStoredTree;
			
			// visit the modifier nodes
			visitSubtrees(node.modifiers(), vde.modifiers());
			
			// visit the type node
			visitSubtree(node.getType(), vde.getType());
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), vde.fragments());
		}
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof VariableDeclarationStatement){
			VariableDeclarationStatement vds = (VariableDeclarationStatement) localStoredTree;
			
			// visit the modifier nodes
			visitSubtrees(node.modifiers(), vds.modifiers());
			
			// visit the type node
			visitSubtree(node.getType(), vds.getType());
			
			// visit the fragement nodes
			visitSubtrees(node.fragments(), vds.fragments());
		}
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof VariableDeclarationFragment){
			VariableDeclarationFragment vdf = (VariableDeclarationFragment) localStoredTree;
			
			// visit the name node
			visitSubtree(node.getName(), vdf.getName());
			
			// visit the initializer node
			visitSubtree(node.getInitializer(), vdf.getInitializer());
		}
		return false;
	}

	@Override
	public boolean visit(WhileStatement node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof WhileStatement){
			WhileStatement ws = (WhileStatement) localStoredTree;
			
			// visit the expression node
			visitSubtree(node.getExpression(), ws.getExpression());
			
			// visit the body node
			visitSubtree(node.getBody(), ws.getBody());
		}
		return false;
	}

	@Override
	public boolean visit(WildcardType node) {
		// locally store the AST
		ASTNode localStoredTree = getSecondTree();
		
		// check for same node type in parallel tree
		if(localStoredTree instanceof WildcardType){
			WildcardType wt = (WildcardType) localStoredTree;
			
			// visit the bound node
			visitSubtree(node.getBound(), wt.getBound());
		}
		return false;
	}

	
	
	
}
