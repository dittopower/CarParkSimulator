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

import java.lang.reflect.Array;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 *
 */
public class CarParkTests {

	private CarPark TestPark;
	private Car TestCar;
	private Simulator TestSim;
	private MotorCycle TestMoto;
	private final int minimum_Stay = 20;
	private final int arrival_Time = 1;
	
	
	
	@Before
	public void setUp() throws Exception {
		
		TestPark =  new CarPark(0,1,0,1);
		TestCar = new Car("7357CAR",arrival_Time,true);
		TestMoto = new MotorCycle("7357MOTO",arrival_Time);
		TestSim = new Simulator();
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}
	
	
	@Test
	public void testCarPark() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testArchiveDepartingVehicles() throws SimulationException, VehicleException {
		boolean forceout = true;
		
		TestPark.parkVehicle(TestCar, arrival_Time, TestSim.setDuration());
		TestPark.archiveDepartingVehicles(TestCar.getDepartureTime(), !forceout);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("A:1") && str.contains("|S:P>A|");
		assertTrue(bool);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * @throws SimulationException 
	 */
	@Test
	public void testArchiveNewVehicle() throws SimulationException {
		TestPark.archiveNewVehicle(TestCar);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:N>A|");
		assertTrue(bool);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testArchiveQueueFailures() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		
		TestPark.archiveQueueFailures(Constants.MAXIMUM_QUEUE_TIME*2);
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:Q>A|");
		assertTrue(bool);
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
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
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
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
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
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumMotorCycles()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testGetNumMotorCycles() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestMoto, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumMotorCycles(),1);
	}

	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumSmallCars()}.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testGetNumSmallCars() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
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
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
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
		TestPark.processQueue(2, TestSim);
		
		assertEquals(TestPark.getNumSmallCars(),1);
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
	 * Expected Results based off the provided default.log file.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void testTryProcessNewVehicles() throws VehicleException, SimulationException {
		int time = 1;
		TestPark.tryProcessNewVehicles(time, TestSim);
		time = 2;
		TestPark.tryProcessNewVehicles(time, TestSim);
		
		String str = TestPark.getStatus(2);
		boolean bool = str.contains("P:1") && str.contains("Q:1");
		assertTrue(bool);	
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void testUnparkVehicle() throws VehicleException, SimulationException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		TestPark.unparkVehicle(TestCar, 1);
		
		assertEquals(TestPark.getNumCars(),0);
	}

	
	
	/* CarParkTests.java  - Recommended Tests*/
	/*
	 * Confirm that the API spec has not been violated through the
	 * addition of public fields, constructors or methods that were
	 * not requested
	 */
	@Test
	public void NoExtraPublicMethods() {
		//Extends Object, extras less toString() 
		final int ExtraMethods = 21; 
		final int NumObjectClassMethods = Array.getLength(Object.class.getMethods());
		final int NumCarParkClassMethods = Array.getLength(CarPark.class.getMethods());
		assertTrue("obj:"+NumObjectClassMethods+":cp:"+NumCarParkClassMethods,(NumObjectClassMethods+ExtraMethods)==NumCarParkClassMethods);
	}
	
	@Test 
	public void NoExtraPublicFields() {
		//Same as Vehicle 
		final int NumObjectClassFields = Array.getLength(Object.class.getFields());
		final int NumCarParkClassFields = Array.getLength(CarPark.class.getFields());
		assertTrue("obj:"+NumObjectClassFields+":cp:"+NumCarParkClassFields,(NumObjectClassFields)==NumCarParkClassFields);
	}
	
	@Test 
	public void NoExtraPublicConstructors() {
		//One extra cons used. 
		final int NumObjectClassConstructors = Array.getLength(Object.class.getConstructors());
		final int NumCarParkClassConstructors = Array.getLength(CarPark.class.getConstructors());
		assertTrue(":obj:"+NumObjectClassConstructors+":cp:"+NumCarParkClassConstructors,(NumObjectClassConstructors+1)==NumCarParkClassConstructors);
	}
}
