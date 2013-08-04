package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.pcd.PCD;

import org.junit.Test;

import utils.MethodTest;

public class PCD_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new PCD();
		this.addMutationOperatorToTest(mutop);
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
	public void testPCDClassField1() {
		String pre = 	"System.out.println(); ((Object) this.i).toString(); System.out.println(this.s);";
		String post = 	"System.out.println(); (this.i).toString(); System.out.println(this.s);";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue()); 
	}
	
	@Test
	public void testPCD_ClassField2() {
		String pre = 	"if(((Object) this.s).hashCode() > 0){System.out.println(this.s);} ";
		String post = 	"if((this.s).hashCode() > 0){System.out.println(this.s);} ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testPCD_LocalVar1() {
		String pre = 	"Computer c = new Smartphone(); System.out.println((Smartphone) c.getProcessorNumber());";
		String post = 	"Computer c = new Smartphone(); System.out.println(c.getProcessorNumber());";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue()); 
	}
	
	@Test
	public void testPCD_LocalVar2() {
		String pre = 	"Smartphone c = new Smartphone(); System.out.println((Object) c.equals(null));";
		String post = 	"Smartphone c = new Smartphone(); System.out.println(c.equals(null));";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
	
	@Test
	public void testPCD_FailingTest1() {
		String pre = 	"Smartphone c = new Smartphone(); System.out.println(((Object) c).equals(null));";
		String post = 	"Smartphone c = new Smartphone(); System.out.println(c.equals(null));";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
	}
}
