package mutationoperators.lco;

import results.ResultListenerMulticaster;
import enums.MutationOperatorCategory;
import mutationoperators.MutationOperator;

public class LCO extends MutationOperator {

	public LCO(ResultListenerMulticaster eventListener, MutationOperatorCategory category) {
		super(eventListener, MutationOperatorCategory.METHOD_LEVEL);
		this.matcher = new LCO_Matcher(this);
		this.visitor = new LCO_Visitor(this.matcher);
	}

	public static final String fullname = "Literal change operator";
	
	@Override
	public String getFullname() {
		return this.fullname;
	}

}
