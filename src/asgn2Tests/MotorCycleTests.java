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

import java.lang.reflect.Array;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {

	Vehicle testCycle;
	final int ArrivalTime = 1;
	final String ID = "Test00";
	final int exit_Time = 40;
	final int ParkTime = 2;
	final int Duration = 30;
	final int DepartTime = 40;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testCycle = new MotorCycle(ID, ArrivalTime);
	}

	
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testMotorCycle() throws VehicleException {
		final int InvaildArrivalTime = 0;
		new MotorCycle(ID, InvaildArrivalTime);
	}

	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getVehID()}.
	 */
	@Test
	public void testGetVehID() {
		assertEquals(ID,testCycle.getVehID());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getArrivalTime()}.
	 */
	@Test
	public void testGetArrivalTime() {
		assertEquals(ArrivalTime,testCycle.getArrivalTime());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterQueuedState() throws VehicleException {
		
		testCycle.enterQueuedState();
		
		assertTrue(testCycle.isQueued());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterQueuedStateException() throws VehicleException {
		
		testCycle.enterQueuedState();
		
		testCycle.enterQueuedState();
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitQueuedState() throws VehicleException {
		
		testCycle.enterQueuedState();
		
		testCycle.exitQueuedState(exit_Time);
		
		assertFalse(testCycle.isQueued());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test
	public void testEnterParkedState() throws VehicleException {
		
		testCycle.enterParkedState(ParkTime, Duration);
		
		assertTrue(testCycle.isParked());
	}
	
	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testEnterParkedStateException() throws VehicleException {
		
		testCycle.enterParkedState(ParkTime, Duration);
		
		testCycle.enterParkedState(ParkTime, Duration);
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitParkedStateInt() throws VehicleException {
		
		testCycle.enterParkedState(ParkTime, Duration);
		
		final int DepartBeforeArrival = -1;
		
		testCycle.exitParkedState(DepartBeforeArrival);
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testExitParkedState() throws VehicleException {
		
		testCycle.enterParkedState(ParkTime, Duration);
		
		testCycle.exitParkedState(DepartTime);
		
		assertFalse(testCycle.isParked());
	}
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 * @throws VehicleException 
	 */
	@Test (expected = VehicleException.class)
	public void testExitParkedStateException() throws VehicleException {
		
		testCycle.exitParkedState(DepartTime);
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isParked()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsParked() throws VehicleException {
		assertFalse(testCycle.isParked());
		
		testCycle.enterParkedState(ParkTime, Duration);
		
		assertTrue(testCycle.isParked());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isQueued()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsQueued() throws VehicleException {
		assertFalse(testCycle.isQueued());
		
		testCycle.enterQueuedState();
		
		assertTrue(testCycle.isQueued());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getParkingTime()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testGetParkingTime() throws VehicleException {

		testCycle.enterParkedState(ParkTime, Duration);
		
		assertEquals(ParkTime, testCycle.getParkingTime());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getDepartureTime()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testGetDepartureTime() throws VehicleException {
		
		testCycle.enterParkedState(ParkTime, Duration);
		
		final int ExpectedDepartTime = ParkTime + Duration;
		assertEquals(ExpectedDepartTime, testCycle.getDepartureTime());
		
		testCycle.exitParkedState(DepartTime);
		
		assertEquals(DepartTime, testCycle.getDepartureTime());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasQueued()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testWasQueued() throws VehicleException {
		assertFalse(testCycle.wasQueued());
		
		testCycle.enterQueuedState();
		
		assertTrue(testCycle.wasQueued());
		
		final int ExitTime = 10;
		testCycle.exitQueuedState(ExitTime);
		
		assertTrue(testCycle.wasQueued());
	}
	
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasParked()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testWasParked() throws VehicleException {
		assertFalse(testCycle.wasParked());
		
		final int ParkTime = 2;
		final int Duration = 30;
		testCycle.enterParkedState(ParkTime, Duration);
		
		assertTrue(testCycle.wasParked());
		
		final int ExitTime = 10;
		testCycle.exitParkedState(ExitTime);
		
		assertTrue(testCycle.wasParked());
	}
	
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isSatisfied()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsSatisfied() throws VehicleException {
		
		assertFalse(testCycle.isSatisfied());
		
		testCycle.enterParkedState(ParkTime, Duration);
		assertTrue(testCycle.isSatisfied());
		
		testCycle.exitParkedState(DepartTime);
		assertTrue(testCycle.isSatisfied());
		
		testCycle.enterQueuedState();
		assertTrue(testCycle.isSatisfied());
		
		final int OverMaxQueueTime = 30;
		testCycle.exitQueuedState(OverMaxQueueTime);
		assertFalse(testCycle.isSatisfied());
	}
	
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#toString()}.
	 */
	@Test
	public void testToString() {
		testCycle.toString();
		assertTrue(true);
	}

	/* MotorCycleTests - Recommended Tests*/
	/*
	 * Confirm that the API spec has not been violated through the
	 * addition of public fields, constructors or methods that were
	 * not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		//MotorCycle Class implements Vehicle
		final int NumVehicleClassMethods = Array.getLength(Vehicle.class.getMethods());
		final int NumMotorCycleClassMethods = Array.getLength(MotorCycle.class.getMethods());
		assertTrue("veh:"+NumVehicleClassMethods+":MotorCycle:"+NumMotorCycleClassMethods,(NumVehicleClassMethods)==NumMotorCycleClassMethods);
	}
	
	@Test 
	public void NoExtraPublicFields() {
		//Same as Vehicle 
		final int NumVehicleClassFields = Array.getLength(Vehicle.class.getFields());
		final int NumMotorCycleClassFields = Array.getLength(MotorCycle.class.getFields());
		assertTrue("veh:"+NumVehicleClassFields+":MotorCycle:"+NumMotorCycleClassFields,(NumVehicleClassFields)==NumMotorCycleClassFields);
	}
	
	@Test 
	public void NoExtraPublicConstructors() {
		//Same as Vehicle
		final int NumVehicleClassConstructors = Array.getLength(Vehicle.class.getConstructors());
		final int NumMotorCycleClassConstructors = Array.getLength(MotorCycle.class.getConstructors());
		assertTrue(":veh:"+NumVehicleClassConstructors+":mc:"+NumMotorCycleClassConstructors,(NumVehicleClassConstructors)==NumMotorCycleClassConstructors);
	}
}
