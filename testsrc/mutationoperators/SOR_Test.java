package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
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
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = 65532 >>> 8 ;System.out.println();", "int result = 65532 >> 8 ;System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testSOR_shiftConstants2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = 555 >> 3 ;System.out.println();", "int result = 555 << 3 ;System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftConstants3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = 1000 << 5 ;System.out.println();", "int result = 1000 >>> 5 ;System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftField1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = this.a << 5 ;System.out.println();", "this.a = this.a >>> 5 ;System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftField2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = this.b >> 3; System.out.println();", "int result = this.b << 3; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_shiftField3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = this.b << this.a ;System.out.println();", "int result = this.b >>> this.a ;System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	 
	@Test
	public void testSOR_notShift1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = this.b << 52 ;System.out.println();", "int result = this.b << 25 ;System.out.println();");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_notShift2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = b >>> 25 ;System.out.println();", "int result = a >>> 25 ;System.out.println();");
		assertEquals(0, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	 
	@Test
	public void testSOR_multipleShift1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = ((this.b << 52) >> 52) ;System.out.println();", "this.a = ((this.b >> 52) >>> 52) ;System.out.println();");
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSOR_multipleShift2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("int result = (((12675 << 2) << 6) << 7); System.out.println();", "int result = (((12675 >> 2) >>> 6) >>> 7); System.out.println();");
		assertEquals(3, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
