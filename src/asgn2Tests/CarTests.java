/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 27/05/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Array;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;
import asgn2Vehicles.Vehicle;

/**
 * @author Damon Jones n8857954
 *
 */
public class CarTests {

	Car testCar;
	final int ArriveTime = 1;
	final String ID = "Test00";
	final boolean SmallCar = true;
	
	
	
	/**
	 * Make a car for testing.
	 * @throws java.lang.Exception
	 */
	@Before@Test
	public void setUp() throws Exception {
		testCar = new Car(ID, ArriveTime, !SmallCar);
	}
	
	
	/**
	 * So long as it don't throw an error, toString works.
	 * Test method for {@link asgn2Vehicles.Car#toString()}.
	 */
	@Test
	public void testToString() {
		//Find out if there are requirement and what this test must do
		testCar.toString();
		assertTrue(true);
	}

	
	/**
	 * Test Constructor throws on invalid time.
	 * Test method for {@link asgn2Vehicles.Car#Car(java.lang.String, int, boolean)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testCar() throws VehicleException {
		final int InvaildArrivalTime = 0;
		new Car(ID,InvaildArrivalTime, !SmallCar);
	}

	
	/**
	 * test issmall works.
	 * Test method for {@link asgn2Vehicles.Car#isSmall()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsSmall() throws VehicleException {
		//Check that the default TestCar is a normal Car.
		assertFalse(testCar.isSmall());
		//Check that a Small car is a small car.
		testCar = new Car(ID, ArriveTime, SmallCar);
		assertTrue(testCar.isSmall());
	}
	
	
	
	/* CarTests.java - Recommended Tests*/ 
	/*
	 * Confirm that the API spec has not been violated through the
	 * addition of public fields, constructors or methods that were
	 * not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		//Car Class implements Vehicle, adds isSmall() 
		final int NumVehicleClassMethods = Array.getLength(Vehicle.class.getMethods());
		final int NumCarClassMethods = Array.getLength(Car.class.getMethods());
		assertTrue("veh:"+NumVehicleClassMethods+":car:"+NumCarClassMethods,(NumVehicleClassMethods+1)==NumCarClassMethods);
	}
	
	@Test 
	public void NoExtraPublicFields() {
		//Same as Vehicle 
		final int NumVehicleClassFields = Array.getLength(Vehicle.class.getFields());
		final int NumCarClassFields = Array.getLength(Car.class.getFields());
		assertTrue("veh:"+NumVehicleClassFields+":car:"+NumCarClassFields,(NumVehicleClassFields)==NumCarClassFields);
	}
	
	@Test 
	public void NoExtraPublicConstructors() {
		//Same as Vehicle
		final int NumVehicleClassConstructors = Array.getLength(Vehicle.class.getConstructors());
		final int NumCarClassConstructors = Array.getLength(Car.class.getConstructors());
		assertTrue(":veh:"+NumVehicleClassConstructors+":car:"+NumCarClassConstructors,(NumVehicleClassConstructors)==NumCarClassConstructors);
	}

}
