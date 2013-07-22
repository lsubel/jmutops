package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.sor.SOR;

import org.junit.Test;

import utils.MethodTest;

public class SOR_Test extends MethodTest {

	@Override
	protected String getOperatorName() {
		return new SOR().getShortname();
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0;" +
				"int b = 64;";
	}
	
	@Test
	public void testSOR_shiftConstants1() {
		int diff = compareMatches("int result = 65532 >>> 8 ;System.out.println();", "int result = 65532 >> 8 ;System.out.println();");
		assertEquals(1, diff);
	}

	@Test
	public void testSOR_shiftConstants2() {
		int diff = compareMatches("int result = 555 >> 3 ;System.out.println();", "int result = 555 << 3 ;System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testSOR_shiftConstants3() {
		int diff = compareMatches("int result = 1000 << 5 ;System.out.println();", "int result = 1000 >>> 5 ;System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testSOR_shiftField1() {
		int diff = compareMatches("this.a = this.a << 5 ;System.out.println();", "this.a = this.a >>> 5 ;System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testSOR_shiftField2() {
		int diff = compareMatches("int result = this.b >> 3; System.out.println();", "int result = this.b << 3; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testSOR_shiftField3() {
		int diff = compareMatches("int result = this.b << this.a ;System.out.println();", "int result = this.b >>> this.a ;System.out.println();");
		assertEquals(1, diff);
	}
	 
	@Test
	public void testSOR_notShift1() {
		int diff = compareMatches("int result = this.b << 52 ;System.out.println();", "int result = this.b << 25 ;System.out.println();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testSOR_notShift2() {
		int diff = compareMatches("int result = b >>> 25 ;System.out.println();", "int result = a >>> 25 ;System.out.println();");
		assertEquals(0, diff);
	}
	 
	@Test
	public void testSOR_multipleShift1() {
		int diff = compareMatches("this.a = ((this.b << 52) >> 52) ;System.out.println();", "this.a = ((this.b >> 52) >>> 52) ;System.out.println();");
		assertEquals(2, diff);
	}
	
	@Test
	public void testSOR_multipleShift2() {
		int diff = compareMatches("int result = (((12675 << 2) << 6) << 7); System.out.println();", "int result = (((12675 >> 2) >>> 6) >>> 7); System.out.println();");
		assertEquals(3, diff);
	}
}
