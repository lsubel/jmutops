package mutationoperators.afro;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

import org.eclipse.jdt.core.dom.FieldAccess;

public class AFRO_Visitor extends BaseASTVisitor {

	public AFRO_Visitor(BaseASTMatcher matcher) {
		super(matcher);
	}

	@Override
	public boolean visit(FieldAccess node) {
		matcher.match(node, parallelTree);
		return false;
	}
}
