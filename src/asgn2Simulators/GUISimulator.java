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
import java.awt.Color;
import java.awt.Component;
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

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable, ActionListener {
	private SimulationRunner simRunner;
	private Simulator sim;
	private CarPark carPark;
	private Log log;
	
	private final int WIDTH = 300;
	private final int HEIGHT = 500;
	// Places where we'll add components to a frame
	private enum Position {MIDDLELEFT, TOPCENTRE, MIDDLECENTRE, BOTTOMCENTRE};
	// How big a margin to allow for the main frame
	final Integer mainMargin = 20; // pixels
	
	private JButton startButton;
	
	//Group 2 fields
	private JTextField maxCarSpaces;
	private JTextField maxSmallCarSpaces;
	private JTextField maxMotorCycleSpaces;
	private JTextField maxQueueSpaces;
	//Group 3 fields
	private JTextField seed;
	private JTextField probCar;
	private JTextField probSmallCar;
	private JTextField probMotorCycle;
	private JTextField duration;
	private JTextField durationSD;

	// Display for simulation messages
	private JTextArea display;           
	private JScrollPane textScrollPane; 
	
	private JPanel pnlCentre;
	private JPanel pnlTop;
	private JPanel pnlBot;
	private JPanel pnlRight;
	private JPanel pnlLeft;
	private JPanel pnlParms;

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	private void createGUI() {
		//setSize(WIDTH, HEIGHT);//Manually setup the window
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(new BorderLayout());
	    
	    //
		GridBagLayout layout = new GridBagLayout();

	    
	    //Solution code uses different colours to highlight different panels 
	    pnlCentre = createPanel(Color.BLUE);
	    pnlTop = createPanel(Color.CYAN);
	    pnlBot = createPanel(Color.DARK_GRAY);
	    pnlRight = createPanel(Color.RED);
	    pnlLeft = createPanel(Color.GREEN);
	    pnlParms = createPanel(Color.PINK);
	    
	    this.getContentPane().add(pnlCentre,BorderLayout.CENTER);
	    this.getContentPane().add(pnlTop,BorderLayout.NORTH);
	    this.getContentPane().add(pnlBot,BorderLayout.SOUTH);
	    this.getContentPane().add(pnlRight,BorderLayout.EAST);
	    this.getContentPane().add(pnlLeft,BorderLayout.WEST);
	    
		setLayout(layout);
		pnlBot.setLayout(layout);
		pnlCentre.setLayout(layout);
		pnlTop.setLayout(layout);
		pnlRight.setLayout(layout);
		pnlLeft.setLayout(layout);
		pnlParms.setLayout(layout);

	    repaint();
	    
	 // Create a scrollable text area for displaying instructions and messages
 		display = new JTextArea(5, 40); // lines by columns
 		display.setEditable(false);
 		display.setLineWrap(true);
 		textScrollPane = new JScrollPane(display);
 		this.add(textScrollPane, positionConstraints(Position.TOPCENTRE, mainMargin));
 		display.setText("Set the initial simulation parameters and press 'Start'\n\n");
	    
	    startButton = createButton("Start");
	    pnlBot.add(startButton);
	    
	    //Group 2 args
	    maxCarSpaces = addParameterPanel("Max Car Spaces:", Constants.DEFAULT_MAX_CAR_SPACES);
	    maxSmallCarSpaces = addParameterPanel("Max Small Car Spaces:", Constants.DEFAULT_MAX_SMALL_CAR_SPACES);
	    maxMotorCycleSpaces = addParameterPanel("Max MotorCycle Spaces:", Constants.DEFAULT_MAX_MOTORCYCLE_SPACES);
	    maxQueueSpaces = addParameterPanel("Max Queue Size:", Constants.DEFAULT_MAX_QUEUE_SIZE);
	    //group 3 args
	    seed = addParameterPanel("Random Number Seed:", Constants.DEFAULT_SEED);
	    probCar = addParameterPanel("Car Probability:", Constants.DEFAULT_CAR_PROB);
	    probSmallCar = addParameterPanel("Small Car Probability:", Constants.DEFAULT_SMALL_CAR_PROB);
	    probMotorCycle = addParameterPanel("MotorCycle Probabilty:", Constants.DEFAULT_MOTORCYCLE_PROB);
	    duration = addParameterPanel("Average Stay Duration:", Constants.DEFAULT_INTENDED_STAY_MEAN);
	    durationSD = addParameterPanel("Stay Standard Deviation:", Constants.DEFAULT_INTENDED_STAY_SD);
	    
	    layoutButtonPanel(); 
	    
	    this.setVisible(true);
	    this.pack();//this uses the automated pack to setup the window.
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		createGUI();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        SwingUtilities.invokeLater(new GUISimulator("BorderLayout"));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Get event's source 
		Object source = e.getSource(); 

		//Consider the alternatives (not all are available at once) 
		if (source == startButton)
		{
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
		    
			//startSimulation
		    try {
				sim = new Simulator(str_Int(seed.getText()), str_Double(duration.getText()), str_Double(durationSD.getText()),
						str_Double(probCar.getText()), str_Double(probSmallCar.getText()), str_Double(probMotorCycle.getText()));
			} catch (SimulationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    carPark = new CarPark(str_Int(maxCarSpaces.getText()), str_Int(maxSmallCarSpaces.getText()),
		    		str_Int(maxMotorCycleSpaces.getText()), str_Int(maxQueueSpaces.getText()));
		    try {
				log = new Log();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    simRunner = new SimulationRunner (carPark, sim, log);
		    try {
				simRunner.runSimulation();
			} catch (VehicleException | SimulationException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    display.setText(carPark.toString());
		}
		
	}
	
	
	
	private int str_Int(String word){
		try {
			return Integer.parseInt(word);
			
		} catch (NumberFormatException e){
			System.err.println("Argument" + word + " must be an integer.");
			System.exit(1);
			return -1;
		}
	}
	
	private double str_Double(String word){
		try {
			return Double.parseDouble(word);
			
		} catch (NumberFormatException e){
			System.err.println("Argument" + word + " must be an integer.");
			System.exit(1);
			return -1;
		}
	}
	
	
	private JPanel createPanel(Color c) {
		JPanel jp = new JPanel();
		jp.setBackground(c);
		return jp;
	}
	
	private JButton createButton(String str) {
		JButton jb = new JButton(str); 
		jb.addActionListener(this);
		return jb; 
	}
	
	private void layoutButtonPanel() {
	    
	    //add components to grid
	    GridBagConstraints constraints = new GridBagConstraints(); 
	    
	    //Defaults
	    constraints.fill = GridBagConstraints.NONE;
	    constraints.anchor = GridBagConstraints.CENTER;
	    constraints.weightx = 100;
	    constraints.weighty = 100;
	    
	    addToPanel(pnlBot, startButton,constraints,0,0,2,1);
	    
	    
	}
	
	/**
     * 
     * A convenience method to add a component to given grid bag
     * layout locations. Code due to Cay Horstmann 
     *
     * @param c the component to add
     * @param constraints the grid bag constraints to use
     * @param x the x grid position
     * @param y the y grid position
     * @param w the grid width
     * @param h the grid height
     */
   private void addToPanel(JPanel jp,Component c, GridBagConstraints constraints, int x, int y, int w, int h) {  
      constraints.gridx = x;
      constraints.gridy = y;
      constraints.gridwidth = w;
      constraints.gridheight = h;
      jp.add(c, constraints);
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
		pnlCentre.add(parameterPanel, positionConstraints(Position.MIDDLELEFT, mainMargin));
		// Return the newly-created text field (but not the label, which never changes)
		return parameterText;
	}

}
