package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.lod.LOD;

import org.junit.Test;

import utils.MethodTest;

public class LOD_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new LOD();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a = 42; " +
				"int b = 1;";
	}

	@Test
	public void testLOD_constant1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = ~42; System.out.println();", "this.a = 42; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOD_constant2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = ~(1 + 2 + 3); System.out.println();", "this.b = (1 + 2 + 3); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOD_field1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = ~this.a; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOD_field2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = ~(this.a + this.b); System.out.println();", "this.b = (this.a + this.b); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
