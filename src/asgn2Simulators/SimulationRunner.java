/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 27/05/2014
 * 
 */
package asgn2Simulators;

import java.io.IOException;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;

/**
 * Class to operate the simulation, taking parameters and utility methods from the Simulator
 * to control the CarPark, and using Log to provide a record of operation. 
 * @author Damon Jones n8857954
 *
 */
public class SimulationRunner {
	private CarPark carPark;
	private Simulator sim;
	
	private Log log;
	
	/**
	 * Constructor just does initialisation 
	 * @param carPark CarPark currently used 
	 * @param sim Simulator containing simulation parameters
	 * @param log Log to provide logging services 
	 */
	public SimulationRunner(CarPark carPark, Simulator sim,Log log) {
		this.carPark = carPark;
		this.sim = sim;
		this.log = log;
	}
	
	
	/**
	 * Method to run the simulation from start to finish. Exceptions are propagated upwards from Vehicle,
	 * Simulation and Log objects as necessary 
	 * @throws VehicleException if Vehicle creation or operation constraints violated 
	 * @throws SimulationException if Simulation constraints are violated 
	 * @throws IOException on logging failures
	 */
	public void runSimulation() throws VehicleException, SimulationException, IOException {
		this.log.initialEntry(this.carPark,this.sim);
		for (int time=0; time<=Constants.CLOSING_TIME; time++) {
			//queue elements exceed max waiting time
			if (!this.carPark.queueEmpty()) {
				this.carPark.archiveQueueFailures(time);
			}
			//vehicles whose time has expired
			if (!this.carPark.carParkEmpty()) {
				//force exit at closing time, otherwise normal
				boolean force = (time == Constants.CLOSING_TIME);
				this.carPark.archiveDepartingVehicles(time, force);
			}
			//attempt to clear the queue 
			if (!this.carPark.carParkFull()) {
				this.carPark.processQueue(time,this.sim);
			}
			// new vehicles from minute 1 until the last hour
			if (newVehiclesAllowed(time)) { 
				this.carPark.tryProcessNewVehicles(time,this.sim);
			}
			//Log progress 
			this.log.logEntry(time,this.carPark);
		}
		this.log.finalise(this.carPark);
	}

	/**
	 * Main program for the simulation 
	 * @param args Arguments to the simulation 
	 */
	public static void main(String[] args) {
		CarPark cp = new CarPark();
		Simulator s = null;
		Log l = null; 
		try {
			s = new Simulator();
			l = new Log();
		} catch (IOException | SimulationException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		/* Implement Argument Processing */
		//Non-magic numbers
		final int minimumArgs = 10;
		final int mustBeInts = 5;
		final int mustBeDoubles = 5;
		
		if (args.length == minimumArgs){
			
			//Arrays to store the parsed arguments.
			int intArgs [] = new int [mustBeInts];
			double doubleArgs [] = new double [mustBeDoubles];
			
			for (int i = 0; i < minimumArgs; i++){
				if (i < mustBeInts){
					//Try to parse the Int's from the command line.
					try {
						intArgs[i] = Integer.parseInt(args[i]);
						
					} catch (NumberFormatException e){
						System.err.println("Argument" + args[i] + " must be an integer.");
						System.exit(1);
					}
				}else{
					//Try to parse the double's from the command line.
					try {
						doubleArgs[i-mustBeDoubles] = Double.parseDouble(args[i]);
						
					} catch (NumberFormatException e){
						System.err.println("Argument" + args[i] + " must be a double.");
						System.exit(1);
					}
				}
			}
			//Create the new Car Park from the command line.
			cp = new CarPark (intArgs[0], intArgs[1], intArgs[2], intArgs[3]);
			//Create the new Simulator from the command line.
			try {
				s = new Simulator(intArgs[4], doubleArgs[3], doubleArgs[4], doubleArgs[0], doubleArgs[1], doubleArgs[2]);
				
			} catch (SimulationException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
		}
		/* End Argument Processing */
		
		//Run the simulation 
		SimulationRunner sr = new SimulationRunner(cp,s,l);
		try {
			sr.runSimulation();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} 
	} 

	/**
	 * Helper method to determine if new vehicles are permitted
	 * @param time int holding current simulation time
	 * @return true if new vehicles permitted, false if not allowed due to simulation constraints. 
	 */
	private boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >=1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}

}
