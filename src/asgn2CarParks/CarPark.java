/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2CarParks 
 * 21/04/2014
 * 
 */
package asgn2CarParks;

import java.util.ArrayList;

import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * The CarPark class provides a range of facilities for working with a car park in support 
 * of the simulator. In particular, it maintains a collection of currently parked vehicles, 
 * a queue of vehicles wishing to enter the car park, and an historical list of vehicles which 
 * have left or were never able to gain entry. 
 * 
 * The class maintains a wide variety of constraints on small cars, normal cars and motorcycles 
 * and their access to the car park. See the method javadoc for details. 
 * 
 * The class relies heavily on the asgn2.Vehicle hierarchy, and provides a series of reports 
 * used by the logger. 
 * 
 * @author hogan
 *
 */
public class CarPark {

	//Max sizes
	private int maxCarSpaces;
	private int maxSmallCarSpaces;
	private int maxMotorCycleSpaces;
	private int maxQueueSize;
	
	//Tracking Variables
	private int count;
	private int numSmallCars;
	private int numCars;
	private int numMotorCycles;
	private int numDissatisfied;
	
	//The actual car park
	private ArrayList<Vehicle> spaces;
	private LinkedBlockingQueue <Vehicle> queue; //http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/LinkedBlockingQueue.html
	private ArrayList<Vehicle> past;
	

	private String status;
	//Variables to allow repeats for gui status
	private String currentStatus;
	private int currentStatusTime;

	
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * Uses default parameters
	 */
	public CarPark() {
		this(Constants.DEFAULT_MAX_CAR_SPACES,Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,Constants.DEFAULT_MAX_QUEUE_SIZE);
	}
	
	
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * @param maxCarSpaces maximum number of spaces allocated to cars in the car park 
	 * @param maxSmallCarSpaces maximum number of spaces (a component of maxCarSpaces) 
	 * 						 restricted to small cars
	 * @param maxMotorCycleSpaces maximum number of spaces allocated to MotorCycles
	 * @param maxQueueSize maximum number of vehicles allowed to queue
	 */
	public CarPark(int maxCarSpaces,int maxSmallCarSpaces, int maxMotorCycleSpaces, int maxQueueSize) {
		this.maxCarSpaces = maxCarSpaces;
		this.maxSmallCarSpaces = maxSmallCarSpaces;
		this.maxMotorCycleSpaces = maxMotorCycleSpaces;
		this.maxQueueSize = maxQueueSize;
		
		count = 0;
		numSmallCars = 0;
		numCars = 0;
		numMotorCycles = 0;
		
		spaces = new ArrayList <Vehicle> ();
		queue = new LinkedBlockingQueue <Vehicle> (maxQueueSize);
		past = new ArrayList <Vehicle> ();
		status = "";
		currentStatusTime = -9999;//Initialize to a value that should never be used.
	}
	
	

	/**
	 * Archives vehicles exiting the car park after a successful stay. Includes transition via 
	 * Vehicle.exitParkedState(). 
	 * @param time int holding time at which vehicle leaves
	 * @param force boolean forcing departure to clear car park 
	 * @throws VehicleException if vehicle to be archived is not in the correct state 
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied
	 */
	public void archiveDepartingVehicles(int time,boolean force) throws VehicleException, SimulationException {
		//Set the number of vehicle to be check to all.
		int remaining = spaces.size()-1;//As elements start from 0 and counting from 1, take 1 from the size.
		
		//While vehicles are unchecked
		while (remaining >= 0){
			
			//Get the next remaining vehicle 
			Vehicle v = spaces.get(remaining);
			
			//Check its state
			if (!v.isParked()){
				throw new VehicleException("Vehicle not in the correct state. ");
			}
			
			//If their time has elapsed or vehicles are being forced out
			if (force || (time >= v.getDepartureTime())){
				
				//Archive and add event to status;
				past.add(v);
				status += setVehicleMsg(v,"P","A");
				
				//Unpark the Vehicle and exit the car park. Contains Vehicle.exitParkedState() transition and Simulation Exception.
				unparkVehicle(v,time);
				
			}
			//Increment vehicles remaining to be checked
			remaining--;	
		}
		
	}
		
	
	
