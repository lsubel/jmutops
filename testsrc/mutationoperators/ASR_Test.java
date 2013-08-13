package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.asr.ASR;

import org.junit.Test;

import utils.MethodTest;

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
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 += 42;", "this.a1 -= 42;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 -= 42;", "this.a1 *= 42;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 *= 42;", "this.a1 /= 42;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 /= 42;", "this.a1 %= 42;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_int5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 %= 42;", "this.a1 += 42;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_boolean1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b1 &= this.b2;", "this.b1 |= this.b2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_boolean2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b1 |= this.b2;", "this.b1 ^= this.b2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_boolean3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b1 ^= this.b2;", "this.b1 &= this.b2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_shift1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 <<= this.a2;", "this.a1 >>= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_shift2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 >>= this.a2;", "this.a1 >>>= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_shift3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 >>>= this.a2;", "this.a1 <<= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 >>>= this.a2;", "this.a1 += this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 <<= this.a2;", "this.a1 -= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 *= this.a2;", "this.a1 >>= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 %= this.a2;", "this.a1 >>>= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testASR_mixed5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a1 /= this.a2;", "this.a1 <<= this.a2;");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
