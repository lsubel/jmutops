package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.jti.JTI;

import org.junit.Test;


public class JTI_Test extends MethodTest {

	MutationOperator mutop_jti;
	MutationOperator mutop_aco;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_jti = new JTI();
		this.mutop_aco = new ACO();
		this.addMutationOperatorToTest(mutop_jti);
		this.addMutationOperatorToTest(mutop_aco);
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
		HashMap<String, Integer> resultMap = compareMatches("a = 5;", "this.a = 5;");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testJTI_FieldAssign2() {
		HashMap<String, Integer> resultMap = compareMatches("b = \"Test\";", "this.b = \"Test\";");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testJTI_FieldUsage1() {
		HashMap<String, Integer> resultMap = compareMatches("a = a*2;", "a = this.a*2;");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}	

	@Test
	public void testJTI_FieldUsage2() {
		HashMap<String, Integer> resultMap = compareMatches("b = b + b;", "b = this.b + b;");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testJTI_FieldUsage3() {
		HashMap<String, Integer> resultMap = compareMatches("a = a + a;", "a = this.a + this.a;");
		assertEquals(2, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTI_MethodCall1() {
		HashMap<String, Integer> resultMap = compareMatches("test();", "this.test();");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTI_MethodCall2() {
		HashMap<String, Integer> resultMap = compareMatches("a = test1();", "this.a = this.test1();");
		assertEquals(2, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTI_MethodCall3() {
		HashMap<String, Integer> resultMap = compareMatches("b = test2();", "this.b = this.test2();");
		assertEquals(2, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTI_Argument1() {
		HashMap<String, Integer> resultMap = compareMatches("test3(b);", "test3(this.b);");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTI_Argument2() {
		HashMap<String, Integer> resultMap = compareMatches("test4(b, b);", "test4(b, this.b);");
		assertEquals(1, getApplicationValue(resultMap, mutop_jti));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

}