	/**
	 * Method to archive new vehicles that don't get parked or queued and are turned 
	 * away
	 * @param v Vehicle to be archived
	 * @throws SimulationException if vehicle is currently queued or parked
	 */
	public void archiveNewVehicle(Vehicle v) throws SimulationException {
		
		//Check is not parked/queued
		if (v.isParked() || v.isQueued()){
			throw new SimulationException("Vehicle is currently queued or parked.");
		}
		
		//Will be dissatisfied
		numDissatisfied++;
		
		//Archive and add event to status;
		past.add(v);
		status += setVehicleMsg(v,"N","A");
	}
	
	
	
	/**
	 * Archive vehicles which have stayed in the queue too long 
	 * @param time int holding current simulation time 
	 * @throws VehicleException if one or more vehicles not in the correct state or if timing constraints are violated
	 * @throws SimulationException 
	 */
	public void archiveQueueFailures(int time) throws VehicleException, SimulationException {
		for (Vehicle v : this.queue){
			if (!v.isQueued()){
				throw new VehicleException("Vehicle is not in the correct state.");
			}
			
			if (((time - v.getArrivalTime()) > Constants.MAXIMUM_QUEUE_TIME)){
				
				exitQueue(v,time);
				
				numDissatisfied++;
				
				queue.remove(v);
				
				past.add(v);
				status += setVehicleMsg(v,"Q","A");
			}
		}
	}
	
	
	
	/**
	 * Simple status showing whether carPark is empty
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() {
		return spaces.isEmpty();
	}
	
	
	
	/**
	 * Simple status showing whether carPark is full
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		return (spaces.size() >= (maxCarSpaces + maxSmallCarSpaces + maxMotorCycleSpaces));
	}
	
	
	
	/**
	 * Method to add vehicle successfully to the queue
	 * Precondition is a test that spaces are available
	 * Includes transition through Vehicle.enterQueuedState 
	 * @param v Vehicle to be added 
	 * @throws SimulationException if queue is full  
	 * @throws VehicleException if vehicle not in the correct state 
	 */
	public void enterQueue(Vehicle v) throws SimulationException, VehicleException {
		if (queueFull()){
			throw new SimulationException ("Queue is full.");
		}
		
		//If statement to test if the vehicle you are trying to park is already in the car park or
		//in the queue. If so throw and exception.
		if (v.isQueued() || v.isParked()){
			throw new VehicleException ("Vehicle is not in the correct state.");
		}
		
		v.enterQueuedState();
		queue.add(v);
	}
	
	
	
	/**
	 * Method to remove vehicle from the queue after which it will be parked or 
	 * removed altogether. Includes transition through Vehicle.exitQueuedState.  
	 * @param v Vehicle to be removed from the queue 
	 * @param exitTime int time at which vehicle exits queue
	 * @throws SimulationException if vehicle is not in queue 
	 * @throws VehicleException if the vehicle is in an incorrect state or timing 
	 * constraints are violated
	 */
	public void exitQueue(Vehicle v,int exitTime) throws SimulationException, VehicleException {
		
		//If statement to test if the vehicle you are trying to park is not in queue. 
		//If so throw and exception.
		if (!queue.contains(v)){
			throw new SimulationException ("Vehicle is not in the queue.");
		}
		
		//If statement to test if the vehicle you are trying to park is not already in the queue. 
		//If so throw and exception.
		if (!v.isQueued()){
			throw new VehicleException ("Vehicle is not in the queued state.");
		}
		
		v.exitQueuedState(exitTime);
		queue.remove(v);
	}
	
	
	
