package mutationoperators;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.mnro.MNRO;

import org.junit.Test;

import utils.MethodTest;



public class MNRO_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new MNRO();
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
	public void testMNRO_Match1() {
		HashMap<String, Integer> resultMap = compareMatches("resetCounter(); incrementCounter(1); incrementCounter(5); int result = getCounter();", "resetName(); incrementCounter(1); incrementCounter(5); int result = getCounter();");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_Match2() {
		HashMap<String, Integer> resultMap = compareMatches("System.out.println(); int a = getCounter(); incrementCounter(a);", "System.out.println(); int a = getNameLength(); incrementCounter(a);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_Match3() {
		HashMap<String, Integer> resultMap = compareMatches("incrementCounter(5);", "resetCounter(5);");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_Match4() {
		HashMap<String, Integer> resultMap = compareMatches("if(isZero()){incrementCounter(1);};", "if(isOne()){incrementCounter(1);};");
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentReturn1() {
		HashMap<String, Integer> resultMap = compareMatches("resetCounter();", "getCounter();");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentReturn2() {
		HashMap<String, Integer> resultMap = compareMatches("getName();", "getCounter();");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentReturn3() {
		HashMap<String, Integer> resultMap = compareMatches("getName();", "isOne();");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentReturn4() {
		HashMap<String, Integer> resultMap = compareMatches("getName();", "resetName();");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentArgLength1() {
		HashMap<String, Integer> resultMap = compareMatches("resetCounter();", "incrementCounter(1);");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentArgLength2() {
		HashMap<String, Integer> resultMap = compareMatches("resetCounter();", "addSubstring(\"Test\");");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentArgLength3() {
		HashMap<String, Integer> resultMap = compareMatches("incrementCounter(1);", "incrementCounter(1, 2);");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testMNRO_DifferentArgType1() {
		HashMap<String, Integer> resultMap = compareMatches("int a = 0; incrementCounter(a);", "byte a = 0; incrementCounter(a);");
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
