package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.aor.AOR;

import org.junit.Test;


public class AOR_Test extends MethodTest {


	MutationOperator mutop_aor;
	MutationOperator mutop_aco;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_aor = new AOR();
		this.addMutationOperatorToTest(mutop_aor);
		this.mutop_aco = new ACO();
		this.addMutationOperatorToTest(mutop_aco);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0; \n" +
				"int b = 4; \n"+
				"int c = 42; \n" +
				"int d = 21; \n" +
				"public int getMax(int arg1, int arg2){if(arg1>arg2){return arg1;}else{return arg2;}} \n"
				;
	}
	
	@Test
	public void testAOR_BinaryMatch1() {
		HashMap<String, Integer> resultMap = compareMatches("a = (b + c);", "a = (b - c);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_BinaryMatch2() {
		HashMap<String, Integer> resultMap = compareMatches("a = (b - c);", "a = (b * c);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_BinaryMatch3() {
		HashMap<String, Integer> resultMap = compareMatches("a = d*1;", "a = d/1;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_BinaryMatch4() {
		HashMap<String, Integer> resultMap = compareMatches("a = d % 2;", "a = d * 2;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_BinaryMatch5() {
		HashMap<String, Integer> resultMap = compareMatches("a = d / 2;", "a = d % 2;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_BinaryMatch6() {
		HashMap<String, Integer> resultMap = compareMatches("a = getMax(a, b + c);", "a = getMax(a, b * c);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_UnaryMatch1() {
		HashMap<String, Integer> resultMap = compareMatches("a = d * (+b);", "a = d * (-b);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_UnaryMatch2() {
		HashMap<String, Integer> resultMap = compareMatches("a = -b;", "a = +b;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_UnaryMatch3() {
		HashMap<String, Integer> resultMap = compareMatches("a = +(b + c);", "a = -(b + c);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_UnaryMatch4() {
		HashMap<String, Integer> resultMap = compareMatches("a = -(b + c);", "a = +(b + c);");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_ShortCut1() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); this.a++;", "System.out.println(); ++this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_ShortCut2() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); --this.a;", "System.out.println(); this.a--;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_ShortCut3() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); ++this.a;", "System.out.println(); this.a--;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_ShortCut4() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); --this.a;", "System.out.println(); this.a++;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_ShortCut5() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); --this.a;", "System.out.println(); ++this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testAOR_ShortCut6() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); ++this.a;", "System.out.println(); --this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop_aor));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
}
