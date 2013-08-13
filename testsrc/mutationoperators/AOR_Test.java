package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aor.AOR;

import org.junit.Test;

import utils.MethodTest;

public class AOR_Test extends MethodTest {


	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new AOR();
		this.addMutationOperatorToTest(mutop);
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
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = (b + c);", "a = (b - c);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_BinaryMatch2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = (b - c);", "a = (b * c);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_BinaryMatch3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = d*1;", "a = d/1;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_BinaryMatch4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = d % 2;", "a = d * 2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_BinaryMatch5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = d / 2;", "a = d % 2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_BinaryMatch6() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = getMax(a, b + c);", "a = getMax(a, b * c);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_UnaryMatch1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = d * (+b);", "a = d * (-b);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_UnaryMatch2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = -b;", "a = +b;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_UnaryMatch3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = +(b + c);", "a = -(b + c);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_UnaryMatch4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = -(b + c);", "a = +(b + c);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_ShortCut1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); this.a++;", "System.out.println(); ++this.a;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_ShortCut2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); --this.a;", "System.out.println(); this.a--;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_ShortCut3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); ++this.a;", "System.out.println(); this.a--;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_ShortCut4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); --this.a;", "System.out.println(); this.a++;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_ShortCut5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); --this.a;", "System.out.println(); ++this.a;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOR_ShortCut6() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("System.out.println(); ++this.a;", "System.out.println(); --this.a;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
