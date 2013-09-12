package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.rvm.RVM;

import org.junit.Test;

import utils.ClassTest;

public class RMV_Test extends ClassTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new RVM();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"boolean a = true; " +
				"String name = \"NAME\"; " + 
				"int value = 0; " +
				"Object o = null; " + 
				"Building b = new Building(); ";
	}
	
	@Override
	protected void initializeContextFiles() {
		String fileContentBuilding = 
			"String name; " + 
 			"String location; " +
			"int priceInDollar; " +
			"int age; " +
			"public Building(){age = 0;}" +
			"public Building(int price, int years){age = years; priceInDollar = price;}"; 
		addContextSourceFile("Building", surroundWithClass("Building", fileContentBuilding));
	}
	
	@Test
	public void testRVM_ChangeBoolean1(){
		String pre 	= "public boolean sameName(String x) {System.out.println(\"INIT\"); if(name.equals(x)){System.out.println(\"CONDITION TRUE\"); return true;} else {System.out.println(\"CONDITION FALSE\"); return false;} }";
		String post	= "public boolean sameName(String x) {System.out.println(\"INIT\");  if(name.equals(x)){System.out.println(\"CONDITION TRUE\"); return true;} else {System.out.println(\"CONDITION FALSE\"); return true;} }";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop)); 
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testRVM_ChangeInteger1(){
		String pre 	= "public int getState() {System.out.println(\"INIT\");  if(value != 0){System.out.println(\"CONDITION TRUE\"); this.value = 42; return 15235;} System.out.println(name + \": CONDITION FALSE\"); return 4; }";
		String post	= "public int getState() {System.out.println(\"INIT\");  if(value != 0){System.out.println(\"CONDITION TRUE\"); this.value = 42; return 0;} System.out.println(name + \": CONDITION FALSE\"); return 4; }";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

	@Test
	public void testRVM_ChangeFloat1(){
		String pre 	= "public float getDiff(float f1, float f2) {System.out.println(\"INIT\");  return 1.0; }";
		String post	= "public float getDiff(float f1, float f2) {System.out.println(\"INIT\");  return -2.0; }";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testRVM_ChangeObject1(){
		String pre 	= "public String getErrorMessage(int error_code) {System.out.println(\"INIT\");  if(error_code != 0){System.out.println(\"ERROR 1\"); return \"ERROR_MESSAGE_1\";} else if(error_code != 1){System.out.println(\"ERROR 2\"); return \"ERROR_MESSAGE_2\";} else System.out.println(\"ERROR 3\"); return \"ERROR_MESSAGE_3\"; }";
		String post	= "public String getErrorMessage(int error_code) {System.out.println(\"INIT\");  if(error_code != 0){System.out.println(\"ERROR 1\"); return null;} else if(error_code != 1){System.out.println(\"ERROR 2\"); return null;} else System.out.println(\"ERROR 3\"); return \"ERROR_MESSAGE_3\"; }";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
}
