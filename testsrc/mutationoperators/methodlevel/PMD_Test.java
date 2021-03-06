package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.pmd.PMD;

import org.junit.Test;


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
	public void testPMD_Test1(){
		String pre 	= "System.out.println(this.a); Child b; b = new Child(); System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Parent b; b = new Child(); System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPMD_Test2(){
		String pre 	= "System.out.println(this.a); Grandchild b; b = new Grandchild(); System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Parent b; b = new Grandchild(); System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPMD_Test3(){
		String pre 	= "System.out.println(this.a); Parent b; b = new Parent(); System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Object b; b = new Parent(); System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPMD_Test4(){
		String pre 	= "System.out.println(this.a); String b; b = \"Hello World\"; System.out.println(b.toString());";
		String post	= "System.out.println(this.a); Object b; b = \"Hello World\"; System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPMD_TestSecondLevel1(){
		String pre 	= "System.out.println(this.a); if(true) {String b; b = \"Hello World\"; } System.out.println(b.toString());";
		String post	= "System.out.println(this.a); if(true) {Object b; b = \"Hello World\"; } System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPMD_TestSecondLevel2(){
		String pre 	= "System.out.println(this.a); while(true) {String b; b = \"Hello World\"; break; } System.out.println(b.toString());";
		String post	= "System.out.println(this.a); while(true) {Object b; b = \"Hello World\"; break;} System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPMD_TestThirdLevel1(){
		String pre 	= "System.out.println(this.a); if(true) { System.out.println(\"Reached first level\"); while(true) {String b; b = \"Hello World\"; break; }} System.out.println(b.toString());";
		String post	= "System.out.println(this.a); if(true) { System.out.println(\"Reached first level\"); while(true) {Object b; b = \"Hello World\"; break;}} System.out.println(b.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
