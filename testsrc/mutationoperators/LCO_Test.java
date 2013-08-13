package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.lco.LCO;

import org.junit.Test;

import utils.MethodTest;

public class LCO_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new LCO();
		this.addMutationOperatorToTest(mutop);
	}
	@Override
	protected String getOtherClassContent() {
		return "boolean b;";
	}
	
	@Test
	public void testLCO_TrueToFalse1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); this.b = true;", "System.out.println(); this.b = false;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_TrueToFalse2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("if(true){System.out.println(b);};", "if(false){System.out.println(b);};");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_TrueToFalse3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("while(true){this.b = true; this.b = false;}", "while(false){this.b = true; this.b = false;}");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_FalseToTrue1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); this.b = false;", "System.out.println(); this.b = true;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_FalseToTrue2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("if(false){System.out.println(42);};", "if(true){System.out.println(42);};");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_FalseToTrue3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("while(false){this.b = true; this.b = false;}", "while(true){this.b = true; this.b = false;}");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_NoChange1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = true;", "b = !true;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_NoChange2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = false;", "b = !false;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLCO_NoChange3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = false;", "b = !true;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
