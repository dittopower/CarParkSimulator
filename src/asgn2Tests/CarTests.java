/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;

/**
 * @author hogan
 *
 */
public class CarTests {

	Car testCar;
	final int ArriveTime = 1;
	final String ID = "Test00";
	final boolean SmallCar = true;
	
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testCar = new Car(ID, ArriveTime, !SmallCar);
	}

	
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Car#toString()}.
	 */
	@Test
	public void testToString() {
		//Find out if there are requirement and what this test must do
		System.out.print(testCar.toString());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Car#Car(java.lang.String, int, boolean)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testCar() throws VehicleException {
		final int InvaildArrivalTime = 0;
		new Car(ID,InvaildArrivalTime, !SmallCar);
	}

	
	
	/**
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

}
