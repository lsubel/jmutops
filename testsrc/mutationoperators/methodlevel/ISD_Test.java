 package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.InheritTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.isd.ISD;

import org.junit.Test;


public class ISD_Test extends InheritTest {
	
	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new ISD();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getExtends() {
		return "SubClass11";
	}

	@Override
	protected String getImplements() {
		return "";
	}
	
	@Override
	protected String getOtherClassContent() {
		return 	"SubClass11 sc11 = new SubClass11(); " + 
				"SubClass1  sc1  = new SubClass1(); ";
	}

	@Override
	protected void initializeContextFiles() {
		String fileContentRootClass = 
			"String name = \"Root\";" +
			"int value = 0;" + 
			"public String getName(){return \"Root\";} " + 
			"public int getValue(){return 0;} ";
		addContextSourceFile("RootClass", surroundWithClass("RootClass", fileContentRootClass));
		
		String fileContentSubClass1 =
				"String name = \"SubClass1\";" +
				"int value = 1;" + 
				"public String getName(){return \"SubClass1\";} " + 
				"public int getValue(){return 1;} ";
		addContextSourceFile("SubClass1", surroundWithClass("SubClass1", "RootClass", "", fileContentSubClass1));
		
		String fileContentSubClass11 =
				"String name = \"SubClass11\";" +
				"int value = 11;" + 
				"public String getName(){return \"SubClass11\";} " + 
				"public int getValue(){return 11;} ";
		addContextSourceFile("SubClass11", surroundWithClass("SubClass11", "SubClass1", "", fileContentSubClass11));
	}

	@Test
	public void testISD_Field1(){
		String pre 	= "int v = super.value;  System.out.println(v);";
		String post	= "int v = value;  System.out.println(v);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testISD_Field2(){
		String pre 	= "String n = super.name;  System.out.println(n);";
		String post	= "String n = name;  System.out.println(n);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testISD_Method1(){
		String pre 	= "int v = super.getValue();  System.out.println(v);";
		String post	= "int v = getValue();  System.out.println(v);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testISD_Method2(){
		String pre 	= "String n = super.getName();  System.out.println(n);";
		String post	= "String n = getName();  System.out.println(n);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
