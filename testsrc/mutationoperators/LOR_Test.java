package mutationoperators;

import static org.junit.Assert.assertEquals;
import mutationoperators.lor.LOR;

import org.junit.Test;

import utils.MethodTest;

public class LOR_Test extends MethodTest {

	public LOR_Test() {
		super(new LOR());
	}

	@Override
	protected String getOtherClassContent() {
		return 	"boolean a = true; " +
				"boolean b = false;" +
				"boolean c = false;" +
				"boolean d = true;";
	}
	
	@Test
	public void testLOR_constantComparision1(){
		int diff = compareMatches("boolean result = (true & false); System.out.println();", "boolean result = (true | false); System.out.println();");
		assertEquals(1, diff);
	}

	@Test
	public void testLOR_constantComparision2(){
		int diff = compareMatches("boolean result = (true ^ false); System.out.println();", "boolean result = (true & false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOR_constantComparision3(){
		int diff = compareMatches("boolean result = (false | false); System.out.println();", "boolean result = (false ^ false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOR_multipleConstantComparision1(){
		int diff = compareMatches("boolean result = ((true ^ false) | true); System.out.println();", "boolean result = ((true & false) | true); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOR_multipleConstantComparision2(){
		int diff = compareMatches("boolean result = ((true & false) | (true ^ false)); System.out.println();", "boolean result = ((true | false) | (true & false)); System.out.println();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testLOR_fieldComparision1(){
		int diff = compareMatches("boolean result = ((a | b) | (c | d)); System.out.println();", "boolean result = ((a & b) ^ (c & d)); System.out.println();");
		assertEquals(3, diff);
	}
	
	@Test
	public void testLOR_fieldComparision2(){
		int diff = compareMatches("boolean result = ((a & b) & c); System.out.println();", "boolean result = ((a ^ b) & c); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOR_fieldComparision3(){
		int diff = compareMatches("boolean result = ((a ^ b) | c); System.out.println();", "boolean result = ((a | b) & c); System.out.println();");
		assertEquals(2, diff);
	}
	
}
