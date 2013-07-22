package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.asr.ASR;

import org.junit.Test;

import utils.MethodTest;

public class ASR_Test extends MethodTest {
	
	@Override
	protected String getOperatorName() {
		return new ASR().getShortname();
	}

	@Override
	protected String getFields() {
		return  "int a1 = 42; " +
				"int a2 = 100; " +
				"boolean b1 = true; " +
				"boolean b2 = false; ";
	}

	@Test
	public void testASR_int1() {
		int diff = compareMatches("this.a1 += 42;", "this.a1 -= 42;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_int2() {
		int diff = compareMatches("this.a1 -= 42;", "this.a1 *= 42;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_int3() {
		int diff = compareMatches("this.a1 *= 42;", "this.a1 /= 42;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_int4() {
		int diff = compareMatches("this.a1 /= 42;", "this.a1 %= 42;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_int5() {
		int diff = compareMatches("this.a1 %= 42;", "this.a1 += 42;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_boolean1() {
		int diff = compareMatches("this.b1 &= this.b2;", "this.b1 |= this.b2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_boolean2() {
		int diff = compareMatches("this.b1 |= this.b2;", "this.b1 ^= this.b2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_boolean3() {
		int diff = compareMatches("this.b1 ^= this.b2;", "this.b1 &= this.b2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_shift1() {
		int diff = compareMatches("this.a1 <<= this.a2;", "this.a1 >>= this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_shift2() {
		int diff = compareMatches("this.a1 >>= this.a2;", "this.a1 >>>= this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_shift3() {
		int diff = compareMatches("this.a1 >>>= this.a2;", "this.a1 <<= this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_mixed1() {
		int diff = compareMatches("this.a1 >>>= this.a2;", "this.a1 += this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_mixed2() {
		int diff = compareMatches("this.a1 <<= this.a2;", "this.a1 -= this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_mixed3() {
		int diff = compareMatches("this.a1 *= this.a2;", "this.a1 >>= this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_mixed4() {
		int diff = compareMatches("this.a1 %= this.a2;", "this.a1 >>>= this.a2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testASR_mixed5() {
		int diff = compareMatches("this.a1 /= this.a2;", "this.a1 <<= this.a2;");
		assertEquals(1, diff);
	}
}
