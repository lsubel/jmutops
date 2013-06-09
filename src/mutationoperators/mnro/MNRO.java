package mutationoperators.mnro;

import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class MNRO extends MutationOperator {

	public MNRO(MutationOperatorChecker checker) {
		super(checker, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new MNRO_Matcher(this);
		this.visitor = new MNRO_Visitor(this.matcher);
	}
}
