package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.pci.PCI;

import org.junit.Ignore;
import org.junit.Test;


public class PCI_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new PCI();
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
	public void testPCI_ClassField1() {
		String pre = 	"System.out.println(); (this.i).toString(); System.out.println(this.s);";
		String post = 	"System.out.println(); ((Object) this.i).toString(); System.out.println(this.s);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop)); 
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPCI_ClassField2() {
		String pre = 	"if((this.s).hashCode() > 0){System.out.println(this.s);} ";
		String post = 	"if(((Object) this.s).hashCode() > 0){System.out.println(this.s);} ";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPCI_LocalVar1() {
		String pre = 	"Computer c = new Smartphone(); int numbers = c.getProcessorNumber(); System.out.println(\"Numbers:\" + numbers);";
		String post = 	"Computer c = new Smartphone(); int numbers = (Smartphone) c.getProcessorNumber(); System.out.println(\"Numbers:\" + numbers);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop)); 
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testPCI_LocalVar2() {
		String pre = 	"Smartphone c = new Smartphone(); boolean theSame = c.equals(null);";
		String post = 	"Smartphone c = new Smartphone(); boolean theSame = (Object) c.equals(null);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	@Ignore
	public void testPCI_FailingTest1() {
		// FIXME test case fails due to the brackets around the cast
		String pre = 	"Smartphone c = new Smartphone(); c.equals(null);";
		String post = 	"Smartphone c = new Smartphone(); ((Object) c).equals(null);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
}
