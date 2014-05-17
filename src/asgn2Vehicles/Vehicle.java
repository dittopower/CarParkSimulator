/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Vehicles 
 * 19/04/2014
 * 
 */
package asgn2Vehicles;

import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;

//Create a State data type
enum State { Default, Queued, Parked, Archived};

/**
 * Vehicle is an abstract class specifying the basic state of a vehicle and the methods used to 
 * set and access that state. A vehicle is created upon arrival, at which point it must either 
 * enter the car park to take a vacant space or become part of the queue. If the queue is full, then 
 * the vehicle must leave and never enters the car park. The vehicle cannot be both parked and queued 
 * at once and both the constructor and the parking and queuing state transition methods must 
 * respect this constraint. 
 * 
 * Vehicles are created in a neutral state. If the vehicle is unable to park or queue, then no changes 
 * are needed if the vehicle leaves the carpark immediately.
 * Vehicles that remain and can't park enter a queued state via {@link #enterQueuedState() enterQueuedState} 
 * and leave the queued state via {@link #exitQueuedState(int) exitQueuedState}. 
 * Note that an exception is thrown if an attempt is made to join a queue when the vehicle is already 
 * in the queued state, or to leave a queue when it is not. 
 * 
 * Vehicles are parked using the {@link #enterParkedState(int, int) enterParkedState} method and depart using 
 * {@link #exitParkedState(int) exitParkedState}
 * 
 * Note again that exceptions are thrown if the state is inappropriate: vehicles cannot be parked or exit 
 * the car park from a queued state. 
 * 
 * The method javadoc below indicates the constraints on the time and other parameters. Other time parameters may 
 * vary from simulation to simulation and so are not constrained here.  
 * 
 * @author hogan
 *
 */
public abstract class Vehicle {
	
	//Class Variables
	//Vehicle
	private String id;
	//Times
	private int arriveTime;
	private int parkTime;
	private int departTime;
	private int queTime;
	private int queExitTime;

	//States
	private State state;
	private boolean hasParked;
	private boolean hasQueued;
	
	
	
	/**
	 * Vehicle Constructor 
	 * @param vehID String identification number or plate of the vehicle
	 * @param arrivalTime int time (minutes) at which the vehicle arrives and is 
	 *        either queued, given entry to the car park or forced to leave
	 * @throws VehicleException if arrivalTime is <= 0 
	 */
	public Vehicle(String vehID, int arrivalTime) throws VehicleException  {
		
		//Enforce positive arrival time.
		if (arrivalTime <= 0){
			throw new VehicleException("Arrival Time Must be Strictly Positive.");
		}
		//Set Variables
		arriveTime = arrivalTime;
		id = vehID;
		
		//Set Initial State.
		state = State.Default;
		hasParked = false;
		hasQueued = false;
	}

	
	
	/**
	 * Transition vehicle to parked state (mutator)
	 * Parking starts on arrival or on exit from the queue, but time is set here
	 * @param parkingTime int time (minutes) at which the vehicle was able to park
	 * @param intendedDuration int time (minutes) for which the vehicle is intended to remain in the car park.
	 *  	  Note that the parkingTime + intendedDuration yields the departureTime
	 * @throws VehicleException if the vehicle is already in a parked or queued state, if parkingTime < 0, 
	 *         or if intendedDuration is less than the minimum prescribed in asgnSimulators.Constants
	 */
	public void enterParkedState(int parkingTime, int intendedDuration) throws VehicleException {
		
		final int Zero = 0;
		
		//Check the vehicle leaves the queue before parking.
		if (isQueued()){
			throw new VehicleException("The Vehicle must exit the Queue before parking.");
		
		//Check the vehicle's not already parked
		}else if (isParked()){
			throw new VehicleException("The Vehicle is Already Parked!");
			
		//Enforce Valid parking time.
		}else if (parkingTime < Zero){
			throw new VehicleException("The Time of Parking must be 0 or greater.");
		
		//Enforce Minimum stay duration.
		}else if (intendedDuration < Constants.MINIMUM_STAY){
			throw new VehicleException("The Vehicle's Stay Duration must be greater than the minimum.");
		}
		
		//Set Variables
		parkTime = parkingTime;
		departTime = parkingTime + intendedDuration;
		
		//Set Parked State.
		state = State.Parked;
		hasParked = true;
	}
	
	
	
	/**
	 * Transition vehicle to queued state (mutator) 
	 * Queuing formally starts on arrival and ceases with a call to {@link #exitQueuedState(int) exitQueuedState}
	 * @throws VehicleException if the vehicle is already in a queued or parked state
	 */
	public void enterQueuedState() throws VehicleException {
		
		//Check the vehicle's not already Queued.
		if (isQueued()){
			throw new VehicleException("The Vehicle must exit the Queue before parking.");
		
		//Check the vehicle's not currently parked.
		}else if (isParked()){
			throw new VehicleException("The Vehicle is Already Parked!");
		}
		
		//Set Queued State.
		state = State.Queued;
		hasQueued = true;
	}
	
	
	
