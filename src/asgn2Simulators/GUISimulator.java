/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 20/04/2014
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
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable, ActionListener {
	
	private Simulator sim;
	private CarPark carPark;
	private Log log;
	
	private final String StartText = "Set the initial simulation parameters and press 'Start'\n\n";
	private final int ErrorValue = -1114;
	
	// Places where we'll add components to a frame
	private enum Position {MIDDLELEFT, TOPCENTRE, MIDDLECENTRE, BOTTOMCENTRE, MIDDLERIGHT};
	// How big a margin to allow for the main frame
	final Integer mainMargin = 20; // pixels
	
	private JButton startButton;
	private JButton resetButton;
	private JButton outputType;
	
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
	
	private JPanel pnlLHS;
	private JPanel pnlData;
	private JPanel pnlParm;
	private JPanel pnlButtons;
	
	private Timer timer;
	private int time;
	private final int tickDelay = 10;
	
	private ChartPanel graph;
	private final String textMode = "Display Graph";
	private final String graphMode = "Display Text";
	
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
	}
	
	
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
	    
	    pnlLHS.setLayout(layout);
		pnlData.setLayout(layout);
		pnlParm.setLayout(layout);
		
	    add(pnlLHS,BorderLayout.WEST);
	    add(pnlData,BorderLayout.EAST);
		
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
	    startButton = createButton("Start");
	    pnlButtons.add(startButton);
	    resetButton = createButton("Reset");
	    resetButton.setEnabled(false);
	    pnlButtons.add(resetButton);
	    outputType = createButton(textMode);
	    pnlButtons.add(outputType);
	    
	    //Group 2 parameters
	    maxCarSpaces = addParameterPanel("Max Car Spaces:", Constants.DEFAULT_MAX_CAR_SPACES);
	    maxSmallCarSpaces = addParameterPanel("Max Small Car Spaces:", Constants.DEFAULT_MAX_SMALL_CAR_SPACES);
	    maxMotorCycleSpaces = addParameterPanel("Max MotorCycle Spaces:", Constants.DEFAULT_MAX_MOTORCYCLE_SPACES);
	    maxQueueSpaces = addParameterPanel("Max Queue Size:", Constants.DEFAULT_MAX_QUEUE_SIZE);
	    //group 3 parameters
	    seed = addParameterPanel("Random Number Seed:", Constants.DEFAULT_SEED);
	    probCar = addParameterPanel("Car Probability:", Constants.DEFAULT_CAR_PROB);
	    probSmallCar = addParameterPanel("Small Car Probability:", Constants.DEFAULT_SMALL_CAR_PROB);
	    probMotorCycle = addParameterPanel("MotorCycle Probabilty:", Constants.DEFAULT_MOTORCYCLE_PROB);
	    duration = addParameterPanel("Average Stay Duration:", Constants.DEFAULT_INTENDED_STAY_MEAN);
	    durationSD = addParameterPanel("Stay Standard Deviation:", Constants.DEFAULT_INTENDED_STAY_SD);
	    
	    timer = new Timer(tickDelay,this);
	    
	    graph = new ChartPanel("Car Park Simulator");
	    
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Potentially should take the same arguments as SimmulationRunner.java
        SwingUtilities.invokeLater(new GUISimulator("CarPark Simulator"));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get event's source 
		Object source = e.getSource(); 

		//Consider the alternatives (not all are available at once) 
		if (source == startButton && checkValues())
		{
			tryStartSimulation();
		}
		if (source == resetButton){
			startButton.setText("Start");
		    resetButton.setEnabled(false);
		    clearText();
		    reset(StartText);
		}
		
		if (source == timer){
			try {
				runSimulation();
			} catch (VehicleException | SimulationException | IOException e1) {
				reset(e1.toString());
			}
		}
		if (source == outputType){
			if (outputType.getText()== textMode){
				pnlData.remove(textScrollPane);
				pnlData.add(graph, positionConstraints(Position.TOPCENTRE, mainMargin));
				outputType.setText(graphMode);
				this.pack();
			}else{
				pnlData.remove(graph);
				pnlData.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
				outputType.setText(textMode);
				this.pack();
			}
		}
	}


	/**
	 * 
	 */
	private void tryStartSimulation() {
		startButton.setEnabled(false);
		maxCarSpaces.setEditable(false);
		maxSmallCarSpaces.setEditable(false);
		maxMotorCycleSpaces.setEditable(false);
		maxQueueSpaces.setEditable(false);
		//group 3 args
		seed.setEditable(false);
		probCar.setEditable(false);
		probSmallCar.setEditable(false);
		probMotorCycle.setEditable(false);
		duration.setEditable(false);
		durationSD.setEditable(false);
		
		//Setup Variables
		try {
			sim = new Simulator(mySeed, meanDuration, meanDurationSD, carProb, smallCarProb, motorCycleProb);
		} catch (SimulationException e1) {
			reset(e1.toString());
		}
		
		carPark = new CarPark(carSpaces, smallCarSpaces, motorCycleSpaces, queueSpaces);
		
		try {
			log = new Log();
		} catch (IOException e1) {
			reset(e1.toString());
		}
		

		time = 0;
		timer.start();
		startButton.setText("Again");
		resetButton.setEnabled(true);
	}
	
	
	private boolean checkValues(){
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
		
		if (carSpaces == ErrorValue){
			addText("Cars Spaces must be an Integer.\n");
			bool = false;
		}
		if (smallCarSpaces == ErrorValue){
			addText("Small Cars Spaces must be an Integer.\n");
			bool = false;
		}
		if (motorCycleSpaces == ErrorValue){
			addText("MotorCycle Spaces must be an Integer.\n");
			bool = false;
		}
		if (queueSpaces == ErrorValue){
			addText("Queue Spaces must be an Integer.\n");
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
	
	
	private int str_Int(String word){
		try {
			return Integer.parseInt(word);
			
		} catch (NumberFormatException e){
			return ErrorValue;
		}
	}
	
	
	private double str_Double(String word){
		try {
			return Double.parseDouble(word);
			
		} catch (NumberFormatException e){
			return ErrorValue;
		}
	}
	
	
	public void addText(String words){
		display.setText(display.getText() + words);
	}
	
	
	public void setText(String words){
		display.setText(words);
	}
	
	
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
		if (time == 0){
		    clearText();
		    addText("Start of Simulation\n" + carPark.initialState() +"\n\n");
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
		
		if (time >= Constants.CLOSING_TIME){
			this.log.finalise(this.carPark);
		    addText("\nEnd of Simulation\n");
		    timer.stop();
		    startButton.setEnabled(true);
		    }
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
	
	
	private JPanel createPanel() {
		JPanel jp = new JPanel();
		return jp;
	}
	
	
	private JButton createButton(String str) {
		JButton jb = new JButton(str); 
		jb.addActionListener(this);
		return jb; 
	}
	
   
	/*
	 * Convenience method for creating a set of positioning constraints for the
	 * specific layout we want for components of our GUI
	 */
	private GridBagConstraints positionConstraints(Position location, Integer margin) {
		//TODO Cull un-necessary cases.
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
		case MIDDLERIGHT:
			constraints.anchor = GridBagConstraints.EAST;
			constraints.insets = new Insets(0, margin, 0, margin); // top, left, bottom, right	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // component occupies whole row
			constraints.weightx = 100; // give extra horizontal space to this object
			break;
		}
		return constraints;
	}
   
	
	/*
	 * Convenience method to add a labelled, editable text field to the
	 * main frame, with a fixed label and a mutable default text value
	 */
	private JTextField addParameterPanel(String label, Number defaultValue) {
		// A parameter panel has two components, a label and a text field
		JPanel parameterPanel = new JPanel();
		JLabel parameterLabel = new JLabel(label);
		JTextField parameterText = new JTextField("" + defaultValue, 3);
		// Add the label to the parameter panel
		parameterLabel.setHorizontalAlignment(JTextField.RIGHT); // flush right
		parameterPanel.add(parameterLabel);
		// Add the text field
		parameterText.setEditable(true);
		parameterText.setHorizontalAlignment(JTextField.RIGHT); // flush right
		parameterPanel.add(parameterText);
		// Add the parameter panel to the main frame
		pnlParm.add(parameterPanel, positionConstraints(Position.MIDDLELEFT, mainMargin));
		// Return the newly-created text field (but not the label, which never changes)
		return parameterText;
	}

}
