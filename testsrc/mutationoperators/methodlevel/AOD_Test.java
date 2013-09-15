package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.aod.AOD;

import org.junit.Test;


public class AOD_Test extends MethodTest {


	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new AOD();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a = 0; " + 
				"int b = 100; ";
	}

	@Test
	public void testAOD_unaryDeletion1() {
		HashMap<String, Integer> resultMap = compareMatches("this.a = +this.b;", "this.a = this.b;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_unaryDeletion2() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = -this.a;", "this.b = this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_unaryDeletion3() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = -(this.a + 10);", "this.b = (this.a + 10);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_unaryDeletion4() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = +1;", "this.b = 1;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutDeletion1() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); this.b = ++this.a;", "System.out.println(); this.b = this.a;");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutDeletion2() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = --this.a; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutDeletion3() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = this.a--; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutDeletion4() {
		HashMap<String, Integer> resultMap = compareMatches("this.b = this.a++; System.out.println();", "this.b = this.a; System.out.println();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutDeletionForLoop1() {
		HashMap<String, Integer> resultMap = compareMatches("for(int i = 0; i<a--; i++){System.out.println();}", "for(int i = 0; i<a; i++){System.out.println();}");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testAOD_shortcutDeletionWhileLoop1() {
		HashMap<String, Integer> resultMap = compareMatches("while(a++ < b){System.out.println();b-=1;}", "while(a < b){System.out.println();b-=1;}");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
