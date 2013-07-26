package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.aco.ACO;

import org.junit.Test;

import utils.MethodTest;

public class ACO_Test extends MethodTest {
	
	MutationOperator mutop;
	
	@Override
	protected String getOtherClassContent() {
		return  "int value; " + 
				"public Foo(){value = 0;} " +
				"public Foo(int v){value = v;} " +
				"public Foo(int v1, int v2){value = v1 + v2;} " +
				"public int getValue(){return value;} " +
				"public void setValue(int v){value = v;} " +
				"public void setValue(int v1, int v2){value = v1 + v2;} " +
				"public void setValue(Foo f1){value = f1.getValue();} " +
				"public void setValue(Foo f1, Foo2){value = f1.getValue() + f2.getValue();} ";
	}

	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new ACO();
		this.addMutationOperatorToTest(mutop);
	}
	
	@Test
	public void testAFRO_MethodNumberChanged1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("setValue(1);", "setValue(1, 2);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testAFRO_MethodNumberChanged2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f1, f2);", "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f2);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}

	@Test
	public void testAFRO_ConstructorNumberChanged1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(0, 4);", "Foo f1 = new Foo();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}

	@Test
	public void testAFRO_ConstructorNumberChanged2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(0);", "Foo f1 = new Foo(6, 1);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testAFRO_ConstructorReordering1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(42, 0);", "Foo f1 = new Foo(0, 42);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testAFRO_MethodReordering1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f1, f2);", "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f2, f1);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testAFRO_MethodReordering2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("setValue(2, 1);", "setValue(1, 2);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
}