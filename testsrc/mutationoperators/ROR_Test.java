package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperators.methodlevel.ror.ROR;

import org.junit.Test;

import utils.MethodTest;

public class ROR_Test extends MethodTest {
	
	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new ROR();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a1 = 42; " +
				"int a2 = 1337; " +
				"boolean b = false;";
	}
	
	@Test
	public void testROR_constantComparision1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (42 < 42); System.out.println();", "boolean result = (42 != 42); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testROR_constantComparision2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (3 > 1); System.out.println();", "boolean result = (3 >= 1); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_constantComparision3(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (5 != 4); System.out.println();", "boolean result = (5 <= 4); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_fieldComparision1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = (a1 != a2); System.out.println();", "this.b = (a1 <= a2); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_fieldComparision2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (a1 < a2); System.out.println();", "boolean result = (a1 != a2); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testROR_fieldComparision3(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean result = (a1 > a2); System.out.println();", "boolean result = (a1 >= a2); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_ifComparision1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("if(a1 < a2){System.out.println();}", "if(a1 == a2){System.out.println();}");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_ifComparision2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("if(a1 != a2){System.out.println();}", "if(a1 >= a2){System.out.println();}");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_whileComparision1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("while(a1 < a2){System.out.println();}", "while(a1 == a2){System.out.println();}");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testROR_whileComparision2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("while(a1 != a2){System.out.println();}", "while(a1 >= a2){System.out.println();}");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
