package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.vro.VRO;

import org.junit.Test;

import utils.MethodTest;

public class VRO_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new VRO();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "String message = \"HELLO MESSAGE HERE!\"; " +
				"int i1 = 42; " +
				"int i2 = 1;	" +
				"Bar b = new Bar(); " +
				"String str1 = \"Hello\"; " +
				"Object o = null; ";
	}
	
	@Override
	protected void initializeContextFiles() {
		String fileContentBar =  
			"int counter = 0; " +
			"int result = 0; "+ 
			"public Bar subclass = null; " +
			"public Bar parentclass = null; " +
			"Foo other = null; " + 
			"public void increment(){counter += 1;} " +
			"public int getCounter(){return counter;} ";
		addContextSourceFile("Bar", surroundWithClass("Bar", fileContentBar));
	}
	
	@Test
	public void testVRO_FieldToLocalVariable1() {
		String pre 	= "int x = 0; System.out.println(i1 + i2);";
		String post = "int x = 0; System.out.println(x + i2);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testVRO_FieldToLocalVariable2() {
		String pre 	= "int x = 0; int y = 1; System.out.println(i1 + i2);";
		String post = "int x = 0; int y = 1; System.out.println(x + y);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_FieldToField1() {
		String pre 	= "int x = 0; int y = 1; x += y; System.out.println(i1 + i2);";
		String post = "int x = 0; int y = 1; x += y; System.out.println(i1 + i1);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_FieldToField2() {
		String pre 	= "String message = \"MESSAGE\"; message = str1; System.out.println(message);";
		String post = "String message = \"MESSAGE\"; message = o; System.out.println(message);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_MethodCallExchange1() {
		String pre 	= "this.b.subclass = new Bar(); this.b.parentclass = new Bar(); System.out.println(this.b.subclass.getCounter());";
		String post = "this.b.subclass = new Bar(); this.b.parentclass = new Bar(); System.out.println(this.b.parentclass.getCounter());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_MethodCallExchange2() {
		String pre 	= "this.b.subclass = this.b; this.b.parentclass = this.b; System.out.println(this.b.parentclass.subclass.parentclass.getCounter());";
		String post = "this.b.subclass = this.b; this.b.parentclass = this.b; System.out.println(this.b.getCounter());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_VariableToConstant1() {
		String pre 	= "int x = 0; int y = 1; x += y; System.out.println(i1 + i2);";
		String post = "int x = 0; int y = 1; x += 42; System.out.println(i1 + i2);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_VariableToConstant2() {
		String pre 	= "int x = 0; int y = 1; x += y; System.out.println(i1 + i2);";
		String post = "int x = 0; int y = 1; x += y; System.out.println(1 + 2);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testVRO_VariableToConstant3() {
		String pre 	= "String output = message; System.out.println(output);";
		String post = "String output = \"MESSAGE\"; System.out.println(output);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}