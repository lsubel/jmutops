package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.vmcm.VMCM;

import org.junit.Test;

import utils.MethodTest;

public class VMCM_Test extends MethodTest {

MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new VMCM();
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
	public void testVMCM_RemoveLocalMethod1() {
		String pre 	= "resetCounter(); incrementCounter(1); incrementCounter(5); int result = getCounter(); System.out.println(result);";
		String post = "resetCounter(); incrementCounter(5); int result = getCounter(); System.out.println(result);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop); 
	}

	@Test 
	public void testVMCM_RemoveLocalMethod2() {
		String pre 	= "resetCounter(); incrementCounter(1); incrementCounter(5); int result = getCounter(); System.out.println(result);";
		String post = "resetCounter(); incrementCounter(1); incrementCounter(5); int result = getCounter();";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop); 
	}
	
	@Test 
	public void testVMCM_RemoveLocalMethod3() {
		String pre 	= "resetCounter(); incrementCounter(1); System.out.println(getName());";
		String post = "resetCounter(); incrementCounter(1); ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop); 
	}
	
	@Test 
	public void testVMCM_RemoveLocalMethod4() {
		String pre 	= "resetCounter(); incrementCounter(1); this.getName().notify(); ";
		String post = "resetCounter(); incrementCounter(1); ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop); 
	}
}
