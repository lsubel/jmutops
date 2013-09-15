package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.asr.ASR;

import org.junit.Test;


public class ASR_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new ASR();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a1 = 42; " +
				"int a2 = 100; " +
				"boolean b1 = true; " +
				"boolean b2 = false; ";
	}

	@Test
	public void testASR_int1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 += 42;", "this.a1 -= 42;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int2() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 -= 42;", "this.a1 *= 42;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int3() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 *= 42;", "this.a1 /= 42;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int4() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 /= 42;", "this.a1 %= 42;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int5() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 %= 42;", "this.a1 += 42;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_boolean1() {
		HashMap<String, Integer> resultMap = compareMatches("this.b1 &= this.b2;", "this.b1 |= this.b2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_boolean2() {
		HashMap<String, Integer> resultMap = compareMatches("this.b1 |= this.b2;", "this.b1 ^= this.b2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_boolean3() {
		HashMap<String, Integer> resultMap = compareMatches("this.b1 ^= this.b2;", "this.b1 &= this.b2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_shift1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 <<= this.a2;", "this.a1 >>= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_shift2() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 >>= this.a2;", "this.a1 >>>= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_shift3() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 >>>= this.a2;", "this.a1 <<= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 >>>= this.a2;", "this.a1 += this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed2() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 <<= this.a2;", "this.a1 -= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed3() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 *= this.a2;", "this.a1 >>= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed4() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 %= this.a2;", "this.a1 >>>= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed5() {
		HashMap<String, Integer> resultMap = compareMatches("this.a1 /= this.a2;", "this.a1 <<= this.a2;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
