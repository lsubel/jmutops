package mutationoperators.methodlevel;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MethodTest;
import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.eoc.EOC;

import org.junit.Test;


public class EOC_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EOC();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"public Stack st1 = new Stack(); " +
				"public Stack st2 = new Stack(); "; 
	}
	
	@Override
	protected void initializeContextFiles() {
		String fileContentBar =  
			"private Object content = null; " +
			"public Stack otherStack = null; " + 
			"public Stack(){this.content = null;} " +
			"public Stack(Object cont){this.content = cont;} " +
			"public void push(Object cont){this.content = cont;} " +
			"public Object pull(){Object temp = this.content; this.content = null; return temp;} " +
			"public Stack clone(){Stack temp = new Stack(this.content);} ";
		addContextSourceFile("Stack", surroundWithClass("Stack", fileContentBar));
	}
	
	@Test
	public void testEOC_oneDepth(){
		String pre 	= "Stack s = new Stack(); s.push(this.st1); boolean b = (this.st2 == s);";
		String post	= "Stack s = new Stack(); s.push(this.st1); boolean b = (this.st2.equals(s));";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEOC_twoDepth1(){
		String pre 	= "Stack s = new Stack(); s.otherStack = this.st1; boolean b = (this.st1 == s.otherStack);";
		String post	= "Stack s = new Stack(); s.otherStack = this.st1; boolean b = (this.st1.equals(s.otherStack));";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEOC_twoDepth2(){
		String pre 	= "Stack s = new Stack(); s.otherStack = this.st1; boolean b = (s.otherStack == this.st1);";
		String post	= "Stack s = new Stack(); s.otherStack = this.st1; boolean b = (s.otherStack.equals(this.st1));";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop));
		checkOtherMutationOperators(resultMap, mutop);
	}

}
