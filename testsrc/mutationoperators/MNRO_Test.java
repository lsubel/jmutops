package mutationoperators;
import static org.junit.Assert.*;

import mutationoperators.mnro.MNRO;

import org.junit.Test;



public class MNRO_Test extends BasicMutationOperatorTest {

	@Override
	protected String getOperatorName() {
		return MNRO.fullname;
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
	public void testMNRO_match1() {
		int diff = compareMatches("resetCounter();", "resetName();");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_match2() {
		int diff = compareMatches("int a = getCounter(); incrementCounter(a);", "int a = getNameLength(); incrementCounter(a);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_match3() {
		int diff = compareMatches("incrementCounter(5);", "resetCounter(5);");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_match4() {
		int diff = compareMatches("if(isZero()){incrementCounter(1);};", "if(isOne()){incrementCounter(1);};");
		assertEquals(1, diff);
	}
	
	@Test
	public void testMNRO_differentReturn1() {
		int diff = compareMatches("resetCounter();", "getCounter();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentReturn2() {
		int diff = compareMatches("getName();", "getCounter();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentReturn3() {
		int diff = compareMatches("getName();", "isOne();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentReturn4() {
		int diff = compareMatches("getName();", "resetName();");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentArgLength1() {
		int diff = compareMatches("resetCounter();", "incrementCounter(1);");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentArgLength2() {
		int diff = compareMatches("resetCounter();", "addSubstring(\"Test\");");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentArgLength3() {
		int diff = compareMatches("incrementCounter(1);", "incrementCounter(1, 2);");
		assertEquals(0, diff);
	}
	
	@Test
	public void testMNRO_differentArgType1() {
		int diff = compareMatches("int a = 0; incrementCounter(a);", "byte a = 0; incrementCounter(a);");
		assertEquals(0, diff);
	}
}
