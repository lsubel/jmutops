package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.aco.ACO;

import org.junit.Test;

import utils.MethodTest;

public class ACO_Test extends MethodTest {
	
	MutationOperator mutop;
	
	@Override
	protected String getOtherClassContent() {
		return  "int value; " + 
				"public String name = \"NAME\";" +
				"public Foo(){value = 0;} " +
				"public Foo(Foo f1){value = f1.getValue();} " +
				"public Foo(int v){value = v;} " +
				"public Foo(int v1, int v2){value = v1 + v2;} " +
				"public int getZero(){return 0;} " +
				"public int getValue(){return value;} " +
				"public void setValue(int v){value = v;} " +
				"public void setValue(int v1, int v2){value = v1 + v2;} " +
				"public void setValue(Foo f1){value = f1.getValue();} " +
				"public void setValue(Foo f1, Foo f2){value = f1.getValue() + f2.getValue();} ";
	}

	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new ACO();
		this.addMutationOperatorToTest(mutop);
	}
	
	@Test
	public void testACO_MethodNumberChanged1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("setValue(1);", "setValue(1, 2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_MethodNumberChanged2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f1, f2);", "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testACO_ConstructorNumberChanged1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(0, 4);", "Foo f1 = new Foo();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testACO_ConstructorNumberChanged2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(0);", "Foo f1 = new Foo(6, 1);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_ConstructorReordering1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(42, 0);", "Foo f1 = new Foo(0, 42);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_MethodReordering1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f1, f2);", "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f2, f1);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_MethodReordering2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("setValue(2, 1);", "setValue(1, 2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_NoMatching1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int i = getZero();", "int i = getValue();");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_NoMatching2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(); Foo f2 = new Foo(2); ", "Foo f1 = new Foo(); Foo f2 = new Foo(f1); ");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_NoMatching3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo f1 = new Foo(); setValue(f1); ", "Foo f1 = new Foo(); setValue(1); ");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testACO_ExampleAspectj1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Foo concreteAspect = new Foo(); String aspectCode = \"CODE\"; StringBuffer namespace = new StringBuffer(); namespace = namespace.append((\";\" + aspectCode));", "Foo concreteAspect = new Foo(); String aspectCode = \"CODE\"; StringBuffer namespace = new StringBuffer(); namespace = namespace.append((\";\" + concreteAspect.name));");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
