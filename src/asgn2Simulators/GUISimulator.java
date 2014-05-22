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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
	
	
	// Mutable text fields for simulation parameters
	private JTextField maxCarSpaces;
	private JTextField maxSmallCarSpacest;
	private JTextField maxMotorCycleSpaces;
	private JTextField maxQueueSpaces;

	private JTextField seed;
	private JTextField carProb;
	private JTextField smallCarProb;
	private JTextField motorCycleProb;
	private JTextField intendedDuration;
	private JTextField intendedDurationSD;
	
	// How big a margin to allow for the main frame
	final Integer mainMargin = 20; // pixels
	
	// Places where we'll add components to a frame
	private enum Position {MIDDLELEFT, TOPCENTRE, MIDDLECENTRE, BOTTOMCENTRE};

	// Buttons
	private JButton startButton;
	
	// Display for simulation messages
	private JTextArea display;           
	private JScrollPane textScrollPane;
	

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    	// Create the main frame
	 	JFrame mainFrame = new JFrame();
	 	// Terminate the program if the user closes the main frame
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Set the title for the main frame
		mainFrame.setTitle("CarPark Simulator");
		// Add components to the frame
		//mainFrame.getContentPane().add(new GUISimulator());
		// Resize the main frame to fit its components
		mainFrame.pack();
        // Make the simulation visible
        mainFrame.setVisible(true);
	}

	
	private void initialiseComponents()
	{

		// Choose a grid layout for the main frame
		this.setLayout(new GridBagLayout());

		// Create a scrollable text area for displaying instructions and messages
		display = new JTextArea(5, 40); // lines by columns
		display.setEditable(false);
		display.setLineWrap(true);
		textScrollPane = new JScrollPane(display);
		this.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
		display.setText("Set the initial simulation parameters and press 'Start'\n\n");
		
		// Add editable panels for simulation parameters
//		seed = addParameterPanel("Random number seed:", DefaultRandomSeed);
//		capacityText = addParameterPanel("Dam capacity (megalitres):", DefaultDamCapacity);
//		maxInflowText = addParameterPanel("Maximum daily inflow (megalitres):", DefaultMaxInflow);
//		maxConsumptionText = addParameterPanel("Maximum daily consumption (megalitres):", DefaultMaxConsumption);
//		defaultReleaseText = addParameterPanel("Default downriver release (megalitres):", DefaultDailyRelease);
//		durationText = addParameterPanel("Job duration (days):", DefaultJobDuration);
		
//		// Panel to contain the buttons
//		buttons = new JPanel(new GridBagLayout());
//		buttons.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Dam",
//				CENTER, BOTTOM));
//		this.add(buttons, positionConstraints(Position.BOTTOMCENTRE, mainMargin));
//		buttons.setVisible(true);
		
//		// Button for starting the simulation
//		startButton = new JButton("Start");
//		startButton.addActionListener(this);
//		buttons.add(startButton);
		
//		// Buttons for controlling the simulation (initially unavailable)
//		halfReleaseButton = new JButton(" Half ");
//		halfReleaseButton.addActionListener(this);
//		halfReleaseButton.setVisible(false);
//		buttons.add(halfReleaseButton);
//		defaultReleaseButton = new JButton("Default");
//		defaultReleaseButton.addActionListener(this);
//		defaultReleaseButton.setVisible(false);
//		buttons.add(defaultReleaseButton);
//		doubleReleaseButton = new JButton("Double");
//		doubleReleaseButton.addActionListener(this);
//		doubleReleaseButton.setVisible(false);
//		buttons.add(doubleReleaseButton);

	}

	
	
	
	/*
	 * Convenience method for creating a set of positioning constraints for the
	 * specific layout we want for components of our GUI
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
}
