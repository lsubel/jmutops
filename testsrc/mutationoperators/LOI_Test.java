package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperators.methodlevel.loi.LOI;

import org.junit.Test;

import utils.MethodTest;

public class LOI_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new LOI();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a = 42; " +
				"int b = 1;";
	}

	@Test
	public void testLOI_constant1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.a = 42; System.out.println();", "this.a = ~42; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOI_constant2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = (1 + 2 + 3); System.out.println();", "this.b = ~(1 + 2 + 3); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOI_field1(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = this.a; System.out.println();", "this.b = ~this.a; System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testLOI_field2(){
		HashMap<MutationOperator, Integer> resultMap = compareMatches("this.b = (this.a + this.b); System.out.println();", "this.b = ~(this.a + this.b); System.out.println();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

}
