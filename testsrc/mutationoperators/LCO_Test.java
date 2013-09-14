package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.icm.ICM;
import mutationoperators.methodlevel.lco.LCO;

import org.junit.Test;

import utils.MethodTest;

public class LCO_Test extends MethodTest {

	MutationOperator mutop_lco;
	MutationOperator mutop_icm;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_lco = new LCO();
		this.addMutationOperatorToTest(mutop_lco);
		this.mutop_icm = new ICM();
		this.addMutationOperatorToTest(mutop_icm);
	}
	@Override
	protected String getOtherClassContent() {
		return "boolean b;";
	}
	
	@Test
	public void testLCO_TrueToFalse1() {
		String pre 	= "System.out.println(); this.b = true;";
		String post = "System.out.println(); this.b = false;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lco));
		assertEquals(1, getApplicationValue(resultMap, mutop_icm));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLCO_TrueToFalse2() {
		String pre 	= "if(true){System.out.println(b);};";
		String post = "if(false){System.out.println(b);};";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lco));
		assertEquals(0, getApplicationValue(resultMap, mutop_icm));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLCO_TrueToFalse3() {
		String pre 	= "while(true){this.b = true; this.b = false;}";
		String post = "while(false){this.b = true; this.b = false;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lco));
		assertEquals(0, getApplicationValue(resultMap, mutop_icm));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLCO_FalseToTrue1() {
		String pre 	= "System.out.println(); this.b = false;";
		String post = "System.out.println(); this.b = true;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lco));
		assertEquals(1, getApplicationValue(resultMap, mutop_icm));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLCO_FalseToTrue2() {
		String pre 	= "if(false){System.out.println(42);};";
		String post = "if(true){System.out.println(42);};";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lco));
		assertEquals(0, getApplicationValue(resultMap, mutop_icm));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testLCO_FalseToTrue3() {
		String pre 	= "while(false){this.b = true; this.b = false;}";
		String post = "while(true){this.b = true; this.b = false;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_lco));
		assertEquals(0, getApplicationValue(resultMap, mutop_icm));
		checkOtherMutationOperators(resultMap);
	}
}
