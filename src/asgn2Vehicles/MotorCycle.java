/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Vehicles 
 * 27/05/2014
 * 
 */
package asgn2Vehicles;

import asgn2Exceptions.VehicleException;

/**
 * The MotorCycle class is a 'specialisation' of the Vehicle class to cater for motorcycles
 * This version uses only those facilities specified in the Vehicle class 
 * 
 * @author Damon Jones n8857954
 *
 */
public class MotorCycle extends Vehicle {

	/**
	 * MotorCycle constructor 
	 * @param vehID - identification number or plate of the vehicle
	 * @param arrivalTime - time (minutes) at which the vehicle arrives and is 
	 *        either queued or given entry to the carpark 
	 * @throws VehicleException if arrivalTime is <= 0  
	 */
	public MotorCycle(String vehID, int arrivalTime) throws VehicleException {
		super(vehID, arrivalTime);
	}
}
