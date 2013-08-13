package mutationoperators.methodlevel.afro;

import mutationoperators.TwoASTMatcher;
import mutationoperators.TwoASTVisitor;

import org.eclipse.jdt.core.dom.FieldAccess;

public class AFRO_Visitor extends TwoASTVisitor {

	public AFRO_Visitor(TwoASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(FieldAccess node) {
		matcher.match(node, parallelTree);
		return false;
	}
}
