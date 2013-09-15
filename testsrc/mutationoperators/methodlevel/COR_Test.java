package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.cor.COR;
import mutationoperators.methodlevel.lor.LOR;

import org.junit.Test;


public class COR_Test extends MethodTest {

	MutationOperator mutop_cor;
	MutationOperator mutop_lor;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_cor = new COR();
		this.addMutationOperatorToTest(mutop_cor);
		this.mutop_lor = new LOR();
		this.addMutationOperatorToTest(mutop_lor);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"boolean a = true; " +
				"boolean b = false;" +
				"boolean c = false;" +
				"boolean d = true;";
	}
	
	@Test
	public void testCOR_constantComparision1(){
		String pre  = "boolean result = (true && false); System.out.println();";
		String post = "boolean result = (true || false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testCOR_constantComparision2(){
		String pre	= "boolean result = (true & false); System.out.println();";
		String post = "boolean result = (true | false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_constantComparision3(){
		String pre	= "boolean result = (false & false); System.out.println();";
		String post	= "boolean result = (false && false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_constantComparision4(){
		String pre	= "boolean result = (true || true); System.out.println();";
		String post = "boolean result = (true | true); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_multipleConstantComparision1(){
		String pre	= "boolean result = ((true && false) | true); System.out.println();";
		String post = "boolean result = ((true || false) | true); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_multipleConstantComparision2(){
		String pre	= "boolean result = ((true && false) | (true && false)); System.out.println();";
		String post = "boolean result = ((true || false) | (true || false)); System.out.println();";	
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_multipleConstantComparision3(){
		String pre	= "boolean result = ((true && false) | (true && false)); System.out.println();";
		String post = "boolean result = ((true & false) && (true & false)); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(3, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_multipleChanges1(){
		String pre	= "boolean result = ((true && false) | (true && false)); System.out.println();";
		String post = "boolean result = ((true || false) | false); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_fieldComparision1(){
		String pre	= "boolean result = ((a && b) | (c && d)); System.out.println();";
		String post = "boolean result = ((a || b) | (c || d)); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_fieldComparision2(){
		String pre	= "boolean result = ((a & b) & c); System.out.println();";
		String post = "boolean result = ((a | b) & c); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cor));
		assertEquals(1, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCOR_fieldComparision3(){
		String pre	= "boolean result = ((a || b) | c); System.out.println();";
		String post = "boolean result = ((a | b) || c); System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_cor));
		assertEquals(0, getApplicationValue(resultMap, mutop_lor));
		checkOtherMutationOperators(resultMap);
	}
	
}
