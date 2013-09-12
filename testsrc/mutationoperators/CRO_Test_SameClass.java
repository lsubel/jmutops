package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.cro.CRO;

import org.junit.Test;

import utils.MethodTest;

public class CRO_Test_SameClass extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new CRO();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int value; " + 
				"Foo childLeft = null; " +
				"Foo childRight = null; " +
				"public Foo(){value = 0;} " +
				"public Foo(int v){value = v;} " +
				"public Foo(Foo left){childLeft = left;} " +
				"public Foo(Foo left, Foo right){childLeft = left; childRight = right;} " +
				"public Foo(int v, Foo left){value = v; childLeft = left;} " +
				"public Foo(int v, Foo left, Foo right){value = v; childLeft = left; childRight = right;} " +
				"public int getValue(){return value;} ";
	}

	@Test
	public void testCRO_LocalReplacement1() {
		HashMap<String, Integer> resultMap = compareMatches("Foo Temp = new Foo();", "Foo Temp = new Foo(1);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_LocalReplacement2() {
		HashMap<String, Integer> resultMap = compareMatches("Foo Temp = new Foo();", "Foo Temp = new Foo(1, null);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testCRO_SameArgumentNumber1() {
		HashMap<String, Integer> resultMap = compareMatches("childRight = new Foo(); Foo Temp = new Foo(1);", "childRight = new Foo(); Foo Temp = new Foo(childRight);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameArgumentNumber2() {
		HashMap<String, Integer> resultMap = compareMatches("childRight = new Foo(); Foo Temp = new Foo(childRight, childRight);", "childRight = new Foo(); Foo Temp = new Foo(42, childRight);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

}
