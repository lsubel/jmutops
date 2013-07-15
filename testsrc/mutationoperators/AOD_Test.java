package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.aod.AOD;

import org.junit.Test;

public class AOD_Test extends BasicMutationOperatorTest {

	@Override
	protected String getOperatorName() {
		return new AOD().getShortname();
	}

	@Override
	protected String getFields() {
		return  "int a = 0; " + 
				"int b = 100; ";
	}

	@Test
	public void testAOD_unaryDeletion1() {
		int diff = compareMatches("this.a = +this.b;", "this.a = this.b;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_unaryDeletion2() {
		int diff = compareMatches("this.b = -this.a;", "this.b = this.a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_unaryDeletion3() {
		int diff = compareMatches("this.b = -(this.a + 10);", "this.b = (this.a + 10);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_unaryDeletion4() {
		int diff = compareMatches("this.b = +1;", "this.b = 1;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion1() {
		int diff = compareMatches("System.out.println(); this.b = ++this.a;", "System.out.println(); this.b = this.a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion2() {
		int diff = compareMatches("this.b = --this.a; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion3() {
		int diff = compareMatches("this.b = this.a--; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion4() {
		int diff = compareMatches("this.b = this.a++; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletionForLoop1() {
		int diff = compareMatches("for(int i = 0; i<a--; i++){System.out.println();}", "for(int i = 0; i<a; i++){System.out.println();}");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletionWhileLoop1() {
		int diff = compareMatches("while(a++ < b){System.out.println();b-=1;}", "while(a < b){System.out.println();b-=1;}");
		assertEquals(1, diff);
	}
}
