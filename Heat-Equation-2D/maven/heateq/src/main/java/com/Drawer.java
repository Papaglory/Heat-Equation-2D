package com;

import java.awt.Color;
import javax.swing.*;

public class Drawer extends JComponent {
	
	/**
	 * Compiler Warning if this is not present.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Label that is added to primaryPanel. It contains information about the simulation.
	 */
	static JLabel infoLabel;
	
	/**
	 * This is the empty window that contains the primary panel. 
	 */
	static JFrame frame;
	
	public static void createWindow(NumericalSolution ns, int SCREENWIDTH, int SCREENHEIGHT, int PADDINGX, int PADDINGY, int PIXELSIZE) {
	    frame = new JFrame("Heat Equation 2D");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.gray);
	    frame.setSize(SCREENWIDTH, SCREENHEIGHT);
	    frame.setVisible(true);
		frame.setResizable(true); //need to be true, if not the canvas might not show anything!
		//Finding Origin
		final int ORIGINX = SCREENWIDTH / 2  + PADDINGX;
		final int ORIGINY = SCREENHEIGHT / 2 - PADDINGY;
			    
		PrimaryPanel panel = new PrimaryPanel(ns, ORIGINX, ORIGINY, PIXELSIZE);
	    frame.add(panel);
	    //create infoLabel for time
	    infoLabel = new JLabel();
	    panel.add(infoLabel);
	}
	
	public static void UpdateFrame(int n) { 
		frame.repaint();
		//update infoLabel text for time
		infoLabel.setText("Time:    " + String.valueOf(n) + " seconds");
	}	
}