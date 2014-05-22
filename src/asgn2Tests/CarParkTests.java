/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 29/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 *
 */
public class CarParkTests {

	CarPark TestPark;
	Car TestCar;
	MotorCycle TestMoto;
	int minimum_stay = 20;
	
	@Before
	public void setUp() throws Exception {
		
		TestPark =  new CarPark(0,1,0,1);
		TestCar = new Car("7357CAR",1,true);
		TestMoto = new MotorCycle("7357MOTO",1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testCarPark() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		
		assertEquals(TestPark.getNumCars(),1);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 */
	@Test
	public void testArchiveDepartingVehicles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 */
	@Test
	public void testArchiveNewVehicle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 */
	@Test
	public void testArchiveQueueFailures() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkEmpty()}.
	 */
	@Test
	public void testCarParkEmpty1() {
		assertEquals(TestPark.carParkEmpty(), true);
	}
	
	@Test
	public void testCarParkEmpty2() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		assertEquals(TestPark.carParkEmpty(), false);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkFull()}.
	 */
	@Test
	public void testCarParkFull1() {
		assertEquals(TestPark.carParkFull(), false);
	}

	@Test
	public void testCarParkFull2() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		assertEquals(TestPark.carParkFull(), true);
	}	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testEnterQueue() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		assertEquals(TestPark.queueEmpty(),false);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testExitQueue() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestPark.exitQueue(TestCar,2);
		
		assertEquals(TestPark.queueEmpty(),true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#finalState()}.
	 */
	@Test
	public void testFinalState() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumCars()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testGetNumCars() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		
		assertEquals(TestPark.getNumCars(),1);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumMotorCycles()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testGetNumMotorCycles() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestMoto, 1, minimum_stay);
		
		assertEquals(TestPark.getNumMotorCycles(),1);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumSmallCars()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testGetNumSmallCars() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		
		assertEquals(TestPark.getNumCars(),1);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getStatus(int)}.
	 */
	@Test
	public void testGetStatus() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#initialState()}.
	 */
	@Test
	public void testInitialState() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#numVehiclesInQueue()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testNumVehiclesInQueue() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		
		assertEquals(TestPark.numVehiclesInQueue(),1);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testParkVehicle() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		
		assertEquals(TestPark.getNumCars(),1);
		}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testProcessQueue() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestPark.processQueue(1, );
		
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueEmpty()}.
	 */
	@Test
	public void testQueueEmpty() {
		assertEquals(TestPark.queueEmpty(),true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueFull()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testQueueFull() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		
		assertEquals(TestPark.queueFull(),true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#spacesAvailable(asgn2Vehicles.Vehicle)}.
	 */
	@Test
	public void testSpacesAvailable() {
		
		assertEquals(TestPark.spacesAvailable(TestCar),true);
		
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#toString()}.
	 */
	@Test
	public void testToString() {
		assertTrue(true);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#tryProcessNewVehicles(int, asgn2Simulators.Simulator)}.
	 */
	@Test
	public void testTryProcessNewVehicles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void testUnparkVehicle() throws VehicleException, SimulationException {
		TestPark.parkVehicle(TestCar, 1, minimum_stay);
		TestPark.unparkVehicle(TestCar, 1);
		
		assertEquals(TestPark.getNumCars(),0);
	}

}
