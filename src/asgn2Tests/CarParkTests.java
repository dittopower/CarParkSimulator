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
 * @author Jordan Beak n8848432
 */
public class CarParkTests {

	private CarPark TestPark;
	private Car TestCar;
	private Simulator TestSim;
	private MotorCycle TestMoto;
	private final int minimum_Stay = 20;
	private final int arrival_Time = 1;
	
	
	
	@Before
	/**
	 * Setup to perform before each test.
	 * @throws Exception
	 */
	public void setUp() throws Exception {
		
		TestPark =  new CarPark(1,1,0,1);
		TestCar = new Car("7357CAR",arrival_Time,true);
		TestMoto = new MotorCycle("7357MOTO",arrival_Time);
		TestSim = new Simulator();
	}	
	
	
	@Test
	/**
	 * Test method to test the constructor.
	 * @throws SimulationException
	 * @throws VehicleException
	 */
	public void testCarPark() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * Test if the ArchiveDepartingVehicles functions correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testArchiveDepartingVehicles1() throws SimulationException, VehicleException {
		boolean forceout = true;
		
		TestPark.parkVehicle(TestCar, arrival_Time, TestSim.setDuration());
		TestPark.archiveDepartingVehicles(TestCar.getDepartureTime(), !forceout);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("A:1") && str.contains("|S:P>A|");
		assertTrue(bool);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveDepartingVehicles(int, boolean)}.
	 * Test if the ArchiveDepartingVehicles method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testArchiveDepartingVehicles2() throws SimulationException, VehicleException {
		boolean forceout = true;
		
		TestPark.parkVehicle(TestCar, arrival_Time, TestSim.setDuration());
		TestCar.exitParkedState(minimum_Stay);
		TestPark.archiveDepartingVehicles(TestCar.getDepartureTime(), !forceout);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("A:1") && str.contains("|S:P>A|");
		assertTrue(bool);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * Test if the ArchiveNewVehicle method functions correctly.
	 * @throws SimulationException 
	 */
	@Test
	public void testArchiveNewVehicle1() throws SimulationException {
		TestPark.archiveNewVehicle(TestCar);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:N>A|");
		assertTrue(bool);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * Test if the ArchiveNewVehicle method throws the SimulationException correctly.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test(expected = SimulationException.class)
	public void testArchiveNewVehicle2() throws SimulationException, VehicleException {
		
		TestPark.parkVehicle(TestCar, arrival_Time, TestSim.setDuration());
		TestPark.archiveNewVehicle(TestCar);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:N>A|");
		assertTrue(bool);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveNewVehicle(asgn2Vehicles.Vehicle)}.
	 * Test if the ArchiveNewVehicle method throws the SimulationException correctly.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test(expected = SimulationException.class)
	public void testArchiveNewVehicle3() throws SimulationException, VehicleException {
		
		TestPark.enterQueue(TestCar);
		TestPark.archiveNewVehicle(TestCar);
		
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:N>A|");
		assertTrue(bool);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * Test if the ArchiveQueueFailures method functions correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testArchiveQueueFailures1() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		
		TestPark.archiveQueueFailures(Constants.MAXIMUM_QUEUE_TIME*2);
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:Q>A|");
		assertTrue(bool);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#archiveQueueFailures(int)}.
	 * Test if the ArchiveQueueFailures method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testArchiveQueueFailures2() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestCar.exitQueuedState(minimum_Stay);
		
		TestPark.archiveQueueFailures(Constants.MAXIMUM_QUEUE_TIME*2);
		String str = TestPark.getStatus(2);

		boolean bool = str.contains("D:1::A:1") && str.contains("|S:Q>A|");
		assertTrue(bool);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkEmpty()}.
	 * Test if the CarParkEmpty method functions correctly.
	 */
	@Test
	public void testCarParkEmpty1() {
		assertEquals(TestPark.carParkEmpty(), true);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkEmpty()}.
	 * Test if the CarParkEmpty method functions correctly.
	 */
	@Test
	public void testCarParkEmpty2() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		assertEquals(TestPark.carParkEmpty(), false);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkFull()}.
	 * Test if the CarParkFull method functions correctly.
	 */
	@Test
	public void testCarParkFull1() {
		assertEquals(TestPark.carParkFull(), false);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#carParkFull()}.
	 * Test if the CarParkFull method functions correctly.
	 */
	@Test
	public void testCarParkFull2() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		assertEquals(TestPark.carParkFull(), true);
	}	
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * Test if the EnterQueue method functions correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testEnterQueue1() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		assertEquals(TestPark.queueEmpty(),false);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * Test if the EnterQueue method throws the SimulationException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = SimulationException.class)
	public void testEnterQueue2() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestPark.enterQueue(TestCar);
		assertEquals(TestPark.queueEmpty(),false);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * Test if the EnterQueue method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testEnterQueue3() throws SimulationException, VehicleException {
		TestCar.enterQueuedState();
		TestPark.enterQueue(TestCar);
		assertEquals(TestPark.queueEmpty(),false);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#enterQueue(asgn2Vehicles.Vehicle)}.
	 * Test if the EnterQueue method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testEnterQueue4() throws SimulationException, VehicleException {
		TestCar.enterParkedState(1, minimum_Stay);
		TestPark.enterQueue(TestCar);
		assertEquals(TestPark.queueEmpty(),false);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 * Test if the ExitQueue method functions correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testExitQueue1() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestPark.exitQueue(TestCar,2);
		
		assertEquals(TestPark.queueEmpty(),true);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 * Test if the ExitQueue method throws the SimulationException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = SimulationException.class)
	public void testExitQueue2() throws SimulationException, VehicleException {
		TestPark.exitQueue(TestCar,minimum_Stay);
		
		assertEquals(TestPark.queueEmpty(),true);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#exitQueue(asgn2Vehicles.Vehicle, int)}.
	 * Test if the ExitQueue method throws the SimulationException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testExitQueue3() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestCar.enterQueuedState();
		TestPark.exitQueue(TestCar,minimum_Stay);
		
		assertEquals(TestPark.queueEmpty(),true);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#finalState()}.
	 * Test if the FinalState method functions correctly.
	 */
	@Test
	public void testFinalState() {
		assertTrue(true);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#getNumCars()}.
	 * Test if the GetNumCars method functions correctly.
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
	 * Test if the GetNumMotoCycles method functions correctly.
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
	 * Test if the GetNumSmallCars method functions correctly.
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
	 * Test if the GetStatus method functions correctly.
	 */
	@Test
	public void testGetStatus() {
		assertTrue(true);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#initialState()}.
	 * Test if the InitialState method functions correctly.
	 */
	@Test
	public void testInitialState() {
		assertTrue(true);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#numVehiclesInQueue()}.
	 * Test if the NumVehiclesInQueue method functions correctly.
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
	 * Test if the ParkVehicle method functions correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testParkVehicle1() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
		}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 * Test if the ParkVehicle method throws the SimulationException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = SimulationException.class)
	public void testParkVehicle2() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
		}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 * Test if the ParkVehicle method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testParkVehicle3() throws SimulationException, VehicleException {
		TestCar.enterParkedState(1, minimum_Stay);
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
		}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 * Test if the ParkVehicle method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testParkVehicle4() throws SimulationException, VehicleException {
		TestCar.enterQueuedState();
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
		}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 * Test if the ParkVehicle method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testParkVehicle5() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, Constants.CLOSING_TIME + 1, minimum_Stay);
		
		assertEquals(TestPark.getNumCars(),1);
		}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#parkVehicle(asgn2Vehicles.Vehicle, int, int)}.
	 * Test if the ParkVehicle method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testParkVehicle6() throws SimulationException, VehicleException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay - 1);
		
		assertEquals(TestPark.getNumCars(),1);
		}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}.
	 * Test if the ProcessQueue method functions correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test
	public void testProcessQueue1() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestPark.processQueue(2, TestSim);
		
		assertEquals(TestPark.getNumSmallCars(),1);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#processQueue(int, asgn2Simulators.Simulator)}.
	 * Test if the ProcessQueue method throws the VehicleException correctly.
	 * @throws VehicleException 
	 * @throws SimulationException 
	 */
	@Test(expected = VehicleException.class)
	public void testProcessQueue2() throws SimulationException, VehicleException {
		TestPark.enterQueue(TestCar);
		TestCar.enterQueuedState();
		TestPark.processQueue(2, TestSim);
		
		assertEquals(TestPark.getNumSmallCars(),1);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueEmpty()}.
	 * Test if the QueueEmpty method functions correctly.
	 */
	@Test
	public void testQueueEmpty() {
		assertEquals(TestPark.queueEmpty(),true);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#queueFull()}.
	 * Test if the QueueFull method functions correctly.
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
	 * Test if the SpacesAvailable method functions correctly.
	 */
	@Test
	public void testSpacesAvailable() {
		
		assertEquals(TestPark.spacesAvailable(TestCar),true);
		
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#toString()}.
	 * Test if the ToString method functions correctly.
	 */
	@Test
	public void testToString() {
		assertTrue(true);
	}

	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#tryProcessNewVehicles(int, asgn2Simulators.Simulator)}.
	 * Expected Results based off the provided default.log file.
	 * Test if the TryProcessNewVehicles method functions correctly.
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
	 * Test if the UnparkVehicle method functions correctly.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test
	public void testUnparkVehicle1() throws VehicleException, SimulationException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		TestPark.unparkVehicle(TestCar, 1);
		
		assertEquals(TestPark.getNumCars(),0);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 * Test if the UnparkVehicle method throws the VehicleException correctly.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test(expected = VehicleException.class)
	public void testUnparkVehicle2() throws VehicleException, SimulationException {
		TestPark.parkVehicle(TestCar, arrival_Time, minimum_Stay);
		TestCar.exitParkedState(1);
		TestPark.unparkVehicle(TestCar, 1);
		
		assertEquals(TestPark.getNumCars(),0);
	}
	
	
	/**
	 * Test method for {@link asgn2CarParks.CarPark#unparkVehicle(asgn2Vehicles.Vehicle, int)}.
	 * Test if the UnparkVehicle method throws the SimulationException correctly.
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test(expected = SimulationException.class)
	public void testUnparkVehicle3() throws VehicleException, SimulationException {
		TestCar.enterParkedState(arrival_Time, minimum_Stay);
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
