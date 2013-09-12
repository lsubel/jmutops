package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.aco.ACO;
import mutationoperators.methodlevel.cro.CRO;
import mutationoperators.methodlevel.pnc.PNC;

import org.junit.Test;

import utils.MethodTest;

public class CRO_Test_MultipleFiles extends MethodTest {

	MutationOperator mutop_cro;
	MutationOperator mutop_pnc;
	MutationOperator mutop_aco;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop_cro = new CRO();
		this.addMutationOperatorToTest(mutop_cro);
		this.mutop_pnc = new PNC();
		this.addMutationOperatorToTest(mutop_pnc);
		this.mutop_aco = new ACO();
		this.addMutationOperatorToTest(mutop_aco);
	}

	@Override
	protected String getOtherClassContent() {
		return  "String owner = \"Me\"; " + 
				"Building firstBuilding; " +
				"Building secondBuilding; ";
	}

	@Override
	protected void initializeContextFiles() {
		String fileContentBuilding = 
			"String name; " + 
 			"String location; " +
			"int priceInDollar; " +
			"int age; " +
			"public Building(){age = 0;}" +
			"public Building(int price, int years){age = years; priceInDollar = price;}"; 
		addContextSourceFile("Building", surroundWithClass("Building", fileContentBuilding));
		
		String fileContentGarage =
			"int slotsForCars; " +
			"boolean containsWorkbench; " + 
			"public Garage(){slotsForCars = 1; containsWorkbench = false; }" +
			"public Garage(int slots){slotsForCars = slots;}";
		addContextSourceFile("Garage", surroundWithClass("Garage", "Building", "", fileContentGarage));
		
		String fileContentHome =
				"int numberOfBedrooms; " +
				"int numberOfBathrooms; " +
				"public Home(){numberOfBedrooms = 1;}" + 
				"public Home(int bed){numberOfBedrooms = bed;}" + 
				"public Home(int bed, int bath){numberOfBedrooms = bed; numberOfBathrooms = bath;}";
		addContextSourceFile("Home", surroundWithClass("Home", "Building", "", fileContentHome));
		
		String fileContentHolidayHome =
				"boolean rented; " + 
				"public HolidayHome(){rented = false;}";
		addContextSourceFile("HolidayHome", surroundWithClass("HolidayHome", "Home", "", fileContentHolidayHome));
	}
	
	@Test
	public void testCRO_SameConstructorMethod1() {
		String pre 	= "Building b = new Building();";
		String post = "Building b = new Garage();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameConstructorMethod2() {
		String pre 	= "Home b = new Home();";
		String post = "Home b = new HolidayHome();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameConstructorMethod3() {
		String pre 	= "Building b = new Building();";
		String post = "Building b = new HolidayHome();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameConstructorMethod4() {
		String pre 	= "Home h = new Home();";
		String post = "Home h = new HolidayHome();";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameConstructorMethod5() {
		String pre 	= "Home b = new HolidayHome(2);";
		String post = "Home b = new Home(2);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameConstructorMethod6() {
		String pre 	= "Building b = new Building(50000, 50000);";
		String post = "Building b = new Home(2, 2);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_SameConstructorMethod7() {
		String pre 	= "Building b = new Building(4, 4);";
		String post = "Building b = new Home(4, 4);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(1, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_DifferentParameterNumber1() {
		String pre 	= "Building b = new Building();";
		String post = "Building b = new Building(2, 5);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(1, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_DifferentParameterNumber2() {
		String pre 	= "Building b = new Building(50000, 1);";
		String post = "Building b = new Garage(2);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
	
	@Test
	public void testCRO_DifferentParameterNumber3() {
		String pre 	= "Home h = new HolidayHome();";
		String post = "Home h = new Home(2);";
		HashMap<String, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, getApplicationValue(resultMap, mutop_cro));
		assertEquals(0, getApplicationValue(resultMap, mutop_pnc));
		assertEquals(0, getApplicationValue(resultMap, mutop_aco));
		checkOtherMutationOperators(resultMap);
	}
}
