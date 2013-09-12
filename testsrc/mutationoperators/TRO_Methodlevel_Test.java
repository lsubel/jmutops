package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.tro.TRO_Methodlevel;

import org.junit.Test;

import utils.MethodTest;

public class TRO_Methodlevel_Test extends MethodTest {

MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new TRO_Methodlevel();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"String name = \"MY NAME\"; " +
				"Object o = null; " +
				"Parent p = new Parent(); ";
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
	public void testTRO_LocalVariable1(){
		String pre 	= "Parent p1 = new Child(); p1.value = 42; System.out.println(p1.name);";
		String post	= "Child p1 = new Child(); p1.value = 42; System.out.println(p1.name);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testTRO_LocalVariable2(){
		String pre 	= "Object p1 = new String(); System.out.println(p1.toString());";
		String post	= "String p1 = new String(); System.out.println(p1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testTRO_LocalVariable3(){
		String pre 	= "Child p1 = new Grandchild(); p1.name = \"GRANDCHILD\"; System.out.println(p1.toString());";
		String post	= "Grandchild p1 = new Grandchild(); p1.name = \"GRANDCHILD\"; System.out.println(p1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testTRO_CastExpression1(){
		String pre 	= "Parent p1 = new Grandchild(); Grandchild g1 = (Child) p1; System.out.println(g1.toString());";
		String post	= "Parent p1 = new Grandchild(); Grandchild g1 = (Grandchild) p1; System.out.println(g1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testTRO_CastExpression2(){
		String pre 	= "Object o1 = \"NAME\"; String s1 = (Object) o1; System.out.println(s1.toString());";
		String post	= "Object o1 = \"NAME\"; String s1 = (String) o1; System.out.println(s1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop)); 
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testTRO_ExceptionParamter1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (Exception e) {System.out.println(\"NullPointerException\");}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEHC_ThrowException1(){
		String pre 	= "if(this.name.equals(\"EXCEPTION\")) {throw new IllegalStateException(\"Illegal State\");}"; 
		String post	= "if(this.name.equals(\"EXCEPTION\")) {throw new Exception(\"Illegal State\");}"; 
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
