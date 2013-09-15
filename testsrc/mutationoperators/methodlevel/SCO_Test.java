package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.sco.SCO;

import org.junit.Test;


public class SCO_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected String getOtherClassContent() {
		return  "int a1 = 0; " +
				"int a2 = 1; " +
				"int a3 = 0; " +
				"boolean b1 = true; ";
	}

	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new SCO();
		this.addMutationOperatorToTest(mutop);
	}
	
	@Test
	public void testSCO_ToIfInnerBlock1(){
		String pre = "int result = 0; this.a2 = 2; if(this.b1){result += this.a1;}";
		String post = "int result = 0; if(this.b1){this.a2 = 2; result += this.a1;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSCO_ToIfInnerBlock2(){
		String pre = "System.out.println(0); int result = 0; this.a2 = 2; if(this.b1){result += this.a1;}";
		String post = "int result = 0; this.a2 = 2; if(this.b1){result += this.a1; System.out.println(0);}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSCO_ToIfOuterBlock1(){
		String pre = "System.out.println(0); int result = 0; this.a2 = 2; if(this.b1){result += this.a1; this.a2 = 0;}";
		String post = "System.out.println(0); int result = 0; this.a2 = 0; this.a2 = 2; if(this.b1){result += this.a1;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSCO_ToWhileInnerBlock1(){
		String pre = "this.a1 = 10; this.a2 = 0; while(this.a1 > 0){this.a1 -= 1; this.a3 += 1;}";
		String post = "this.a1 = 10; while(this.a1 > 0){this.a1 -= 1; this.a2 = 0; this.a3 += 1;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testSCO_ToWhileOuterBlock1(){
		String pre = "this.a1 = 10; this.a2 = 0; while(this.a1 > 0){this.a1 -= 1; this.a3 += 1;}";
		String post = "this.a3 += 1; this.a1 = 10; this.a2 = 0; while(this.a1 > 0){this.a1 -= 1;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSCO_ToTryBodyBlock1(){
		String pre = "this.a1 = 0; this.a2 = 1; try {this.a3 = this.a1 + 1;} catch (Exception e) {System.out.println(-1);} ";
		String post = "this.a2 = 1; try {this.a1 = 0; this.a3 = this.a1 + 1;} catch (Exception e) {System.out.println(-1);} ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSCO_ToTryExceptionBlock1(){
		String pre = "this.a1 = 0; this.a2 = 1; try {this.a3 = this.a1 + 1;} catch (Exception e) {System.out.println(-1);} ";
		String post = "this.a2 = 1; try {this.a1 = 0; this.a3 = this.a1 + 1;} catch (Exception e) {System.out.println(-1);} ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testSCO_NoMatch1(){
		String pre = "int result = 0; this.a2 = 2; if(this.b1){result += this.a1;}";
		String post = "this.a2 = 2; int result = 0; if(this.b1){result += this.a1;}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
