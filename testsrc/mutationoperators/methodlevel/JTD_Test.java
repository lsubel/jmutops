package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.jtd.JTD;

import org.junit.Test;


public class JTD_Test extends MethodTest {

	MutationOperator mutop_jdt;
	MutationOperator mutop_aco;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_jdt = new JTD();
		this.addMutationOperatorToTest(mutop_jdt);
		this.mutop_aco = new ACO();
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
	public void testJTD_FieldAssign1() {
		String pre 	= "this.a = 5;";
		String post = "a = 5;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testJTD_FieldAssign2() {
		String pre 	= "this.b = \"Test\";";
		String post = "b = \"Test\";";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testJTD_FieldUsage1() {
		String pre 	= "a = this.a*2;";
		String post = "a = a*2;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}	

	@Test
	public void testJTD_FieldUsage2() {
		String pre 	= "b = this.b + b;";
		String post = "b = b + b;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

	@Test
	public void testJTD_FieldUsage3() {
		String pre 	= "a = this.a + this.a;";
		String post = "a = a + a;";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTD_MethodCall1() {
		String pre 	= "this.test();";
		String post = "test();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTD_MethodCall2() {
		String pre 	= "this.a = this.test1();";
		String post = "a = test1();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTD_MethodCall3() {
		String pre 	= "this.b = this.test2();";
		String post = "b = test2();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTD_Argument1() {
		String pre 	= "test3(this.b);";
		String post = "test3(b);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testJTD_Argument2() {
		String pre 	= "test4(b, this.b);";
		String post = "test4(b, b);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_jdt));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}

}
