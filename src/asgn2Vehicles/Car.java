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
 * The Car class is a specialization of the Vehicle class to cater for production cars
 * This version of the class does not cater for model types, but records whether or not the 
 * vehicle can use a small parking space. 
 * 
 * @author Damon Jones n8857954
 *
 */
public class Car extends Vehicle {
	
	//Declaring variables
	private boolean small; 

	/**
	 * The Car Constructor - small set at creation, not mutable. 
	 * @param vehID - identification number or plate of the vehicle
	 * @param arrivalTime - time (minutes) at which the vehicle arrives and is 
	 *        either queued or given entry to the carpark 
	 * @param small - indicator whether car is regarded as small or not
	 * @throws VehicleException if arrivalTime is <= 0
	 */
	public Car(String vehID, int arrivalTime, boolean small) throws VehicleException {
		super (vehID, arrivalTime);
		this.small = small;
	}

	
	/**
	 * Boolean status indicating whether car is small enough for small 
	 * car parking spaces  
	 * @return true if small parking space, false otherwise
	 */
	public boolean isSmall() {
		return small;
	}

	
	/* (non-Javadoc)
	 * @see asgn2Vehicles.Vehicle#toString()
	 */
	@Override
	public String toString() {
		//Get core string
		String result = coreString();
		
		//Add car string
		if (isSmall()){
			return result + "\nCar can use small car parking space";
		}
		return result + "\nCar cannot use small car parking space";
	}
}
