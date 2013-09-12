package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.prv.PRV;

import org.junit.Test;

import utils.MethodTest;

public class PRV_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new PRV();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "Object obj;" + 
				"String s1 = \"Hello\";" +
				"SubClass1 sc1 = null; " +
				"RootClass rc = null; ";
	}
	
	@Override
	protected void initializeContextFiles() {
		String fileContentBarRoot =  
			"String name = \"Name\"; " +
			"Integer id = new Integer(1); " ;
		addContextSourceFile("RootClass", surroundWithClass("RootClass", fileContentBarRoot));
		
		String fileContentBarSubClass1 =  
				"Object specialField = null; " ;
		addContextSourceFile("SubClass1", surroundWithClass("SubClass1", "RootClass", "", fileContentBarSubClass1));
	}

	@Test
	public void testPRV_LocalObject1() {
		String pre 	= "Object obj; String s = \"Hello\"; Integer i = new Integer(4); obj = s; ";
		String post = "Object obj; String s = \"Hello\"; Integer i = new Integer(4); obj = i; ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPRV_LocalObject2() {
		String pre 	= "Object objtarget; RootClass rc = new RootClass(); SubClass1 sc1 = new SubClass1(); objtarget = rc; ";
		String post = "Object objtarget; RootClass rc = new RootClass(); SubClass1 sc1 = new SubClass1(); objtarget = sc1; ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPRV_LocalObject3() {
		String pre 	= "RootClass rctarget; RootClass rcsrc = new RootClass(); SubClass1 scsrc = new SubClass1(); rctarget = rcsrc; ";
		String post = "RootClass rctarget; RootClass rcsrc = new RootClass(); SubClass1 scsrc = new SubClass1(); rctarget = scsrc; ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPRV_ClassField1() {
		String pre 	= "RootClass rcl = new RootClass(); SubClass1 sc1 = new SubClass1(); this.rc = rcl; System.out.println();";
		String post = "RootClass rcl = new RootClass(); SubClass1 sc1 = new SubClass1(); this.rc = sc1; System.out.println();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop)); 
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPRV_ClassField2() {
		String pre 	= "Integer i = new Integer(4); this.obj = this.s1; ";
		String post = "Integer i = new Integer(4); this.obj = i; ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPRV_NoMatching1() {
		String pre 	= "Integer i = new Integer(4); Integer i2 = new Integer(5); this.obj = i; ";
		String post = "Integer i = new Integer(4); Integer i2 = new Integer(5); this.obj = i2; ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
