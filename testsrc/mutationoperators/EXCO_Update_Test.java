package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.exco.EXCO_Update;
import mutationoperators.methodlevel.tro.TRO_Methodlevel;

import org.junit.Test;

import utils.MethodTest;

public class EXCO_Update_Test extends MethodTest {
	
	private MutationOperator mutop_exco;
	private MutationOperator mutop_tro;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_exco = new EXCO_Update();
		this.addMutationOperatorToTest(mutop_exco);
		this.mutop_tro = new TRO_Methodlevel();
		this.addMutationOperatorToTest(mutop_tro);
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
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_exco));
		assertEquals(1, getApplicationValue(resultMap, mutop_tro));
		checkOtherMutationOperators(resultMap);
	}
}
