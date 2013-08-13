package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperator.MutationOperator;
import mutationoperators.methodlevel.cro.CRO;

import org.junit.Test;

import utils.MethodTest;

public class CRO_Test_MultipleFiles extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new CRO();
		this.addMutationOperatorToTest(mutop);
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
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Building();", "Building b = new Garage();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Home();", "Building b = new HolidayHome();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Building();", "Building b = new HolidayHome();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod4() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Home h = new Home();", "Home h = new HolidayHome();");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod5() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Garage(2);", "Building b = new Home(2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod6() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Building(50000, 50000);", "Building b = new Home(2, 2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod7() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Building(4, 4);", "Building b = new Home(4, 4);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod8() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Garage();", "Building b = new Garage(2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_SameConstructorMethod9() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Garage g = new Garage();", "Garage g = new Garage(2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_DifferentParameterNumber1() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Building();", "Building b = new Garage(2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_DifferentParameterNumber2() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Building b = new Building(50000, 1);", "Building b = new Garage(2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
	
	@Test
	public void testCRO_DifferentParameterNumber3() {
		HashMap<MutationOperator, Integer> resultMap = compareMatches("Home h = new HolidayHome();", "Home h = new Home(2);");
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}
}
