package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aoi.AOI;

import org.junit.Test;


public class AOI_Test extends MethodTest {

MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new AOI();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a = 0; " + 
				"int b = 100; ";
	}

	@Test
	public void testAOI_unaryInsertion1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a = this.b;", "this.a = +this.b;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOI_unaryInsertion() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = this.a;", "this.b = -this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOI_unaryInsertion3() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = (this.a + 10);", "this.b = -(this.a + 10);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOI_unaryInsertion4() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = 1;", "this.b = +1;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutInsertion1() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); this.b = this.a;", "System.out.println(); this.b = ++this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutInsertion2() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = this.a; System.out.println();", "this.b = --this.a; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutInsertion3() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = this.a; System.out.println();", "this.b = this.a--; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutInsertion4() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = this.a; System.out.println();", "this.b = this.a++; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutInsertionForLoop1() {
		HashMap<String, Integer> resultMap = compareMatches("for(int i = 0; i<a; i++){System.out.println();}", "for(int i = 0; i<a--; i++){System.out.println();}");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutInsertionWhileLoop1() {
		HashMap<String, Integer> resultMap = compareMatches("while(a < b){System.out.println();b-=1;}", "while(a++ < b){System.out.println();b-=1;}");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
