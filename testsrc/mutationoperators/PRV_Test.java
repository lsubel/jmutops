package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.prv.PRV;

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
		return  "String s1 = \"Hello\";" +
				"SubClass1 sc1 = null; ";
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
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testPRV_LocalObject2() {
		String pre 	= "Object obj; RootClass rc = new RootClass(); SubClass1 sc1 = new SubClass1(); obj = rc; ";
		String post = "Object obj; RootClass rc = new RootClass(); SubClass1 sc1 = new SubClass1(); obj = sc1; ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testPRV_LocalObject3() {
		String pre 	= "RootClass obj; RootClass rc = new RootClass(); SubClass1 sc1 = new SubClass1(); obj = rc; ";
		String post = "RootClass obj; RootClass rc = new RootClass(); SubClass1 sc1 = new SubClass1(); obj = sc1; ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
}
