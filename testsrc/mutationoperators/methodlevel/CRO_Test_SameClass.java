package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.cro.CRO;

import org.junit.Test;


public class CRO_Test_SameClass extends MethodTest {

	MutationOperator mutop_cro;
	MutationOperator mutop_aco;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_cro = new CRO();
		this.addMutationOperatorToTest(mutop_cro);
		this.mutop_aco = new ACO();
		this.addMutationOperatorToTest(mutop_aco);
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
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_LocalReplacement2() {
		HashMap<String, Integer> resultMap = compareMatches("Foo Temp = new Foo();", "Foo Temp = new Foo(1, null);");
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testCRO_SameArgumentNumber1() {
		HashMap<String, Integer> resultMap = compareMatches("childRight = new Foo(); Foo Temp = new Foo(1);", "childRight = new Foo(); Foo Temp = new Foo(childRight);");
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameArgumentNumber2() {
		HashMap<String, Integer> resultMap = compareMatches("childRight = new Foo(); Foo Temp = new Foo(childRight, childRight);", "childRight = new Foo(); Foo Temp = new Foo(42, childRight);");
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

}
