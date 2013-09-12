package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.exco.EXCO_Delete;
import mutationoperators.methodlevel.vmcm.VMCM;

import org.junit.Test;

import utils.MethodTest;

public class EXCO_Delete_Test extends MethodTest {

	private MutationOperator mutop_exco;
	private MutationOperator mutop_vmcm; 
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_exco = new EXCO_Delete();
		this.addMutationOperatorToTest(mutop_exco);
		this.mutop_vmcm = new VMCM();
		this.addMutationOperatorToTest(mutop_vmcm);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 1; " +
				"int b = 2; ";
	}
	
	@Test
	public void testEXCO_removeCatch1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");} catch (IllegalStateException e){System.out.println(\"IllegalStateException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_exco));
		assertEquals(1, getApplicationValue(resultMap, mutop_vmcm));
		checkOtherMutationOperators(resultMap);
	}
}
