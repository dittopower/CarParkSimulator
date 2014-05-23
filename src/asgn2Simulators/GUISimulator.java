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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable, ActionListener {
	
	private final int WIDTH = 400;
	private final int HEIGHT = 700;
	
	private JButton startButton;

	private JPanel pnlCentre;
	private JPanel pnlTop;
	private JPanel pnlBot;
	private JPanel pnlRight;
	private JPanel pnlLeft;
	private JPanel pnlTest;

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	private void createGUI() {
		//setSize(WIDTH, HEIGHT);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(new BorderLayout());
	    
	    //Solution code uses different colours to highlight different panels 
	    pnlCentre = createPanel(Color.BLUE);
	    pnlTop = createPanel(Color.CYAN);
	    pnlBot = createPanel(Color.DARK_GRAY);
	    pnlRight = createPanel(Color.RED);
	    pnlLeft = createPanel(Color.GREEN);
	    pnlTest = createPanel(Color.PINK);
	    
	    this.getContentPane().add(pnlCentre,BorderLayout.CENTER);
	    this.getContentPane().add(pnlTop,BorderLayout.NORTH);
	    this.getContentPane().add(pnlBot,BorderLayout.SOUTH);
	    this.getContentPane().add(pnlRight,BorderLayout.EAST);
	    this.getContentPane().add(pnlLeft,BorderLayout.WEST);
	    
	    pnlCentre.add(pnlTest, BorderLayout.WEST);
	    repaint();
	    
	    startButton = createButton("Start");
	    
	    layoutButtonPanel(); 
	    
	    this.setVisible(true);
	    this.pack();
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
			//startSimulation();
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
		GridBagLayout layout = new GridBagLayout();
		pnlBot.setLayout(layout);
	    
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
	
	

}