	/**
	 * Transition vehicle from parked state (mutator) 
	 * @param departureTime int holding the actual departure time 
	 * @throws VehicleException if the vehicle is not in a parked state, is in a queued 
	 * 		  state or if the revised departureTime < parkingTime
	 */
	public void exitParkedState(int departureTime) throws VehicleException {
		
		//Check the vehicle is parked
		if (!isParked()){
			throw new VehicleException("The Vehicle is not in the Parked State!");
		}
		
		//Check the New departure time is not before the parking time
		if (departureTime < parkTime){
			throw new VehicleException("The new Departure Time is earilier than the Parking Time!");
		}
		
		//Set Departure Time
		departTime = departureTime;
		
		//Reset State.
		state = State.Default;
	}

	
	
	/**
	 * Transition vehicle from queued state (mutator) 
	 * Queuing formally starts on arrival with a call to {@link #enterQueuedState() enterQueuedState}
	 * Here we exit and set the time at which the vehicle left the queue
	 * @param exitTime int holding the time at which the vehicle left the queue 
	 * @throws VehicleException if the vehicle is in a parked state or not in a queued state, or if 
	 *  exitTime is not later than arrivalTime for this vehicle
	 */
	public void exitQueuedState(int exitTime) throws VehicleException {
		
		//Check the vehicle is Queued
		if (!isQueued()){
			throw new VehicleException("The Vehicle is not in the Queued State!");
		}
		
		//Check the Exit time is not before the Arrival time
		if (exitTime < arriveTime){
			throw new VehicleException("The Queue Exit Time is earilier than Arrival Time!");
		}
		
		//Set Exit Time
		queExitTime = exitTime;
		
		//Reset State.
		state = State.Default;
	}
	
	
	
	/**
	 * Simple getter for the arrival time 
	 * @return the arrivalTime
	 */
	public int getArrivalTime() {
		return arriveTime;
	}
	
	
	
	/**
	 * Simple getter for the departure time from the car park
	 * Note: result may be 0 before parking, show intended departure 
	 * time while parked; and actual when archived
	 * @return the departureTime
	 */
	public int getDepartureTime() {
		return departTime;
	}
	
	
	
	/**
	 * Simple getter for the parking time
	 * Note: result may be 0 before parking
	 * @return the parkingTime
	 */
	public int getParkingTime() {
		return parkTime;
	}

	
	
	/**
	 * Simple getter for the vehicle ID
	 * @return the vehID
	 */
	public String getVehID() {
		return id;
	}

	
	
	/**
	 * Boolean status indicating whether vehicle is currently parked 
	 * @return true if the vehicle is in a parked state; false otherwise
	 */
	public boolean isParked() {
		
		//If the vehicle is in the parked state it must be parked.
		if (state == State.Parked){
			return true;
		}
		return false;
	}

	
	
	/**
	 * Boolean status indicating whether vehicle is currently queued
	 * @return true if vehicle is in a queued state, false otherwise 
	 */
	public boolean isQueued() {

		//If the vehicle is in the Queued state it must be Queued.
		if (state == State.Queued){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Boolean status indicating whether customer is satisfied or not
	 * Satisfied if they park; dissatisfied if turned away, or queuing for too long 
	 * Note that calls to this method may not reflect final status 
	 * @return true if satisfied, false if never in parked state or if queuing time exceeds max allowable 
	 */
	public boolean isSatisfied() {
		if (wasParked() && (queExitTime - queTime) <= Constants.MAXIMUM_QUEUE_TIME){
			return true;
		}
		return false;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return coreString(); 
	}



	/**
	 * Creates the core string of the to string method.
	 * Is separate to allow easy adjustment in subclasses
	 * @return The core string for the toString Method.
	 */
	protected String coreString() {
		//Get the ID and initial Time strings
		String result = "Vehicle vehID: " + getVehID() +
				"\nArrival Time: " + getArrivalTime();
		
		//Get the string of the objects Queue information
		if (wasQueued()){
			result += "\nEntry to Queue: " + queTime +
					"\nExit from Queue: " + queExitTime +
					"\nQueuing Time: " + (queExitTime - queTime);
		}else{
			result += "\nVehicle was not quequed";
		}
		
		//Get the string of the objects Carpark information
		if (wasParked()){
			result += "\nEntry to Car Park: " + parkTime +
					"\nExit from Car Park: " + departTime +
					"\nParking Time: " + (departTime - parkTime);
		}else{
			result += "\nVehicle was not parked";
		}
		
		//Get the string of the customers satisfaction
		if (isSatisfied()){
			result += "\nCustomer was satisfied";
		}else{
			result += "\nCustomer was unsatisfied";
		}
		
		//Return Strings
		return result;
	}

	
	
	/**
	 * Boolean status indicating whether vehicle was ever parked
	 * Will return false for vehicles in queue or turned away 
	 * @return true if vehicle was or is in a parked state, false otherwise 
	 */
	public boolean wasParked() {
		return hasParked;
	}

	
	
	/**
	 * Boolean status indicating whether vehicle was ever queued
	 * @return true if vehicle was or is in a queued state, false otherwise 
	 */
	public boolean wasQueued() {
		return hasQueued;
	}
}
