package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.ror.ROR;

import org.junit.Test;

import utils.MethodTest;

public class ROR_Test extends MethodTest {
	
	@Override
	protected String getOperatorName() {
		return new ROR().getShortname();
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a1 = 42; " +
				"int a2 = 1337; " +
				"boolean b = false;";
	}
	
	@Test
	public void testROR_constantComparision1(){
		int diff = compareMatches("boolean result = (42 < 42); System.out.println();", "boolean result = (42 != 42); System.out.println();");
		assertEquals(1, diff);
	}

	@Test
	public void testROR_constantComparision2(){
		int diff = compareMatches("boolean result = (3 > 1); System.out.println();", "boolean result = (3 >= 1); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_constantComparision3(){
		int diff = compareMatches("boolean result = (5 != 4); System.out.println();", "boolean result = (5 <= 4); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_fieldComparision1(){
		int diff = compareMatches("this.b = (a1 != a2); System.out.println();", "this.b = (a1 <= a2); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_fieldComparision2(){
		int diff = compareMatches("boolean result = (a1 < a2); System.out.println();", "boolean result = (a1 != a2); System.out.println();");
		assertEquals(1, diff);
	}

	@Test
	public void testROR_fieldComparision3(){
		int diff = compareMatches("boolean result = (a1 > a2); System.out.println();", "boolean result = (a1 >= a2); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_ifComparision1(){
		int diff = compareMatches("if(a1 < a2){System.out.println();}", "if(a1 == a2){System.out.println();}");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_ifComparision2(){
		int diff = compareMatches("if(a1 != a2){System.out.println();}", "if(a1 >= a2){System.out.println();}");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_whileComparision1(){
		int diff = compareMatches("while(a1 < a2){System.out.println();}", "while(a1 == a2){System.out.println();}");
		assertEquals(1, diff);
	}
	
	@Test
	public void testROR_whileComparision2(){
		int diff = compareMatches("while(a1 != a2){System.out.println();}", "while(a1 >= a2){System.out.println();}");
		assertEquals(1, diff);
	}
}
