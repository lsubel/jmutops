package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.sor.SOR;

import org.junit.Test;

import utils.MethodTest;

public class SOR_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new SOR();
		this.addMutationOperatorToTest(mutop);
	}
	
	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0;" +
				"int b = 64;";
	}
	
	@Test
	public void testSOR_shiftConstants1() {
		HashMap<String, Integer> resultMap = compareMatches("int result = 65532 >>> 8 ;System.out.println();", "int result = 65532 >> 8 ;System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testSOR_shiftConstants2() {
		HashMap<String, Integer> resultMap = compareMatches("int result = 555 >> 3 ;System.out.println();", "int result = 555 << 3 ;System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftConstants3() {
		HashMap<String, Integer> resultMap = compareMatches("int result = 1000 << 5 ;System.out.println();", "int result = 1000 >>> 5 ;System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftField1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a = this.a << 5 ;System.out.println();", "this.a = this.a >>> 5 ;System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftField2() {
		HashMap<String, Integer> resultMap = compareMatches("int result = this.b >> 3; System.out.println();", "int result = this.b << 3; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftField3() {
		HashMap<String, Integer> resultMap = compareMatches("int result = this.b << this.a ;System.out.println();", "int result = this.b >>> this.a ;System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	 
	@Test
	public void testSOR_notShift1() {
		HashMap<String, Integer> resultMap = compareMatches("int result = this.b << 52 ;System.out.println();", "int result = this.b << 25 ;System.out.println();");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	 
	@Test
	public void testSOR_multipleShift1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a = ((this.b << 52) >> 52) ;System.out.println();", "this.a = ((this.b >> 52) >>> 52) ;System.out.println();");
		assertEquals(2, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_multipleShift2() {
		HashMap<String, Integer> resultMap = compareMatches("int result = (((12675 << 2) << 6) << 7); System.out.println();", "int result = (((12675 >> 2) >>> 6) >>> 7); System.out.println();");
		assertEquals(3, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
