package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.cro.CRO;
import mutationoperators.methodlevel.pnc.PNC;
import mutationoperators.methodlevel.vro.VRO;

import org.junit.Test;


public class PNC_Test extends MethodTest {

	MutationOperator mutop_pnc;
	MutationOperator mutop_cro;
	MutationOperator mutop_vro;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_pnc = new PNC();
		this.addMutationOperatorToTest(mutop_pnc);
		this.mutop_cro = new CRO();
		this.addMutationOperatorToTest(mutop_cro);
		this.mutop_vro = new VRO();
		this.addMutationOperatorToTest(mutop_vro);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 0;" +
				"int b = 42; " +
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
		String pre 	= "this.p = new Parent(); p.value = this.a; System.out.println(p.name);";
		String post	= "this.p = new Child(); p.value = this.a; System.out.println(p.name);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testPNC_Test2(){ 
		String pre 	= "this.p = new Parent(); 		p.value = this.a; System.out.println(p.name);";
		String post	= "this.p = new Grandchild();  p.value = this.a; System.out.println(p.name);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testPNC_Test3(){
		String pre 	= "Child p; p = new Grandchild(); p.value = this.a; System.out.println(p.name);";
		String post	= "Child p; p = new Child(); p.value = this.a; System.out.println(p.name);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testPNC_NoMatching1(){
		String pre 	= "Child p; Child q; new Grandchild().value = this.a; System.out.println(p.name);";
		String post	= "Child p; Child q; new Grandchild().value = 42; System.out.println(p.name);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_vro));
		checkOtherMutationOperators(resultMap);
	}
}
