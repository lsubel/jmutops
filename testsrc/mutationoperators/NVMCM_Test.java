package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.nvmcm.NVMCM;

import org.junit.Test;

import utils.MethodTest;

public class NVMCM_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new NVMCM();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int counter = 0; " + 
				"String name = \"\";" +
				"public void resetCounter(){counter = 0;} " +
				"public void resetName(){name = \"\";} " +
				"public int getCounter(){return counter;} " +
				"public boolean isZero(){return (counter == 0);} " +
				"public boolean isOne(){return (counter == 1);} " +
				"public int getNameLength(){return name.length();} " + 
				"public String getName(){return name;} " +
				"public char getFirstCharOfName(){return name.charAt(0); }" +
				"public void addSubstring(String arg){name = name + arg;} " +
				"public void incrementCounter(int value){counter += value;} " +
				"public void incrementCounter(byte value){counter += value;} " +
				"public void incrementCounter(int value, int value2){counter += value + value2;} " +
				"public void resetCounter(int newValue){counter = newValue;} " +
				"public void addValues(int a, String b){counter += a; name = name + b;} " +
				"public void addValues(String b, int a){counter += a; name = name + b;} "
				;
	}
	
	@Test 
	public void testNVMCM_RemoveLocalMethod1() {
		String pre 	= "resetCounter(); if(isZero()) {int result = getCounter(); System.out.println(result);} ";
		String post = "resetCounter(); if(false) {int result = getCounter(); System.out.println(result);} ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop); 
	}

	@Test 
	public void testNVMCM_RemoveLocalMethod2() {
		String pre 	= "resetName(); name = \"NAME\"; int length = getNameLength(); System.out.println(length);";
		String post = "resetName(); name = \"NAME\"; int length = 0; System.out.println(length);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test 
	public void testNVMCM_RemoveLocalMethod3() {
		String pre 	= "resetCounter(); incrementCounter(1); System.out.println(getFirstCharOfName());";
		String post = "resetCounter(); incrementCounter(1); System.out.println('\u0000');";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop); 
	}
	
	@Test 
	public void testNVMCM_RemoveLocalMethod4() {
		String pre 	= "resetCounter(); incrementCounter(1); String s = getName(); ";
		String post = "resetCounter(); incrementCounter(1); String s = null; ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop); 
	}

}
