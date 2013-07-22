package mutationoperators;

import static org.junit.Assert.*;

import mutationoperators.lod.LOD;

import org.junit.Test;

public class LOD_Test extends BasicMutationOperatorTest {

	@Override
	protected String getOperatorName() {
		return new LOD().getShortname();
	}

	@Override
	protected String getFields() {
		return  "int a = 42; " +
				"int b = 1;";
	}

	@Test
	public void testLOD_constant1(){
		int diff = compareMatches("this.a = ~42; System.out.println();", "this.a = 42; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOD_constant2(){
		int diff = compareMatches("this.b = ~(1 + 2 + 3); System.out.println();", "this.b = (1 + 2 + 3); System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOD_field1(){
		int diff = compareMatches("this.b = ~this.a; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testLOD_field2(){
		int diff = compareMatches("this.b = ~(this.a + this.b); System.out.println();", "this.b = (this.a + this.b); System.out.println();");
		assertEquals(1, diff);
	}
}