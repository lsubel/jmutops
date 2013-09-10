package utils;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;

public class LiteralValueExtractor extends ASTVisitor {
	
	boolean isNegative;
	double value;
	
	public LiteralValueExtractor() {
		this.isNegative = false;
		this.value 		= 0;
	}
	
	
	public static double getValue(ASTNode node) {
		// set value
		LiteralValueExtractor lve = new LiteralValueExtractor();
		node.accept(lve);
		return lve.getValue();
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		// store the value
		this.value = Double.parseDouble(node.getToken());
		// so we dont have to walk deeper into the AST
		return false;
	}


	@Override
	public boolean visit(PrefixExpression node) {
		if(node.getOperator().equals(PrefixExpression.Operator.MINUS)) {
			this.isNegative = !(this.isNegative);
			return true;
		}
		else if(node.getOperator().equals(PrefixExpression.Operator.PLUS)) {
			return true;
		}
		return false;
	}


	private double getValue() {
		if(isNegative) {
			return -this.value;
		}
		else {
			return this.value;
		}
	}
		
	
}
