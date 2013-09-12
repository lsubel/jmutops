package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.cod.COD;

import org.junit.Test;

import utils.MethodTest;

public class COD_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new COD();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a1 = 0; " +
				"int a2 = 9; " +
				"boolean b1 = true; " +
				"boolean b2 = false; ";
	}

	@Test
	public void testCOD_constantComparision1(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !(true && false); System.out.println();", "boolean result = (true && false); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_constantComparision2(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !(true & false); System.out.println();", "boolean result = (true & false); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_constantComparision3(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !(false | false); System.out.println();", "boolean result = (false | false); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_constantComparision4(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !(true || true); System.out.println();", "boolean result = (true || true); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_constantComparision5(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !(true ^ true); System.out.println();", "boolean result = (true ^ true); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_negateConstant1(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !true; System.out.println();", "boolean result = true; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testCOD_negateConstant2(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !false; System.out.println();", "boolean result = false; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_multiple1(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !b1 || !b2; System.out.println();", "boolean result = b1 || b2; System.out.println();");
		assertEquals(2, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCOD_multiple2(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = !(!(b1)); System.out.println();", "boolean result = ((b1)); System.out.println();");
		assertEquals(2, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
