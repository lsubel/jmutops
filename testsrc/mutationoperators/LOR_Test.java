package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.cor.COR;
import mutationoperators.methodlevel.lor.LOR;

import org.junit.Test;

import utils.MethodTest;

public class LOR_Test extends MethodTest {

	MutationOperator mutop_lor;
	MutationOperator mutop_cor;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_lor = new LOR();
		this.addMutationOperatorToTest(mutop_lor);
		this.mutop_cor = new COR();
		this.addMutationOperatorToTest(mutop_cor);
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
		String pre 	= "boolean result = (true & false); System.out.println();";
		String post = "boolean result = (true | false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testLOR_constantComparision2(){
		String pre 	= "boolean result = (true ^ false); System.out.println();";
		String post = "boolean result = (true & false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLOR_constantComparision3(){
		String pre 	= "boolean result = (false | false); System.out.println();";
		String post = "boolean result = (false ^ false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLOR_multipleConstantComparision1(){
		String pre 	= "boolean result = ((true ^ false) | true); System.out.println();";
		String post = "boolean result = ((true & false) | true); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLOR_multipleConstantComparision2(){
		String pre 	= "boolean result = ((true & false) | (true ^ false)); System.out.println();";
		String post = "boolean result = ((true | false) | (true & false)); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_lor));
		assertEquals(2, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLOR_fieldComparision1(){
		String pre 	= "boolean result = ((a | b) | (c | d)); System.out.println();";
		String post = "boolean result = ((a & b) ^ (c & d)); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(3, getApplicationValue(resultMap, mutop_lor));
		assertEquals(3, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLOR_fieldComparision2(){
		String pre 	= "boolean result = ((a & b) & c); System.out.println();";
		String post = "boolean result = ((a ^ b) & c); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLOR_fieldComparision3(){
		String pre 	= "boolean result = ((a ^ b) | c); System.out.println();";
		String post = "boolean result = ((a | b) & c); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_lor));
		assertEquals(2, getApplicationValue(resultMap, mutop_cor));
		checkOtherMutationOperators(resultMap);
	}
	
}