	/**
	 * State dump intended for use in logging the final state of the carpark
	 * All spaces and queue positions should be empty and so we dump the archive
	 * @return String containing dump of final carpark state 
	 */
	public String finalState() {
		String str = "Vehicles Processed: count:" + 
				this.count + ", logged: " + this.past.size() 
				+ "\nVehicle Record: \n";
		for (Vehicle v : this.past) {
			str += v.toString() + "\n\n";
		}
		return str + "\n";
	}
	
	
	
	/**
	 * Simple getter for number of cars in the car park 
	 * @return number of cars in car park, including small cars
	 */
	public int getNumCars() {
		return (numCars + numSmallCars);
	}
	
	
	
	/**
	 * Simple getter for number of motorcycles in the car park 
	 * @return number of MotorCycles in car park, including those occupying 
	 * 			a small car space
	 */
	public int getNumMotorCycles() {
		return numMotorCycles;
	}
	
	
	
	/**
	 * Simple getter for number of small cars in the car park 
	 * @return number of small cars in car park, including those 
	 * 		   not occupying a small car space. 
	 */
	public int getNumSmallCars() {
		return numSmallCars;
	}
	
	
	
	/**
	 * Method used to provide the current status of the car park. 
	 * Uses private status String set whenever a transition occurs. 
	 * Example follows (using high probability for car creation). At time 262, 
	 * we have 276 vehicles existing, 91 in car park (P), 84 cars in car park (C), 
	 * of which 14 are small (S), 7 MotorCycles in car park (M), 48 dissatisfied (D),
	 * 176 archived (A), queue of size 9 (CCCCCCCCC), and on this iteration we have 
	 * seen: car C go from Parked (P) to Archived (A), C go from queued (Q) to Parked (P),
	 * and small car S arrive (new N) and go straight into the car park<br>
	 * 262::276::P:91::C:84::S:14::M:7::D:48::A:176::Q:9CCCCCCCCC|C:P>A||C:Q>P||S:N>P|
	 * @return String containing current state 
	 */
	public String getStatus(int time) {
		if (currentStatusTime == time){
			return currentStatus;
		}
		currentStatusTime = time;
		String str = stringStatus(time);
		this.status="";
		currentStatus = str;
		return str;
	}



	/**
	 * @param time
	 * @return
	 */
	protected String stringStatus(int time) {
		String str = time +"::"
		+ this.count + "::" 
		+ "P:" + this.spaces.size() + "::"
		+ "C:" + this.getNumCars() + "::S:" + this.numSmallCars 
		+ "::M:" + this.numMotorCycles 
		+ "::D:" + this.numDissatisfied 
		+ "::A:" + this.past.size()  
		+ "::Q:" + this.queue.size(); 
		for (Vehicle v : this.queue) {
			if (v instanceof Car) {
				if (((Car)v).isSmall()) {
					str += "S";
				} else {
					str += "C";
				}
			} else {
				str += "M";
			}
		}
		str += this.status;
		return str+"\n";
	}
	

	
	/**
	 * State dump intended for use in logging the initial state of the carpark.
	 * Mainly concerned with parameters. 
	 * @return String containing dump of initial carpark state 
	 */
	public String initialState() {
		return "CarPark [maxCarSpaces: " + this.maxCarSpaces
				+ " maxSmallCarSpaces: " + this.maxSmallCarSpaces 
				+ " maxMotorCycleSpaces: " + this.maxMotorCycleSpaces 
				+ " maxQueueSize: " + this.maxQueueSize + "]";
	}

	
	
	/**
	 * Simple status showing number of vehicles in the queue 
	 * @return number of vehicles in the queue
	 */
	public int numVehiclesInQueue() {
		return queue.size();
	}
	
	
	
