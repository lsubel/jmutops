package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.cro.CRO;
import mutationoperators.methodlevel.vro.VRO;

import org.junit.Test;


public class ACO_Test extends MethodTest {
	
	MutationOperator mutop_aco;
	MutationOperator mutop_cro;
	MutationOperator mutop_vro;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_aco = new ACO();
		this.addMutationOperatorToTest(mutop_aco);
		this.mutop_cro = new CRO();
		this.addMutationOperatorToTest(mutop_cro);
		this.mutop_vro = new VRO();
		this.addMutationOperatorToTest(mutop_vro);
	}
	
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
	
	@Test
	public void testACO_MethodNumberChanged1() {
		HashMap<String, Integer> resultMap = compareMatches("setValue(1);", "setValue(1, 2);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap, mutop_aco);
	}
	
	@Test
	public void testACO_MethodNumberChanged2() {
		HashMap<String, Integer> resultMap = compareMatches("Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f1, f2);", "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f2);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap, mutop_aco);
	}

	@Test
	public void testACO_ConstructorNumberChanged1() {
		HashMap<String, Integer> resultMap = compareMatches("Foo f1 = new Foo(0, 4);", "Foo f1 = new Foo();");
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testACO_ConstructorNumberChanged2() {
		HashMap<String, Integer> resultMap = compareMatches("Foo f1 = new Foo(0);", "Foo f1 = new Foo(6, 1);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testACO_ConstructorReordering1() {
		String pre  = "Foo f1 = new Foo(42, 0);";
		String post = "Foo f1 = new Foo(0, 42);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testACO_MethodReordering1() {
		String pre  = "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f1, f2);";
		String post = "Foo f1 = new Foo(); Foo f2 = new Foo(2); setValue(f2, f1);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(2, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testACO_MethodReordering2() {
		HashMap<String, Integer> resultMap = compareMatches("setValue(2, 1);", "setValue(1, 2);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testACO_ExampleAspectj1() {
		String pre  = "Foo concreteAspect = new Foo(); String aspectCode = \"CODE\"; StringBuffer namespace = new StringBuffer(); namespace = namespace.append((\";\" + aspectCode));";
		String post = "Foo concreteAspect = new Foo(); String aspectCode = \"CODE\"; StringBuffer namespace = new StringBuffer(); namespace = namespace.append((\";\" + concreteAspect.name));";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
}
