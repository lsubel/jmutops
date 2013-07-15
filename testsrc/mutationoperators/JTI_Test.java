package mutationoperators;

import static org.junit.Assert.*;
import mutationoperators.jti.JTI;
import org.junit.Test;

public class JTI_Test extends BasicMutationOperatorTest {
	
	@Override
	protected String getOperatorName() {
		return new JTI().getShortname();
	}

	@Override
	protected String getFields() {
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
		int diff = compareMatches("a = 5;", "this.a = 5;");
		assertEquals(1, diff);
	}

	@Test
	public void testJTI_FieldAssign2() {
		int diff = compareMatches("b = \"Test\";", "this.b = \"Test\";");
		assertEquals(1, diff);
	}

	@Test
	public void testJTI_FieldUsage1() {
		int diff = compareMatches("a = a*2;", "a = this.a*2;");
		assertEquals(1, diff);
	}	

	@Test
	public void testJTI_FieldUsage2() {
		int diff = compareMatches("b = b + b;", "b = this.b + b;");
		assertEquals(1, diff);
	}

	@Test
	public void testJTI_FieldUsage3() {
		int diff = compareMatches("a = a + a;", "a = this.a + this.a;");
		assertEquals(2, diff);
	}
	
	@Test
	public void testJTI_MethodCall1() {
		int diff = compareMatches("test();", "this.test();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testJTI_MethodCall2() {
		int diff = compareMatches("a = test1();", "this.a = this.test1();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testJTI_MethodCall3() {
		int diff = compareMatches("b = test2();", "this.b = this.test2();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testJTI_Argument1() {
		int diff = compareMatches("test3(b);", "test3(this.b);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testJTI_Argument2() {
		int diff = compareMatches("test4(b, b);", "test4(b, this.b);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testJTI_NoMatch1() {
		int diff = compareMatches("test4(this.b, this.b);", "test4(b, this.b);");
		assertEquals(0, diff);
	}
	
	@Test
	public void testJTI_NoMatch2() {
		int diff = compareMatches("a = this.a*2;", "a = a*2;");
		assertEquals(0, diff);
	}	
	
	@Test
	public void testJTI_NoMatch3() {
		int diff = compareMatches("this.a = 5;", "a = 5;");
		assertEquals(0, diff);
	}
	
	@Test
	public void testJTI_NoMatch4() {
		int diff = compareMatches("this.a = 5;", "this.c = 5;");
		assertEquals(0, diff);
	}
	
	@Test
	public void testJTI_NoMatch5() {
		int diff = compareMatches("a = this.a*2;", "a = this.c*2;");
		assertEquals(0, diff);
	}	
}
