package mutationoperators;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperators.methodlevel.afro.AFRO;

import org.junit.Test;

import utils.MethodTest;


public class AFRO_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new AFRO();
		this.addMutationOperatorToTest(mutop);
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
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int res = this.a1;", "int res = this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_LocalReplacement2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int res = this.a1++;", "int res = this.a2++;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_LocalReplacement3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("boolean res = !this.b1;", "int res = !this.b2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_InObjectReplacement1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int res = this.counter1.result;", "int res = this.counter1.counter;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	@Test
	public void testAFRO_InObjectReplacement2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Bar res = this.counter1;", "Bar res = this.counter2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_InObjectReplacement3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Bar res = this.counter1.subclass;", "Bar res = this.counter1.parentclass;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_NoMatch1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Bar res = this.counter1.subclass;", "Bar res = this.counter2.subclass;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_NoMatch2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Bar res = this.counter1.subclass;", "Bar res = this.counter2.subclass;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_NoMatch3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo foo = new Foo(); this.counter1.other = foo;", "Foo foo = new Foo(); this.counter2.other = foo;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAFRO_NoMatch4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int i = 0; this.a1 = 4;", "int i = 0; i = 4;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
