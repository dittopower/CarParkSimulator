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
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {

	MotorCycle testCycle;
	final int ArrivalTime = 1;
	final String ID = "Test00";
	
	
	
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
	 * Test method for {@link asgn2Vehicles.Vehicle#Vehicle(java.lang.String, int)}.
	 */
	@Test
	public void testVehicle() {
		fail("Not yet implemented"); // TODO
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
	 */
	@Test
	public void testEnterQueuedState() {
		fail("Not yet implemented"); // TODO
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 */
	@Test
	public void testExitQueuedState() {
		fail("Not yet implemented"); // TODO
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 */
	@Test
	public void testEnterParkedState() {
		fail("Not yet implemented"); // TODO
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 */
	@Test
	public void testExitParkedStateInt() {
		fail("Not yet implemented"); // TODO
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 */
	@Test
	public void testExitParkedState() {
		fail("Not yet implemented"); // TODO
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isParked()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testIsParked() throws VehicleException {
		assertFalse(testCycle.isParked());
		
		final int ParkTime = 2;
		final int Duration = 30;
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

		final int ParkTime = 2;
		final int Duration = 30;
		testCycle.enterParkedState(ParkTime, Duration);
		
		assertEquals(ParkTime, testCycle.getParkingTime());
	}

	
	
	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getDepartureTime()}.
	 * @throws VehicleException 
	 */
	@Test
	public void testGetDepartureTime() throws VehicleException {
		
		final int ParkTime = 2;
		final int Duration = 30;
		testCycle.enterParkedState(ParkTime, Duration);
		
		final int ExpectedDepartTime = ParkTime + Duration;
		assertEquals(ExpectedDepartTime, testCycle.getDepartureTime());
		
		final int DepartTime = 40;
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
	 */
	@Test
	public void testIsSatisfied() {
		fail("Not yet implemented"); // TODO
	}
	
	

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#toString()}.
	 */
	@Test
	public void testToString() {
		testCycle.toString();
	}

}
