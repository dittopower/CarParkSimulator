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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;

/**
 * This class is responsible for making and running the GUI version of {@link asgn2Simulators.SimulationRunner}.
 * @author Damon Jones n8857954
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable, ActionListener {
	
	//Simulation Components
	private Simulator sim;
	private CarPark carPark;
	private Log log;

	//Setup Variables
	private enum Position {MIDDLELEFT, TOPCENTRE, MIDDLECENTRE, BOTTOMCENTRE};
	// How big a margin to allow for the main frame
	final Integer mainMargin = 20; // pixels	
	private final String StartText = "Set the initial simulation parameters and press 'Start'\n\n";
	private final int ErrorValue = -1114;
	
	//buttons
	private JButton startBtn;
	private JButton resetBtn;
	private JButton outputType;
	private JButton finalGraphBtn;
	
	//Group 2 fields
	private JTextField maxCarSpaces;
	private int carSpaces;
	private JTextField maxSmallCarSpaces;
	private int smallCarSpaces;
	private JTextField maxMotorCycleSpaces;
	private int motorCycleSpaces;
	private JTextField maxQueueSpaces;
	private int queueSpaces;
	
	//Group 3 fields
	private JTextField seed;
	private int mySeed;
	private JTextField probCar;
	private double carProb;
	private JTextField probSmallCar;
	private double smallCarProb;
	private JTextField probMotorCycle;
	private double motorCycleProb;
	private JTextField duration;
	private double meanDuration;
	private JTextField durationSD;
	private double meanDurationSD;

	// Display for simulation messages
	private JTextArea display;           
	private JScrollPane textScrollPane; 
	
	//panels
	private JPanel pnlLHS;
	private JPanel pnlData;
	private JPanel pnlParm;
	private JPanel pnlButtons;
	
	//timer
	private Timer timer;
	private int time;
	private final int tickDelay = 1;
	
	//Graph related objects
	private ChartPanel graph;
	private final String textMode = "Display Graph";
	private final String graphMode = "Display Text";
	private final String title = "Car Park Simulator";
	private final String fnlGraph = "Normal Graph";
	private final String nmlGraph = "Overview Graph";
	
	
	
	/**
	 * Create the GUI and set it up with the defaults
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		carSpaces = Constants.DEFAULT_MAX_CAR_SPACES;
		smallCarSpaces = Constants.DEFAULT_MAX_SMALL_CAR_SPACES;
		motorCycleSpaces = Constants.DEFAULT_MAX_MOTORCYCLE_SPACES;
		queueSpaces = Constants.DEFAULT_MAX_QUEUE_SIZE;
		mySeed = Constants.DEFAULT_SEED;
		carProb = Constants.DEFAULT_CAR_PROB;
		smallCarProb = Constants.DEFAULT_SMALL_CAR_PROB;
		motorCycleProb = Constants.DEFAULT_MOTORCYCLE_PROB;
		meanDuration = Constants.DEFAULT_INTENDED_STAY_MEAN;
		meanDurationSD = Constants.DEFAULT_INTENDED_STAY_SD;
	}
	
	
	/**
	 * Create the GUI and set it up with the defaults
	 * @param arg0
	 * @param num1 max car spaces
	 * @param num2 max small car spaces
	 * @param num3 max motorcycle spaces
	 * @param num4 max queue spaces
	 * @param num5 random generator seed
	 * @param num6 car probability
	 * @param num7 small car probability
	 * @param num8 motorcycle probability
	 * @param num9 mean stay duration
	 * @param num0 mean stay standard deviation
	 */
	public GUISimulator(String arg0, int num1, int num2, int num3, int num4, int num5,
			double num6, double num7, double num8, double num9, double num0){
		super(arg0);
		carSpaces = num1;
		smallCarSpaces = num2;
		motorCycleSpaces = num3;
		queueSpaces = num4;
		mySeed = num5;
		carProb = num6;
		smallCarProb = num7;
		motorCycleProb = num8;
		meanDuration = num9;
		meanDurationSD = num0;
	}
	
	
	/**
	 * Assembles the GUI and sets the interfaces initial values.
	 * 
	 */
	private void createGUI() {
//		setSize(WIDTH, HEIGHT);//Manually setup the window
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(new BorderLayout());
	    
		GridBagLayout layout = new GridBagLayout();
	   
	    //create panels 
	    pnlData = createPanel();
	    pnlParm = createPanel();
	    pnlButtons = createPanel();
	    pnlLHS = createPanel();
	    
	    //Set layout style
	    pnlLHS.setLayout(layout);
		pnlData.setLayout(layout);
		pnlParm.setLayout(layout);
		
		//Add to the window
	    add(pnlLHS,BorderLayout.WEST);
	    add(pnlData,BorderLayout.EAST);
		
	    //Put the parameters and button in their section of the window
	    pnlLHS.add(pnlParm, positionConstraints(Position.MIDDLECENTRE, mainMargin));
	    pnlLHS.add(pnlButtons, positionConstraints(Position.BOTTOMCENTRE, mainMargin));
	    
	    // Create a scrollable text area for displaying instructions and messages
 		display = new JTextArea(20, 40); // lines by columns
 		display.setEditable(false);
 		display.setLineWrap(true);
 		textScrollPane = new JScrollPane(display);
 		pnlData.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
 		setText(StartText);

 		// Create Buttons   
	    startBtn = createButton("Start");
	    pnlButtons.add(startBtn);
	    resetBtn = createButton("Reset");
	    resetBtn.setEnabled(false);
	    pnlButtons.add(resetBtn);
	    outputType = createButton(textMode);
	    pnlButtons.add(outputType);
	    finalGraphBtn = createButton(nmlGraph);
	    pnlButtons.add(finalGraphBtn);
	    
	    //Group 2 parameters
	    maxCarSpaces = addParameterPanel("Max Car Spaces:", carSpaces);
	    maxSmallCarSpaces = addParameterPanel("Max Small Car Spaces:", smallCarSpaces);
	    maxMotorCycleSpaces = addParameterPanel("Max MotorCycle Spaces:", motorCycleSpaces);
	    maxQueueSpaces = addParameterPanel("Max Queue Size:", queueSpaces);
	    //group 3 parameters
	    seed = addParameterPanel("Random Number Seed:", mySeed);
	    probCar = addParameterPanel("Car Probability:", carProb);
	    probSmallCar = addParameterPanel("Small Car Probability:", smallCarProb);
	    probMotorCycle = addParameterPanel("MotorCycle Probabilty:", motorCycleProb);
	    duration = addParameterPanel("Average Stay Duration:", meanDuration);
	    durationSD = addParameterPanel("Stay Standard Deviation:", meanDurationSD);
	    
	    //setup timer
	    timer = new Timer(tickDelay,this);
	    //Setup graph
	    graph = new ChartPanel(title);
	    
	    //make visible and assemble/render the window.
	    this.setVisible(true);
	    this.pack();//this uses the automated pack to setup the window.
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		createGUI();
	}

	
	/**
	 * Runs the GUI version of the simulator.
	 * @param args 10 values representing the group 2 & 3 parameters.
	 */
	public static void main(String[] args) {
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
			//Start the GUI sim.
			SwingUtilities.invokeLater(new GUISimulator("CarPark Simulator",intArgs[0],intArgs[1],intArgs[2],intArgs[3],intArgs[4],doubleArgs[0],doubleArgs[1],doubleArgs[2],doubleArgs[3],doubleArgs[4]));
		}else{
		//no arguments start the GUI sim.
        SwingUtilities.invokeLater(new GUISimulator("CarPark Simulator"));
		}
	}

	/**
	 * Makes the buttons run their respective functions.
	 * @param e The action-event that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get event's source 
		Object source = e.getSource(); 

		//Start button
		if (source == startBtn && checkValues())
		{
			tryStartSimulation();
		}
		
		//Reset button
		if (source == resetBtn){
			//set everything back to it's initial state
			startBtn.setText("Start");
			startBtn.setEnabled(true);
			timer.stop();
		    resetBtn.setEnabled(false);
		    clearText();
		    //Most of this isn't in the reset function because that function can be called at other times.
		    reset(StartText);
		    graph.clearData();
		}
		
		//The timer
		if (source == timer){
			try {
				runSimulation();
			} catch (VehicleException | SimulationException | IOException e1) {
				reset(e1.toString());
			}
		}
		
		//The text/graph output button.
		if (source == outputType){
			swapOutput();
		}
		
		//The swap between graphs button
		if (source == finalGraphBtn){
			//Swap
			graph.switchGraph();
			
			//if necessary swap output type and button names as well.
			if (outputType.getText()== textMode){
				swapOutput();
			}
			if (finalGraphBtn.getText() == nmlGraph){
				finalGraphBtn.setText(fnlGraph);
			}else{
				finalGraphBtn.setText(nmlGraph);
			}
		}
	}
	

	/**
	 * Swaps the textarea and graph on the GUI display.
	 */
	private void swapOutput() {
		//if it's currently the text area
		if (outputType.getText()== textMode){
			//swap to the graph
			pnlData.remove(textScrollPane);
			pnlData.add(graph, positionConstraints(Position.TOPCENTRE, mainMargin));
			outputType.setText(graphMode);
			this.pack();
		//otherwise swap to the text area
		}else{
			pnlData.remove(graph);
			pnlData.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
			outputType.setText(textMode);
			this.pack();
		}
	}


	/**
	 * Locks all the un-necessary fields, creates the simulation components and starts the simulation running.
	 */
	private void tryStartSimulation() {
		//Lock fields
		startBtn.setEnabled(false);
		maxCarSpaces.setEditable(false);
		maxSmallCarSpaces.setEditable(false);
		maxMotorCycleSpaces.setEditable(false);
		maxQueueSpaces.setEditable(false);
		seed.setEditable(false);
		probCar.setEditable(false);
		probSmallCar.setEditable(false);
		probMotorCycle.setEditable(false);
		duration.setEditable(false);
		durationSD.setEditable(false);
		
		//Setup simulation components
		try {
			//simulator
			sim = new Simulator(mySeed, meanDuration, meanDurationSD, carProb, smallCarProb, motorCycleProb);
		} catch (SimulationException e1) {
			reset(e1.toString());
		}
		//carpark
		carPark = new CarPark(carSpaces, smallCarSpaces, motorCycleSpaces, queueSpaces);
		try {
			//log
			log = new Log();
		} catch (IOException e1) {
			reset(e1.toString());
		}
		//Start the simulation timer
		time = 0;
		timer.start();
		
		//Set buttons/button states.
		startBtn.setText("Again");
		resetBtn.setEnabled(true);
	}
	
	
	/**
	 * Checks the input values are valid and sets the to variables or show error messages in the textarea.
	 * @return true if all fields are valid, otherwise false
	 */
	private boolean checkValues(){
		//try getting/setting the values
		boolean bool = true;
		carSpaces = str_Int(maxCarSpaces.getText());
		smallCarSpaces = str_Int(maxSmallCarSpaces.getText());
		motorCycleSpaces =  str_Int(maxMotorCycleSpaces.getText());
		queueSpaces = str_Int(maxQueueSpaces.getText());
		mySeed = str_Int(seed.getText());
		meanDuration = str_Double(duration.getText());
		meanDurationSD = str_Double(durationSD.getText());
		carProb = str_Double(probCar.getText());
		smallCarProb =str_Double(probSmallCar.getText());
		motorCycleProb = str_Double(probMotorCycle.getText());
		
		//Test them against their respective constraints.
		if (carSpaces < 0){
			addText("Cars Spaces must be a non-negative Integer.\n");//show error in textarea
			bool = false;//set return to failed
		}
		if (smallCarSpaces < 0 || smallCarSpaces > carSpaces){
			addText("Small Cars Spaces must be a non-negative Integer, no larger than Car Spaces.\n");
			bool = false;
		}
		if (motorCycleSpaces < 0){
			addText("MotorCycle Spaces must be a non-negative Integer.\n");
			bool = false;
		}
		if (queueSpaces < 0){
			addText("Queue Spaces must be a non-negative Integer.\n");
			bool = false;
		}
		if (mySeed == ErrorValue){
			addText("Seed must be an Integer.\n");
			bool = false;
		}
		if (meanDuration < 0 || meanDuration == ErrorValue){
			addText("Intended Duration must be a number larger than 0.\n");
			bool = false;
		}
		if (meanDurationSD < 0 || meanDurationSD == ErrorValue){
			addText("Durartion Deviation must be a number larger than 0.\n");
			bool = false;
		}
		if (invalidProb(carProb)){
			addText("Car Probability must be a double in the range 0-1.\n");
			bool = false;
		}
		if (invalidProb(smallCarProb)){
			addText("Small Car Probability must be a double in the range 0-1.\n");
			bool = false;
		}
		if (invalidProb(motorCycleProb)){
			addText("MotorCycle Probability must be a double in the range 0-1.\n");
			bool = false;
		}
		//return whether all values passed
		return bool;
	}
	
	
	/**
	 * Helper method to ensure valid probability 
	 * @param prob double holding probability 
	 * @return true if valid, false if prob > 1.0  or prob < 0.0
	 */
	private boolean invalidProb(double prob) {
		return (prob < 0.0) || (prob > 1.0);
	}
	
	
	/**
	 * resets accessibility to parameter fields and display why it was reset.
	 * @param reason the reason the GUI was reset.
	 */
	private void reset(String reason){
		addText(reason);
		maxCarSpaces.setEditable(true);
	    maxSmallCarSpaces.setEditable(true);
	    maxMotorCycleSpaces.setEditable(true);
	    maxQueueSpaces.setEditable(true);
	    //group 3 args
	    seed.setEditable(true);
	    probCar.setEditable(true);
	    probSmallCar.setEditable(true);
	    probMotorCycle.setEditable(true);
	    duration.setEditable(true);
	    durationSD.setEditable(true);
	}
	
	
	/**
	 * Parses strings to integers or set them to the error value.
	 * @param word the string that should be an integer
	 * @return the int version of the string or the error value.
	 */
	private int str_Int(String word){
		try {
			return Integer.parseInt(word);
			
		} catch (NumberFormatException e){
			return ErrorValue;
		}
	}
	
	
	/**
	 * Parses strings to doubles or set them to the error value.
	 * @param word the string that should be a double
	 * @return the double version of the string or the error value.
	 */
	private double str_Double(String word){
		try {
			return Double.parseDouble(word);
			
		} catch (NumberFormatException e){
			return ErrorValue;
		}
	}
	
	
	/**
	 * Adds the given text to the textarea in the window.
	 * @param words the text to be added
	 */
	public void addText(String words){
		display.setText(display.getText() + words);
	}
	
	
	/**
	 * Sets the text of the textarea in the window.
	 * @param words the text the window should display.
	 */
	public void setText(String words){
		display.setText(words);
	}
	
	
	/**
	 * Clears the textarea in the window.
	 */
	private void clearText(){
		display.setText("");
	}
	
	
	/**
	 * Method to run the simulation from start to finish. Exceptions are propagated upwards from Vehicle,
	 * Simulation and Log objects as necessary 
	 * @throws VehicleException if Vehicle creation or operation constraints violated 
	 * @throws SimulationException if Simulation constraints are violated 
	 * @throws IOException on logging failures
	 */
	private void runSimulation() throws VehicleException, SimulationException, IOException {
		//If at the initial time
		if (time == 0){
			//clear the display and run the initial methods
		    clearText();
		    graph.clearData();
		    addText("Start of Simulation\n" + sim.toString() + "\n"+ carPark.initialState() +"\n\n");
			this.log.initialEntry(this.carPark,this.sim);
		}
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
		addText(carPark.getStatus(time));
		graph.addData(carPark.getStatus(time));
		this.log.logEntry(time,this.carPark);
		
		//if at the finishing time
		if (time >= Constants.CLOSING_TIME){
			//run the final methods and stop the timer
			this.log.finalise(this.carPark);
		    addText("\nEnd of Simulation\n");
		    timer.stop();
		    startBtn.setEnabled(true);
		    }
		//increment simulation time
	    time++;
	}
	
	
	/**
	 * Helper method to determine if new vehicles are permitted
	 * @param time int holding current simulation time
	 * @return true if new vehicles permitted, false if not allowed due to simulation constraints. 
	 */
	protected boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >=1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}
	
	
	/* Start GUI Helper Methods */
	
	/**
	 * Creates a new panel.
	 * @return the new panel
	 */
	private JPanel createPanel() {
		JPanel jp = new JPanel();
		return jp;
	}
	
	
	/**
	 * Create a new button.
	 * @param str The Text on the button
	 * @return the new button
	 */
	private JButton createButton(String str) {
		JButton jb = new JButton(str); 
		jb.addActionListener(this);
		return jb; 
	}
	
   
	/**
	 * Helper method for positioning constraints with the layout for components of the GUI.
	 * @param location the position relative in it's contain we want it
	 * @param margin the space around each element
	 */
	private GridBagConstraints positionConstraints(Position location, Integer margin) {
		GridBagConstraints constraints = new GridBagConstraints();
		switch (location) {
		case TOPCENTRE:
			constraints.anchor = GridBagConstraints.NORTH;
			constraints.insets = new Insets(margin, margin, 0, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			break;
		case MIDDLECENTRE:
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.insets = new Insets(margin, margin, margin, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weighty = 100; // give extra vertical space to this object
			break;
		case BOTTOMCENTRE:
			constraints.anchor = GridBagConstraints.SOUTH;
			constraints.insets = new Insets(margin, margin, margin, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weighty = 100; // give extra vertical space to this object
			break;
		case MIDDLELEFT:
			constraints.anchor = GridBagConstraints.WEST;
			constraints.insets = new Insets(0, margin, 0, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weightx = 100; // give extra horizontal space to this object
			break;
		}
		return constraints;
	}
   
	
	/**
	 * Convenience method to add a labeled, editable text field to the
	 * main frame, with a fixed label and a mutable default text value
	 * @param label the Text to show next to the box
	 * @param defaultValue the value to have in the parameters box initially
	 * @return the textfield containing the parameter.
	 */
	private JTextField addParameterPanel(String label, Number defaultValue) {
		//make a panel to group the two components
		JPanel parameterPanel = new JPanel();
		//The parameter name/description
		JLabel parameterLabel = new JLabel(label);
		JTextField parameterText = new JTextField("" + defaultValue, 3);
		
		//add the label to the parameters panel
		parameterLabel.setHorizontalAlignment(JTextField.RIGHT); // flush right
		parameterPanel.add(parameterLabel);
		
		//add the text field
		parameterText.setEditable(true);
		parameterText.setHorizontalAlignment(JTextField.RIGHT); // flush right
		parameterPanel.add(parameterText);
		
		//add the parameter panel to the parameters panel
		pnlParm.add(parameterPanel, positionConstraints(Position.MIDDLELEFT, mainMargin));
		//Return the parameter field
		return parameterText;
	}

}
