package mutationoperators.mnro;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;
import mutationoperators.MutationOperatorChecker;

public class MNRO extends MutationOperator {

	public static final String fullname = "Method name replacement operator";
	
	public MNRO(ResultListenerMulticaster eventListener) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new MNRO_Matcher(this);
		this.visitor = new MNRO_Visitor(this.matcher);
	}
	
	@Override
	public String getFullname() {
		return this.fullname;
	}
}
