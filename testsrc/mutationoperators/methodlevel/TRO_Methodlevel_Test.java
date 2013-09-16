package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.cro.CRO;
import mutationoperators.methodlevel.exco.EXCO_Update;
import mutationoperators.methodlevel.pcc.PCC;
import mutationoperators.methodlevel.pnc.PNC;
import mutationoperators.methodlevel.tro.TRO_Methodlevel;

import org.junit.Test;


public class TRO_Methodlevel_Test extends MethodTest {

	MutationOperator mutop_tro_methodlevel;
	MutationOperator mutop_pcc;
	MutationOperator mutop_pnc;
	MutationOperator mutop_exco;
	MutationOperator mutop_cro;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_tro_methodlevel = new TRO_Methodlevel();
		this.addMutationOperatorToTest(mutop_tro_methodlevel);
		this.mutop_pcc = new PCC();
		this.addMutationOperatorToTest(mutop_pcc);
		this.mutop_pnc = new PNC();
		this.addMutationOperatorToTest(mutop_pnc);
		this.mutop_exco = new EXCO_Update();
		this.addMutationOperatorToTest(mutop_exco);
		this.mutop_cro = new CRO();
		this.addMutationOperatorToTest(mutop_cro);
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
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_exco));
		assertEquals(0, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testTRO_LocalVariable2(){
		String pre 	= "Object p1 = new String(); System.out.println(p1.toString());";
		String post	= "String p1 = new String(); System.out.println(p1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_exco));
		assertEquals(0, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testTRO_LocalVariable3(){
		String pre 	= "Child p1 = new Grandchild(); p1.name = \"GRANDCHILD\"; System.out.println(p1.toString());";
		String post	= "Grandchild p1 = new Grandchild(); p1.name = \"GRANDCHILD\"; System.out.println(p1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_exco));
		assertEquals(0, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testTRO_CastExpression1(){
		String pre 	= "Parent p1 = new Grandchild(); Grandchild g1 = (Child) p1; System.out.println(g1.toString());";
		String post	= "Parent p1 = new Grandchild(); Grandchild g1 = (Grandchild) p1; System.out.println(g1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_exco));
		assertEquals(1, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testTRO_CastExpression2(){
		String pre 	= "Object o1 = \"NAME\"; String s1 = (Object) o1; System.out.println(s1.toString());";
		String post	= "Object o1 = \"NAME\"; String s1 = (String) o1; System.out.println(s1.toString());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel)); 
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_exco));
		assertEquals(1, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testTRO_ExceptionParamter1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (Exception e) {System.out.println(\"NullPointerException\");}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_exco));
		assertEquals(0, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testEHC_ThrowException1(){
		String pre 	= "if(this.name.equals(\"EXCEPTION\")) {throw new IllegalStateException(\"Illegal State\");}"; 
		String post	= "if(this.name.equals(\"EXCEPTION\")) {throw new Exception(\"Illegal State\");}"; 
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_tro_methodlevel));
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_exco));
		assertEquals(0, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		checkOtherMutationOperators(resultMap);
	}
}
