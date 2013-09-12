package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.lor.LOR;

import org.junit.Test;

import utils.MethodTest;

public class LOR_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new LOR();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"boolean a = true; " +
				"boolean b = false;" +
				"boolean c = false;" +
				"boolean d = true;";
	}
	
	@Test
	public void testLOR_constantComparision1(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = (true & false); System.out.println();", "boolean result = (true | false); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testLOR_constantComparision2(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = (true ^ false); System.out.println();", "boolean result = (true & false); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOR_constantComparision3(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = (false | false); System.out.println();", "boolean result = (false ^ false); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOR_multipleConstantComparision1(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = ((true ^ false) | true); System.out.println();", "boolean result = ((true & false) | true); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOR_multipleConstantComparision2(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = ((true & false) | (true ^ false)); System.out.println();", "boolean result = ((true | false) | (true & false)); System.out.println();");
		assertEquals(2, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOR_fieldComparision1(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = ((a | b) | (c | d)); System.out.println();", "boolean result = ((a & b) ^ (c & d)); System.out.println();");
		assertEquals(3, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOR_fieldComparision2(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = ((a & b) & c); System.out.println();", "boolean result = ((a ^ b) & c); System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOR_fieldComparision3(){
		HashMap<String, Integer> resultMap = compareMatches("boolean result = ((a ^ b) | c); System.out.println();", "boolean result = ((a | b) & c); System.out.println();");
		assertEquals(2, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
}
