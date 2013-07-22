package mutationoperators;
import static org.junit.Assert.assertEquals;
import mutationoperators.afro.AFRO;

import org.junit.Test;

import utils.MethodTest;


public class AFRO_Test extends MethodTest {
	
	@Override
	protected String getOperatorName() {
		return (new AFRO()).getShortname();
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a1 = 0; " + 
				"boolean b1 = true; " +
				"boolean b2 = false; " +
				"int a2 = 4; ";
	}

	@Test
	public void testAFRO_LocalclassReplacement1() {
		int diff = compareMatches("int res = this.a1;", "int res = this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAFRO_LocalclassReplacement2() {
		int diff = compareMatches("int res = this.a1++;", "int res = this.a2++;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAFRO_LocalclassReplacement3() {
		int diff = compareMatches("boolean res = !this.b1;", "int res = !this.b2;");
		assertEquals(1, diff);
	}
}
