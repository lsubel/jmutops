package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.exco.EXCO_Update;

import org.junit.Test;

import utils.MethodTest;

public class EXCO_Update_Test extends MethodTest {
	
	private MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EXCO_Update();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 1; " +
				"int b = 2; ";
	}
	
	@Test
	public void testEXCO_replaceExceptionType1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (Exception e) {System.out.println(\"NullPointerException\");}";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
