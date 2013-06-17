package mutationoperators;

import static org.junit.Assert.*;

import java.io.File;

import mutationoperators.jti.JTI;

import org.junit.Test;

public class JTI_Test extends BasicMutationOperatorTest {

	@Test
	public void testJTI_FieldAssign1() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("int a; ", "a = 5;"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("int a; ", "this.a = 5;"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 1, this.counter.getCount(JTI.fullname).intValue());
	}

	@Test
	public void testJTI_FieldAssign2() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("String a; ", "a = \"Test\";"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("String a; ", "this.a = \"Test\";"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 1, this.counter.getCount(JTI.fullname).intValue());
	}

	@Test
	public void testJTI_FieldUsage1() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("int a = 21; ", "a = a*2;"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("int a = 21; ", "a = this.a*2;"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 1, this.counter.getCount(JTI.fullname).intValue());
	}	

	@Test
	public void testJTI_FieldUsage2() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("String a = \"Test\"; ", "a = a + a;"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("String a = \"Test\"; ", "a = this.a + a;"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 1, this.counter.getCount(JTI.fullname).intValue());
	}

	@Test
	public void testJTI_FieldUsage3() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("String a = \"Test\"; ", "a = a + a;"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("String a = \"Test\"; ", "a = this.a + this.a;"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 2, this.counter.getCount(JTI.fullname).intValue());
	}
	
	@Test
	public void testJTI_MethodCall1() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("String a; public void test(){a = \"test\";}", "test();"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("String a; public void test(){a = \"test\";}", "this.test();"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 1, this.counter.getCount(JTI.fullname).intValue());
	}
	
	@Test
	public void testJTI_MethodCall2() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("int a; public int test(){return a;}", "a = test();"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("int a; public int test(){return a;}", "this.a = this.test();"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 2, this.counter.getCount(JTI.fullname).intValue());
	}
	
	@Test
	public void testJTI_MethodCall3() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("String a = \"test\"; public String test(){return a;}", "a = test();"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("String a = \"test\"; public String test(){return a;}", "this.a = this.test();"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 2, this.counter.getCount(JTI.fullname).intValue());
	}
	
	@Test
	public void testJTI_Argument1() {
		int oldValue = this.counter.getCount(JTI.fullname);
		File preFix = this.createPrefixSourceFile(this.createFieldMethodSourceCode("String a = \"test\"; public void test(String b){this.a = this.a + b;}", "test(a);"));
		File postFix = this.createPostfixSourceFile(this.createFieldMethodSourceCode("String a = \"test\"; public void test(String b){this.a = this.a + b;}", "test(this.a);"));
		this.jmutops.checkFiles(preFix, postFix);
		assertEquals(oldValue + 1, this.counter.getCount(JTI.fullname).intValue());
	}
}
