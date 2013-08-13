package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperators.methodlevel.cfdo.CFDO_Insert;

import org.junit.Test;

import utils.MethodTest;

public class CFDO_Insert_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new CFDO_Insert();
		this.addMutationOperatorToTest(mutop);
	}

	@Override
	protected String getOtherClassContent() {
		return  "int a1 = 0; " +
				"int a2 = 1; " +
				"boolean b = true; " +
				"String s = \"Hello\";";
	}
	
	@Test
	public void testCFDO_Insert_break1(){
		String pre 	= "for(int i=0; i < a2; i++){System.out.println(i); a1 += i; if(b){a2 += 1;}} System.out.println(a1);";
		String post	= "for(int i=0; i < a2; i++){System.out.println(i); a1 += i; if(b){a2 += 1; break;}} System.out.println(a1);"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Insert_break2(){
		String pre 	= "while(true){if(a1>a2){System.out.println(a1);}a1 += a2;}";
		String post	= "while(true){if(a1>a2){System.out.println(a1);break;}a1 += a2;}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Insert_break3(){		
		String pre 	= "while(true){do{if(b){a2 += 1;}; a1 += a2;}while(a1 < a2);}";
		String post	= "while(true){do{if(b){a2 += 1;break;}; a1 += a2;}while(a1 < a2);}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
