package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.MutationOperator;
import mutationoperators.methodlevel.cfdo.CFDO_Update;

import org.junit.Test;

import utils.MethodTest;

public class CFDO_Update_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new CFDO_Update();
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
	public void testCFDO_Update_break1(){	
		String pre 	= "BeforeLoop: System.out.println(1); BeforeLoop2: for(int i=0; i < a2; i++){System.out.println(i); a1 += i; if(b){a2 += 1;break BeforeLoop;}} System.out.println(a1);";
		String post	= "BeforeLoop: System.out.println(1); BeforeLoop2: for(int i=0; i < a2; i++){System.out.println(i); a1 += i; if(b){a2 += 1;break BeforeLoop2;}} System.out.println(a1);"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Update_break2(){
		String pre 	= "Label1: System.out.println(0); Label2: while(true){if(a1>a2){System.out.println(a1);break Label1;}a1 += a2;}  ";
		String post	= "Label1: System.out.println(0); Label2: while(true){if(a1>a2){System.out.println(a1);break Label2;}a1 += a2;}  "; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Update_break3(){		
		String pre 	= "Label1: System.out.println(0); Label2: while(true){do{if(b){a2 += 1;break Label1;}; a1 += a2;}while(a1 < a2);}";
		String post	= "Label1: System.out.println(0); Label2: while(true){do{if(b){a2 += 1;break Label2;}; a1 += a2;}while(a1 < a2);}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Update_continue1(){
		String pre 	= "Label1: System.out.println(0); Label2: for(int i=0; i < a2; i++){System.out.println(i); a1 += i; if(b){a2 += 1;continue Label1;}} System.out.println(a1);";
		String post	= "Label1: System.out.println(0); Label2: for(int i=0; i < a2; i++){System.out.println(i); a1 += i; if(b){a2 += 1;continue Label2;}} System.out.println(a1);"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Update_continue2(){
		String pre 	= "Label1: System.out.println(0); Label2: while(true){if(a1>a2){System.out.println(a1);continue Label1;}a1 += a2;}";
		String post	= "Label1: System.out.println(0); Label2: while(true){if(a1>a2){System.out.println(a1);continue Label2;}a1 += a2;}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCFDO_Update_continue3(){		
		String pre 	= "Label1: System.out.println(0); Label2: while(true){do{if(b){a2 += 1;continue Label1;}; a1 += a2;}while(a1 < a2);}";
		String post	= "Label1: System.out.println(0); Label2: while(true){do{if(b){a2 += 1;continue Label2;}; a1 += a2;}while(a1 < a2);}"; 
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}

}
