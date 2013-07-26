package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.jti.JTI;

import org.junit.Test;

import utils.MethodTest;

public class JTI_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new JTI();
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
	public void testJTI_FieldAssign1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = 5;", "this.a = 5;");
		assertEquals(1, resultMap.get(mutop).intValue());
	}

	@Test
	public void testJTI_FieldAssign2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = \"Test\";", "this.b = \"Test\";");
		assertEquals(1, resultMap.get(mutop).intValue());
	}

	@Test
	public void testJTI_FieldUsage1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = a*2;", "a = this.a*2;");
		assertEquals(1, resultMap.get(mutop).intValue());
	}	

	@Test
	public void testJTI_FieldUsage2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = b + b;", "b = this.b + b;");
		assertEquals(1, resultMap.get(mutop).intValue());
	}

	@Test
	public void testJTI_FieldUsage3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = a + a;", "a = this.a + this.a;");
		assertEquals(2, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_MethodCall1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test();", "this.test();");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_MethodCall2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = test1();", "this.a = this.test1();");
		assertEquals(2, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_MethodCall3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("b = test2();", "this.b = this.test2();");
		assertEquals(2, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_Argument1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test3(b);", "test3(this.b);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_Argument2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test4(b, b);", "test4(b, this.b);");
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_NoMatch1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("test4(this.b, this.b);", "test4(b, this.b);");
		assertEquals(0, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_NoMatch2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = this.a*2;", "a = a*2;");
		assertEquals(0, resultMap.get(mutop).intValue());
	}	
	
	@Test
	public void testJTI_NoMatch3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = 5;", "a = 5;");
		assertEquals(0, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_NoMatch4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = 5;", "this.c = 5;");
		assertEquals(0, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testJTI_NoMatch5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("a = this.a*2;", "a = this.c*2;");
		assertEquals(0, resultMap.get(mutop).intValue());
	}	
}
