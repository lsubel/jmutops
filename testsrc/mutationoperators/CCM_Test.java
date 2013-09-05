package mutationoperators;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import mutationoperators.methodlevel.ccm.CCM;

import org.junit.Test;

import utils.MethodTest;

public class CCM_Test extends MethodTest {

	MutationOperator mutop;
	
	@Override
	protected void initializeMutationOperatorsToTest() {
		this.mutop = new CCM();
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
	public void testCCM_RemoveObject1() {
		String pre 	= "String message = \"START PROCESS\"; System.out.println(message); Building b = new Building(); ";
		String post = "String message = \"START PROCESS\"; System.out.println(message); Building b = null; ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}	
	
	@Test 
	public void testCCM_RemoveObject2() {
		String pre 	= "Home h = new Home(2); System.out.println(); Garage g = new Garage(); ";
		String post = "Home h = null; System.out.println(); Garage g = null; ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(2, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}	
	
	@Test 
	public void testCCM_RemoveObject3() {
		String pre 	= "Home h = new Home(); String s = new String(); System.out.println(); ";
		String post = "Home h = new Home(); String s = null; System.out.println(); ";
		HashMap<MutationOperator, Integer> resultMap = compareMatches(pre, post);
		assertEquals(1, resultMap.get(mutop).intValue());
		checkOtherMutationOperators(resultMap, mutop);
	}	
}
