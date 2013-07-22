package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.cor.COR;

import org.junit.Test;

import utils.MethodTest;

public class COR_Test extends MethodTest {

	@Override
	protected String getOperatorName() {
		return new COR().getShortname();
	}

	@Override
	protected String getFields() {
		return 	"boolean a = true; " +
				"boolean b = false;" +
				"boolean c = false;" +
				"boolean d = true;";
	}
	
	@Test
	public void testCOR_constantComparision1(){
		int diff = compareMatches("boolean result = (true && false); System.out.println();", "boolean result = (true || false); System.out.println();");
		assertEquals(1, diff);
	}

	@Test
	public void testCOR_constantComparision2(){
		int diff = compareMatches("boolean result = (true & false); System.out.println();", "boolean result = (true | false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOR_constantComparision3(){
		int diff = compareMatches("boolean result = (false & false); System.out.println();", "boolean result = (false && false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOR_constantComparision4(){
		int diff = compareMatches("boolean result = (true || true); System.out.println();", "boolean result = (true | true); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOR_multipleConstantComparision1(){
		int diff = compareMatches("boolean result = ((true && false) | true); System.out.println();", "boolean result = ((true || false) | true); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOR_multipleConstantComparision2(){
		int diff = compareMatches("boolean result = ((true && false) | (true && false)); System.out.println();", "boolean result = ((true || false) | (true || false)); System.out.println();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testCOR_multipleConstantComparision3(){
		int diff = compareMatches("boolean result = ((true && false) | (true && false)); System.out.println();", "boolean result = ((true & false) && (true & false)); System.out.println();");
		assertEquals(3, diff);
	}
	
	@Test
	public void testCOR_multipleChanges1(){
		int diff = compareMatches("boolean result = ((true && false) | (true && false)); System.out.println();", "boolean result = ((true || false) | false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOR_fieldComparision1(){
		int diff = compareMatches("boolean result = ((a && b) | (c && d)); System.out.println();", "boolean result = ((a || b) | (c || d)); System.out.println();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testCOR_fieldComparision2(){
		int diff = compareMatches("boolean result = ((a & b) & c); System.out.println();", "boolean result = ((a | b) & c); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOR_fieldComparision3(){
		int diff = compareMatches("boolean result = ((a || b) | c); System.out.println();", "boolean result = ((a | b) || c); System.out.println();");
		assertEquals(2, diff);
	}
	
}