	/**
	 * Method to add vehicle successfully to the car park store. 
	 * Precondition is a test that spaces are available. 
	 * Includes transition via Vehicle.enterParkedState.
	 * @param v Vehicle to be added 
	 * @param time int holding current simulation time
	 * @param intendedDuration int holding intended duration of stay 
	 * @throws SimulationException if no suitable spaces are available for parking 
	 * @throws VehicleException if vehicle not in the correct state or timing constraints are violated
	 */
	public void parkVehicle(Vehicle v, int time, int intendedDuration) throws SimulationException, VehicleException {
		if (!spacesAvailable(v)){
			throw new SimulationException("No suitable spaces are available for parking");
		}
		
		//If statement to test if the vehicle you are trying to park is already in the car park or
		//in the queue. If so throw and exception.
		if (v.isParked() || v.isQueued()){
			throw new VehicleException("Vehicle is not in the correct state.");
		}
		
		//If statement to test if the vehicle you are trying to park has a time that exceeds the
		//closing time for the car park. If so throw and exception.
		if (time >= Constants.CLOSING_TIME){
			throw new VehicleException("Parking Time is later than closing.");
		}
		
		//If statement to test if the vehicle you are trying to park has a time that is less than
		//the minimum stay time for the car park. If so throw and exception.
		if (intendedDuration < Constants.MINIMUM_STAY){
			throw new VehicleException("Intended duration is less than minimum.");
		}
		
		v.enterParkedState(time, intendedDuration);
		spaces.add(v);
		
		//Increment the count for that type of vehicle
		if (v instanceof Car) {
			if (((Car)v).isSmall()){
				numSmallCars++;
			}else{
				numCars++;
			}
		}else{
			numMotorCycles++;
		}
		
		
	}

	
	
	/**
	 * Silently process elements in the queue, whether empty or not. If possible, add them to the car park. 
	 * Includes transition via exitQueuedState where appropriate
	 * Block when we reach the first element that can't be parked. 
	 * @param sim Simulation object controlling vehicle creation
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when parking attempted
	 * @throws VehicleException if state is incorrect, or timing constraints are violated
	 */
	public void processQueue(int time, Simulator sim) throws VehicleException, SimulationException {

		if (!queueEmpty()){
			for (Vehicle v : this.queue){ 
				
				//If statement to test if the vehicle that is being processed is not in the queue.
				//If so throw and exception.
				if (!v.isQueued()){
					throw new VehicleException("Vehicle is not in the correct state.");
				}
				
				if (spacesAvailable(v)){
					exitQueue(v,time);
					parkVehicle(v,time,sim.setDuration());

					status += setVehicleMsg(v,"Q","P");
				}else{
					break;
				}
			}
		}
	}

	
	
