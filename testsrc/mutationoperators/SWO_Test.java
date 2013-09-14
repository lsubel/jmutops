package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.swo.SWO;

import org.junit.Test;

import utils.MethodTest;

public class SWO_Test extends MethodTest {

MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new SWO();
		this.addMutationOperatorToTest(mutop);
	}
	
	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0;" +
				"int b = 64;";
	}
	
	@Test
	public void testSWO_switch1() {
		String pre = "switch(a){case 1: b += 1; System.out.println(1); break; case 0: b += 0; System.out.println(0); break; default: break;}";
		String post = "switch(a){case 1: b += 0; System.out.println(0); break; case 0: b += 1; System.out.println(1); break; default: break;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSWO_if1() {
		String pre =  "int result; if(a >= 0) {result = a; System.out.println(\"Value is positive\");}else {result = - a; System.out.println(\"Value is negative\");} System.out.println(\"Finished method.\");";
		String post = "int result; if(a >= 0) {result = -a; System.out.println(\"Value is negative\");}else {result = a; System.out.println(\"Value is positive\");} System.out.println(\"Finished method.\");";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
