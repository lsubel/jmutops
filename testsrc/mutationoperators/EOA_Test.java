package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.eoa.EOA;

import org.junit.Test;

import utils.MethodTest;

public class EOA_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new EOA();
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
	public void testEOA_oneDepth(){
		String pre 	= "Stack s = new Stack(); s.push(this.st1); this.st2 = s;";
		String post	= "Stack s = new Stack(); s.push(this.st1); this.st2 = s.clone();"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testEOA_twoDepth(){
		String pre 	= "Stack s = new Stack(); s.otherStack = this.st1; this.st1 = s.otherStack;";
		String post	= "Stack s = new Stack(); s.otherStack = this.st1; this.st1 = s.otherStack.clone();";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
