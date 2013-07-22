package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.cod.COD;

import org.junit.Test;

import utils.MethodTest;

public class COD_Test extends MethodTest {

	@Override
	protected String getOperatorName() {
		return new COD().getShortname();
	}

	@Override
	protected String getFields() {
		return  "int a1 = 0; " +
				"int a2 = 9; " +
				"boolean b1 = true; " +
				"boolean b2 = false; ";
	}

	@Test
	public void testCOD_constantComparision1(){
		int diff = compareMatches("boolean result = !(true && false); System.out.println();", "boolean result = (true && false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOD_constantComparision2(){
		int diff = compareMatches("boolean result = !(true & false); System.out.println();", "boolean result = (true & false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOD_constantComparision3(){
		int diff = compareMatches("boolean result = !(false | false); System.out.println();", "boolean result = (false | false); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOD_constantComparision4(){
		int diff = compareMatches("boolean result = !(true || true); System.out.println();", "boolean result = (true || true); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOD_constantComparision5(){
		int diff = compareMatches("boolean result = !(true ^ true); System.out.println();", "boolean result = (true ^ true); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOD_negateConstant1(){
		int diff = compareMatches("boolean result = !true; System.out.println();", "boolean result = true; System.out.println();");
		assertEquals(1, diff);
	}

	@Test
	public void testCOD_negateConstant2(){
		int diff = compareMatches("boolean result = !false; System.out.println();", "boolean result = false; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testCOD_multiple1(){
		int diff = compareMatches("boolean result = !b1 || !b2; System.out.println();", "boolean result = b1 || b2; System.out.println();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testCOD_multiple2(){
		int diff = compareMatches("boolean result = !(!(b1)); System.out.println();", "boolean result = ((b1)); System.out.println();");
		assertEquals(2, diff);
	}
}
