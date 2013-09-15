package mutationoperators.methodlevel;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.afro.AFRO;
import mutationoperators.methodlevel.vro.VRO;

import org.junit.Test;



public class AFRO_Test extends MethodTest {

	MutationOperator mutop_afro;
	MutationOperator mutop_vro;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_afro = new AFRO();
		this.addMutationOperatorToTest(mutop_afro);
		this.mutop_vro = new VRO();
		this.addMutationOperatorToTest(mutop_vro);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a1 = 0; " + 
				"boolean b1 = true; " +
				"boolean b2 = false; " +
				"int a2 = 4; " +
				"Bar counter1 = new Bar(); " +
				"Bar counter2 = new Bar(); ";
	}
	
	@Override
	protected void initializeContextFiles() {
		String fileContentBar =  
			"int counter = 0; " +
			"int result = 0; "+ 
			"Bar subclass = null; " +
			"Bar parentclass = null; " +
			"Foo other = null; " + 
			"public void increment(){counter += 1;} " +
			"public int getCounter(){return counter;} ";
		addContextSourceFile("Bar", surroundWithClass("Bar", fileContentBar));
	}

	@Test
	public void testAFRO_LocalReplacement1() {
		String pre 	= "int res = this.a1;";
		String post = "int res = this.a2;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_LocalReplacement2() {
		String pre 	= "int res = this.a1++;";
		String post = "int res = this.a2++;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_LocalReplacement3() {
		String pre 	= "boolean res = !this.b1;";
		String post = "boolean res = !this.b2;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_InObjectReplacement1() {
		String pre 	= "int res = this.counter1.result;";
		String post = "int res = this.counter1.counter;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	@Test
	public void testAFRO_InObjectReplacement2() {
		String pre 	= "Bar res = this.counter1;";
		String post = "Bar res = this.counter2;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_InObjectReplacement3() {
		String pre 	= "Bar res = this.counter1.subclass;";
		String post = "Bar res = this.counter1.parentclass;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_NoMatch1() {
		String pre 	= "Bar res = this.counter1.subclass;";
		String post = "Bar res = this.counter2.subclass;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_NoMatch2() {
		String pre 	= "Bar res = this.counter1.subclass;";
		String post = "Bar res = this.counter2.subclass;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_NoMatch3() {
		String pre 	= "Foo foo = new Foo(); this.counter1.other = foo;";
		String post = "Foo foo = new Foo(); this.counter2.other = foo;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop_afro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAFRO_NoMatch4() {
		String pre 	= "int i = 0; this.a1 = 4;";
		String post = "int i = 0; i = 4;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop_afro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
}