	/**
	 * Simple status showing whether queue is empty
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		return queue.isEmpty();
	}

	
	
	/**
	 * Simple status showing whether queue is full
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		return (queue.remainingCapacity() <= 0);
	}
	
	
	
	/**
	 * Method determines, given a vehicle of a particular type, whether there are spaces available for that 
	 * type in the car park under the parking policy in the class header.  
	 * @param v Vehicle to be stored. 
	 * @return true if space available for v, false otherwise 
	 */
	public boolean spacesAvailable(Vehicle v) {
		
		//If the vehicle is a car
		if (v instanceof Car) {
			
			//If the car is a small car
			if (((Car)v).isSmall()){
				//It can use small car or normal car spots.
				return ((maxSmallCarSpaces + (maxCarSpaces - numCars)) > numSmallCars);
			}
			
			//Normal cars use car spots.
			if (numSmallCars > maxSmallCarSpaces){
				return ((maxCarSpaces - maxSmallCarSpaces) > ((numSmallCars - maxSmallCarSpaces) + numCars));
			}
			return ((maxCarSpaces - maxSmallCarSpaces) > numCars);
		
		//If the vehicle is a motorcycle
		}else if (v instanceof MotorCycle){
			//It can use motorcycle or small car spots.
			if (maxSmallCarSpaces > numSmallCars){
				return ((maxMotorCycleSpaces + maxSmallCarSpaces - numSmallCars ) > numMotorCycles);
			}
			return (maxMotorCycleSpaces > numMotorCycles);
		}
		
		//Fall back: if it's not an expected type of vehicle return false.
		return false;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CarPark [count:" + count +
				" numCars: " + numCars +
				" numSmallCars: " + numSmallCars +
				" numMotorCycles: " + numMotorCycles +
				" queue: " + (queue.size()) +
				" numDissatisfied: " + numDissatisfied +
				" Archive: " + (past.size()) + "]";
	}

	
	
	/**
	 * Method to try to create new vehicles (one trial per vehicle type per time point) 
	 * and to then try to park or queue (or archive) any vehicles that are created 
	 * @param sim Simulation object controlling vehicle creation 
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when operation attempted 
	 * @throws VehicleException if vehicle creation violates constraints 
	 */
	public void tryProcessNewVehicles(int time,Simulator sim) throws VehicleException, SimulationException {
		
		if (sim.newCarTrial()){
			String id = "C" + count;
			Vehicle v = new Car(id,time,sim.smallCarTrial());
			processNewVehicle(v,time, sim);
		}
		
		if (sim.motorCycleTrial()){
			String id = "MC" + count;
			Vehicle v = new MotorCycle(id,time);
			processNewVehicle(v,time, sim);
		}
	}



	/**
	 * 
	 * @param time int holding current simulation time.
	 * @param sim Simulation object controlling vehicle creation.
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	private void processNewVehicle(Vehicle v, int time, Simulator sim) throws VehicleException, SimulationException {
		
		if (spacesAvailable(v)){
			parkVehicle(v,time,sim.setDuration());
			status += setVehicleMsg(v,"N","P");
		}else if (!queueFull()){
			enterQueue(v);
			status += setVehicleMsg(v,"N","Q");
		}else{
			archiveNewVehicle(v);
			//Status is set inside all archiving methods.
		}
		count++;
	}

	
	
	/**
	 * Method to remove vehicle from the carpark. 
	 * For symmetry with parkVehicle, include transition via Vehicle.exitParkedState.  
	 * So vehicle should be in parked state prior to entry to this method. 
	 * @param v Vehicle to be removed from the car park 
	 * @throws VehicleException if Vehicle is not parked, is in a queue, or violates timing constraints 
	 * @throws SimulationException if vehicle is not in car park
	 */
	public void unparkVehicle(Vehicle v,int departureTime) throws VehicleException, SimulationException {
		
		//If statement to test if the vehicle that is being 'unparked' is not parked.
		//If so throw and exception.
		if (!v.isParked()){
			throw new VehicleException("Target vehicle is not in the correct state.");
		}
		
		//If statement to test if the vehicle that is being 'unparked' is not in the car park.
		//If so throw and exception.
		if (!spaces.contains(v)){
			throw new SimulationException("Target vehicle is not in the Car Park");
		}
		
		v.exitParkedState(departureTime);
		spaces.remove(v);
		
		//De-Increment the count for that type of vehicle
		if (v instanceof Car) {
			if (((Car)v).isSmall()){
				numSmallCars--;
			}else{
				numCars--;
			}
		}else{
			numMotorCycles--;
		}
	}
	
	
	
	/**
	 * Helper to set vehicle message for transitions 
	 * @param v Vehicle making a transition (uses S,C,M)
	 * @param source String holding starting state of vehicle (N,Q,P,A) 
	 * @param target String holding finishing state of vehicle (Q,P,A) 
	 * @return String containing transition in the form: |(S|C|M):(N|Q|P|A)>(Q|P|A)| 
	 */
	private String setVehicleMsg(Vehicle v,String source, String target) {
		String str="";
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				str+="S";
			} else {
				str+="C";
			}
		} else {
			str += "M";
		}
		return "|"+str+":"+source+">"+target+"|";
	}
}
