package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperator.methodlevel.exco.EXCO_Delete;
import mutationoperator.methodlevel.exco.EXCO_Insert;
import mutationoperator.methodlevel.exco.EXCO_Move;
import mutationoperator.methodlevel.exco.EXCO_Update;

import org.junit.Test;

import utils.MethodTest;

public class EXCO_Test extends MethodTest {

	private MutationOperator mutop_insert;
	private MutationOperator mutop_delete;
	private MutationOperator mutop_update;
	private MutationOperator mutop_move;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_insert = new EXCO_Insert();
		this.addMutationOperatorToTest(mutop_insert);
		this.mutop_delete = new EXCO_Delete();
		this.addMutationOperatorToTest(mutop_insert);
		this.mutop_update = new EXCO_Update();
		this.addMutationOperatorToTest(mutop_insert);
		this.mutop_move = new EXCO_Move();
		this.addMutationOperatorToTest(mutop_insert);
	}

	@Override
	protected String getOtherClassContent() {
		return 	"int a = 1; " +
				"int b = 2; ";
	}
	
	@Test
	public void testEXCO_addNewCatch1(){
		String pre 	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		String post	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");} catch (IllegalStateException e){System.out.println(\"IllegalStateException\");}";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop_insert).intValue());
		checkOtherMutationOperators(resultMap, mutop_insert);
	}
	
	@Test
	public void testEXCO_removeCatch1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");} catch (IllegalStateException e){System.out.println(\"IllegalStateException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop_delete).intValue());
		checkOtherMutationOperators(resultMap, mutop_delete);
	}
	
	@Test
	public void testEXCO_reorderCatch1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");} catch (IllegalStateException e){System.out.println(\"IllegalStateException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (IllegalStateException e){System.out.println(\"IllegalStateException\");} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop_move).intValue());
		checkOtherMutationOperators(resultMap, mutop_move);
	}
	
	@Test
	public void testEXCO_replaceExceptionType1(){
		String pre	= "try {Object o = null;o.equals(null);} catch (NullPointerException e) {System.out.println(\"NullPointerException\");}";
		String post = "try {Object o = null;o.equals(null);} catch (Exception e) {System.out.println(\"NullPointerException\");}";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop_update).intValue());
		checkOtherMutationOperators(resultMap, mutop_update);
	}
}
