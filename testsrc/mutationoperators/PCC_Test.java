package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.pcc.PCC;
import mutationoperators.methodlevel.tro.TRO_Methodlevel;

import org.junit.Test;

import utils.MethodTest;

public class PCC_Test extends MethodTest {

	MutationOperator mutop_pcc;
	MutationOperator mutop_tro;
	MutationOperator mutop_aco;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_pcc = new PCC();
		this.addMutationOperatorToTest(mutop_pcc);
		this.mutop_tro = new TRO_Methodlevel();
		this.addMutationOperatorToTest(mutop_tro);
		this.mutop_aco = new ACO();
		this.addMutationOperatorToTest(mutop_aco);
	}

	@Override
	protected String getOtherClassContent() {
		return  "Integer i = new Integer(4); " +
				"String s = \"Hello\";";
	}
	
	@Override
	protected void initializeContextFiles() {
		String fileContentComputer = 
			"int numberOfProcessor = 1; " +
			"int RAMinMB = 1024; " +
			"float sizeOfDisplay; " +
			"public int getProcessorNumber(){return numberOfProcessor;} " +
			"public float getDisplaySize(){return sizeOfDisplay;}";
		addContextSourceFile("Computer", surroundWithClass("Computer", fileContentComputer));
		
		String fileContentSmartphone = 
				"int hoursOfEnergy = 8; " +
				"public int getEnergy(){return hoursOfEnergy;} ";
			addContextSourceFile("Smartphone", surroundWithClass("Smartphone", "Computer", "", fileContentSmartphone));
	}
	
	@Test
	public void testPCC_ClassField1() {
		String pre = 	"System.out.println(); ((Integer) this.i).toString(); System.out.println(this.s);";
		String post = 	"System.out.println(); ((Object) this.i).toString(); System.out.println(this.s);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pcc)); 
		assertEquals(1, getApplicationValue(resultMap, mutop_tro)); 
		assertEquals(0, getApplicationValue(resultMap, mutop_aco)); 
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testPCC_ClassField2() {
		String pre = 	"if(((Object) this.s).hashCode() > 0){System.out.println(this.s);} ";
		String post = 	"if(((String) this.s).hashCode() > 0){System.out.println(this.s);} ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(1, getApplicationValue(resultMap, mutop_tro)); 
		assertEquals(0, getApplicationValue(resultMap, mutop_aco)); 
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testPCC_LocalVar1() {
		String pre = 	"Object c = new Smartphone(); System.out.println(((Computer) c).getProcessorNumber());";
		String post = 	"Object c = new Smartphone(); System.out.println(((Smartphone) c).getProcessorNumber());";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(1, getApplicationValue(resultMap, mutop_tro)); 
		assertEquals(1, getApplicationValue(resultMap, mutop_aco)); 
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testPCC_LocalVar2() {
		String pre = 	"Smartphone c = new Smartphone(); System.out.println(((Computer) c).equals(null));";
		String post = 	"Smartphone c = new Smartphone(); System.out.println(((Object) c).equals(null));";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_pcc));
		assertEquals(1, getApplicationValue(resultMap, mutop_tro)); 
		assertEquals(1, getApplicationValue(resultMap, mutop_aco)); 
		checkOtherMutationOperators(resultMap);
	}
}
