package mutationoperators;
import static org.junit.Assert.*;

import mutationoperators.mnro.MNRO;

import org.junit.Test;

import utils.BasicMutationOperatorTest;



public class MNRO_Test extends BasicMutationOperatorTest {

	@Override
	protected String getOperatorName() {
		return new MNRO().getShortname();
	}

	@Override
	protected String getFields() {
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
		int diff = compareMatches("resetCounter(); incrementCounter(1); incrementCounter(5); int result = getCounter();", "resetName(); incrementCounter(1); incrementCounter(5); int result = getCounter();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_Match2() {
		int diff = compareMatches("System.out.println(); int a = getCounter(); incrementCounter(a);", "System.out.println(); int a = getNameLength(); incrementCounter(a);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_Match3() {
		int diff = compareMatches("incrementCounter(5);", "resetCounter(5);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_Match4() {
		int diff = compareMatches("if(isZero()){incrementCounter(1);};", "if(isOne()){incrementCounter(1);};");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_DifferentReturn1() {
		int diff = compareMatches("resetCounter();", "getCounter();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentReturn2() {
		int diff = compareMatches("getName();", "getCounter();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentReturn3() {
		int diff = compareMatches("getName();", "isOne();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentReturn4() {
		int diff = compareMatches("getName();", "resetName();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentArgLength1() {
		int diff = compareMatches("resetCounter();", "incrementCounter(1);");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentArgLength2() {
		int diff = compareMatches("resetCounter();", "addSubstring(\"Test\");");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentArgLength3() {
		int diff = compareMatches("incrementCounter(1);", "incrementCounter(1, 2);");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_DifferentArgType1() {
		int diff = compareMatches("int a = 0; incrementCounter(a);", "byte a = 0; incrementCounter(a);");
		assertEquals(0, diff);
	}
}
