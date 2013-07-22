package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.aor.AOR;

import org.junit.Test;

import utils.BasicMutationOperatorTest;

public class AOR_Test extends BasicMutationOperatorTest {

	@Override
	protected String getOperatorName() {
		return new AOR().getShortname();
	}

	@Override
	protected String getFields() {
		return 	"int a = 0; \n" +
				"int b = 4; \n"+
				"int c = 42; \n" +
				"int d = 21; \n" +
				"public int getMax(int arg1, int arg2){if(arg1>arg2){return arg1;}else{return arg2;}} \n"
				;
	}
	
	@Test
	public void testAOR_BinaryMatch1() {
		int diff = compareMatches("a = (b + c);", "a = (b - c);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_BinaryMatch2() {
		int diff = compareMatches("a = (b - c);", "a = (b * c);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_BinaryMatch3() {
		int diff = compareMatches("a = d*1;", "a = d/1;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_BinaryMatch4() {
		int diff = compareMatches("a = d % 2;", "a = d * 2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_BinaryMatch5() {
		int diff = compareMatches("a = d / 2;", "a = d % 2;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_BinaryMatch6() {
		int diff = compareMatches("a = getMax(a, b + c);", "a = getMax(a, b * c);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_UnaryMatch1() {
		int diff = compareMatches("a = d * (+b);", "a = d * (-b);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_UnaryMatch2() {
		int diff = compareMatches("a = -b;", "a = +b;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_UnaryMatch3() {
		int diff = compareMatches("a = +(b + c);", "a = -(b + c);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_UnaryMatch4() {
		int diff = compareMatches("a = -(b + c);", "a = +(b + c);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_ShortCut1() {
		int diff = compareMatches("System.out.println(); this.a++;", "System.out.println(); ++this.a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_ShortCut2() {
		int diff = compareMatches("System.out.println(); --this.a;", "System.out.println(); this.a--;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_ShortCut3() {
		int diff = compareMatches("System.out.println(); ++this.a;", "System.out.println(); this.a--;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_ShortCut4() {
		int diff = compareMatches("System.out.println(); --this.a;", "System.out.println(); this.a++;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_ShortCut5() {
		int diff = compareMatches("System.out.println(); --this.a;", "System.out.println(); ++this.a;");
		assertEquals(1, diff);
	}
	
	@Test
	public void testAOR_ShortCut6() {
		int diff = compareMatches("System.out.println(); ++this.a;", "System.out.println(); --this.a;");
		assertEquals(1, diff);
	}
}
