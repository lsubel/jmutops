package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.ehc.EHC;

import org.junit.Test;

import utils.ClassTest;

public class EHC_Test extends ClassTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EHC();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"boolean a = true; " +
				"boolean b = false;" +
				"boolean c = false;" +
				"boolean d = true;";
	}
	
	@Override
	protected String getImports() {
		return  "import java.util.IllegalFormatException;";
	}
	
	@Test
	public void testEHC_RemoveCatchStatement1(){
		String pre 	= "public boolean isValidInt(String x) { try { Integer.getInteger(x); return true; } catch (Exception e) { return false; }}";
		String post	= "public boolean isValidInt(String x) throws Exception { Integer.getInteger(x); return true; }"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEHC_RemoveCatchStatement2(){
		String pre 	= "public String formatMsg(Object o) { try { return String.format(\"\", o); } catch (Exception e) { return \"\"; }}";
		String post	= "public String formatMsg(Object o) throws IllegalFormatException {return String.format(\"\", o);}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEHC_RemoveThrow1(){
		String pre 	= "public boolean isString(Object o) throws ClassCastException { String s = (String) o; return true;}";
		String post	= "public boolean isString(Object o) { try { String s = (String) o; return true; } catch (ClassCastException e) {return false; }}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

}
