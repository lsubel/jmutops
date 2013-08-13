package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperator.methodlevel.exco.EXCO_Insert;

import org.junit.Test;

import utils.MethodTest;

public class EXCO_Insert_Test extends MethodTest {

	private MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EXCO_Insert();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 1; " +
				"int b = 2; ";
	}
	
	@Test
	public void testEXCO_addNewCatch1(){
		String pre 	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		String post	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");} catch (IllegalStateException e){System.out.println(\"IllegalStateException\");}";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
