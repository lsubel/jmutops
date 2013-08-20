package utils;

import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

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
	public void endVisit(AssertStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}
	
	@Override
	public void endVisit(Block node) {
		List stmts = node.statements();
		for(int i = 0; i < stmts.size(); i++) {
			if(this.child.subtreeMatch(defaultMatcher, stmts.get(i))) {
				stack.push(new Integer(i));
				this.child = node;
				return;
			}
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(BreakStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getLabel())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}
	
	@Override
	public void endVisit(ContinueStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getLabel())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}
	
	@Override
	public void endVisit(DoStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(EmptyStatement node) {
		return;
	}

	@Override
	public void endVisit(EnhancedForStatement node) { 
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(ExpressionStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(ForStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(IfStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getThenStatement())) {
			stack.push(1);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getElseStatement())) {
			stack.push(2);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(LabeledStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getLabel())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(ReturnStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(SwitchStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node; 
			return;
		}
		List stmts = node.statements();
		for(int i = 0; i < stmts.size(); i++) {
			if(this.child.subtreeMatch(defaultMatcher, stmts.get(i))) {
				stack.push(new Integer(1 + i));
				this.child = node;
				return;
			}
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(SynchronizedStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(ThrowStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(TryStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getBody() )) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getFinally())) {
			stack.push(1);
			this.child = node;
			return;
		}
		List stmts = node.catchClauses();
		for(int i = 0; i < stmts.size(); i++) {
			if(this.child.subtreeMatch(defaultMatcher, stmts.get(i))) {
				stack.push(new Integer(2 + i));
				this.child = node;
				return;
			}
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(TypeDeclarationStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getDeclaration())) {
			stack.push(0);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(VariableDeclarationStatement node) {
		stack.push(new Integer(-1));
		this.child = node;
	}

	@Override
	public void endVisit(WhileStatement node) {
		if(this.child.subtreeMatch(defaultMatcher, node.getExpression())) {
			stack.push(0);
			this.child = node;
			return;
		}
		if(this.child.subtreeMatch(defaultMatcher, node.getBody())) {
			stack.push(1);
			this.child = node;
			return;
		}
		stack.push(new Integer(-1));
		this.child = node;
	}
}
