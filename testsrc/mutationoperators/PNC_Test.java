package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.pnc.PNC;

import org.junit.Test;

import utils.MethodTest;

public class PNC_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new PNC();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"public Stack st1 = new Stack(); " +
				"public Stack st2 = new Stack(); "; 
	}

	@Override
	protected void initializeContextFiles() {
		String fileContentParent =
			"int value; " +
			"String name = \"\"; " +
			"public Parent(){this.value = 0;} ";
		addContextSourceFile("Parent", surroundWithClass("Parent", fileContentParent));
		
		String fileContentChild = 
			"public Child(){this.value = 1;} ";
		addContextSourceFile("Child", surroundWithClass("Child", "Parent", "", fileContentChild));
		
		String fileContentGrandchild = 
				"public Grandchild(){this.value = 2;} ";
			addContextSourceFile("Grandchild", surroundWithClass("Grandchild", "Child", "", fileContentGrandchild));
	}
	
	@Test
	public void testPNC_Test1(){
		String pre 	= "Parent p; p = new Parent();";
		String post	= "Parent p; p = new Child();";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testPNC_Test2(){
		String pre 	= "Parent p; p = new Parent();";
		String post	= "Parent p; p = new Grandchild();";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testPNC_Test3(){
		String pre 	= "Object p; p = new Grandchild();";
		String post	= "Object p; p = new Child();";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
}
