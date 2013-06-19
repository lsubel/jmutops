package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.lco.LCO;

import org.junit.Test;

public class LCO_Test extends BasicMutationOperatorTest {
	
	@Override
	protected String getOperatorName() {
		return LCO.class.getSimpleName();
	}

	@Override
	protected String getFields() {
		return "boolean b;";
	}
	
	@Test
	public void testLCO_TrueToFalse() {
		int diff = compareMatches("b = true;", "b = false;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLCO_FalseToTrue() {
		int diff = compareMatches("b = false;", "b = true;");
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
}
