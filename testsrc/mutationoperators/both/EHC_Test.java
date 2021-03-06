package mutationoperators.both;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.ClassTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.ehc.EHC_Insert;

import org.junit.Test;


public class EHC_Test extends ClassTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EHC_Insert();
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
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEHC_RemoveCatchStatement2(){
		String pre 	= "public String formatMsg(Object o) { try { return String.format(\"\", o); } catch (Exception e) { return \"\"; }}";
		String post	= "public String formatMsg(Object o) throws IllegalFormatException {return String.format(\"\", o);}"; 
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEHC_RemoveThrow1(){
		String pre 	= "public boolean isString(Object o) throws ClassCastException { String s = (String) o; return true;}";
		String post	= "public boolean isString(Object o) { try { String s = (String) o; return true; } catch (ClassCastException e) {return false; }}"; 
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

}
