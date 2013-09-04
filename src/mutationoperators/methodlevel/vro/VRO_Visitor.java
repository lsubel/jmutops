package mutationoperators.methodlevel.vro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;
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

	
	
}
