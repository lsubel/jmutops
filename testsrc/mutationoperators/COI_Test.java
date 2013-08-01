package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.coi.COI;

import org.junit.Test;

import utils.MethodTest;

public class COI_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new COI();
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
	public void testCOI_constantComparision1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (true && false); System.out.println();", "boolean result = !(true && false); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_constantComparision2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (true & false); System.out.println();", "boolean result = !(true & false); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_constantComparision3(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (false | false); System.out.println();", "boolean result = !(false | false); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_constantComparision4(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (true || true); System.out.println();", "boolean result = !(true || true); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_constantComparision5(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (true ^ true); System.out.println();", "boolean result = !(true ^ true); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_negateConstant1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = true; System.out.println();", "boolean result = !true; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}

	@Test
	public void testCOI_negateConstant2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = false; System.out.println();", "boolean result = !false; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_multiple1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = b1 || b2; System.out.println();", "boolean result = !b1 || !b2; System.out.println();");
		assertEquals(2, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testCOI_multiple2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = ((b1)); System.out.println();", "boolean result = !(!(b1)); System.out.println();");
		assertEquals(2, resultMap.get(mutop).intValue());
	}
}
