package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperators.methodlevel.jtd.JTD;

import org.junit.Test;

import utils.MethodTest;

public class JTD_Test extends MethodTest {

MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new JTD();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0; " +
				"int c = 42; " +
				"String b = \"Bla\"; " +
				"public void test(){b = \"test\";} " + 
				"public int test1(){return a;} " +
				"public String test2(){return b;}" +
				"public void test3(String arg){this.b = this.b + arg;}" +
				"public void test4(String arg1, String arg2){this.b = this.b + arg1 + arg2;}"
				;
	}
	
	
	@Test
	public void testJTD_FieldAssign1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = 5;", "a = 5;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testJTD_FieldAssign2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = \"Test\";", "b = \"Test\";");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testJTD_FieldUsage1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = this.a*2;", "a = a*2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}	

	@Test
	public void testJTD_FieldUsage2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = this.b + b;", "b = b + b;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testJTD_FieldUsage3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = this.a + this.a;", "a = a + a;");
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_MethodCall1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.test();", "test();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_MethodCall2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = this.test1();", "a = test1();");
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_MethodCall3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = this.test2();", "b = test2();");
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_Argument1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test3(this.b);", "test3(b);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_Argument2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test4(b, this.b);", "test4(b, b);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_NoMatch1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test4(b, this.b);", "test4(this.b, this.b);");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_NoMatch2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = a*2;", "a = this.a*2;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}	
	
	@Test
	public void testJTD_NoMatch3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = 5;", "this.a = 5;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_NoMatch4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.c = 5;", "this.a = 5;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testJTD_NoMatch5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = this.c*2;", "a = this.a*2;");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

}
