package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.lco.LCO;

import org.junit.Test;

public class LCO_Test extends BasicMutationOperatorTest {
	
	@Override
	protected String getOperatorName() {
		return new LCO().getShortname();
	}

	@Override
	protected String getFields() {
		return "boolean b;";
	}
	
	@Test
	public void testLCO_TrueToFalse1() {
		int diff = compareMatches("System.out.println(); this.b = true;", "System.out.println(); this.b = false;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_TrueToFalse2() {
		int diff = compareMatches("if(true){System.out.println(b);};", "if(false){System.out.println(b);};");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_TrueToFalse3() {
		int diff = compareMatches("while(true){this.b = true; this.b = false;}", "while(false){this.b = true; this.b = false;}");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_FalseToTrue1() {
		int diff = compareMatches("System.out.println(); this.b = false;", "System.out.println(); this.b = true;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_FalseToTrue2() {
		int diff = compareMatches("if(false){System.out.println(42);};", "if(true){System.out.println(42);};");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_FalseToTrue3() {
		int diff = compareMatches("while(false){this.b = true; this.b = false;}", "while(true){this.b = true; this.b = false;}");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_NoChange1() {
		int diff = compareMatches("b = true;", "b = !true;");
		assertEquals(0, diff);
	}
	
	@Test
	public void testLCO_NoChange2() {
		int diff = compareMatches("b = false;", "b = !false;");
		assertEquals(0, diff);
	}
	
	@Test
	public void testLCO_NoChange3() {
		int diff = compareMatches("b = false;", "b = !true;");
		assertEquals(0, diff);
	}
}
