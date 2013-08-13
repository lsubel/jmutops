package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.pmd.PMD;

import org.junit.Test;

import utils.MethodTest;

public class PMD_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new PMD();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0;" +
				"Parent p; ";
	}

	@Override
	protected void initializeContextFiles() {
		String fileContentParent =
			"public int value; " +
			"public String name = \"\"; " +
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
		String pre 	= "System.out.println(this.a); Child b; b = new Child(); System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Parent b; b = new Child(); System.out.println(b.toString());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPNC_Test2(){
		String pre 	= "System.out.println(this.a); Grandchild b; b = new Grandchild(); System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Parent b; b = new Grandchild(); System.out.println(b.toString());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPNC_Test3(){
		String pre 	= "System.out.println(this.a); Parent b; b = new Parent(); System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Object b; b = new Parent(); System.out.println(b.toString());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPNC_Test4(){
		String pre 	= "System.out.println(this.a); String b; b = \"Hello World\"; System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Object b; b = \"Hello World\"; System.out.println(b.toString());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
