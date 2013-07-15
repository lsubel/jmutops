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
		int diff = compareMatches("this.b++;", "this.b;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion2() {
		int diff = compareMatches("this.b--;", "this.b;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion3() {
		int diff = compareMatches("++this.a;", "this.a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion4() {
		int diff = compareMatches("--this.a;", "this.a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion5() {
		int diff = compareMatches("b = ++a;", "b = a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion6() {
		int diff = compareMatches("b = --a;", "b = a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion7() {
		int diff = compareMatches("b = a--;", "b = a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOD_shortcutDeletion8() {
		int diff = compareMatches("b = a++;", "b = a;");
		assertEquals(1, diff);
	}
}
