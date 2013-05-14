package mutationoperators.mnro;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

import mutationoperators.BaseASTMatcher;
import mutationoperators.BaseASTVisitor;

public class MNRO_Visitor extends BaseASTVisitor {

	public MNRO_Visitor(BaseASTMatcher matcher, ASTNode secondTree) {
		super(matcher, secondTree);
	}

	@Override
	protected void visitSubtree(ASTNode left, ASTNode right) {
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

	@Override
	public boolean visit(MethodInvocation node) {

		matcher.match(node, this.secondTree);
		
		return super.visit(node);
	}

	
	
}
