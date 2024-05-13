package com;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.ejml.simple.SimpleMatrix;

/**
 * @author Marius
 * 
 * This is the primary panel where everything is drawn. The Drawer class creates a panel (this panel)
 * and draws everything that is on this panel.
 *
 */
public class PrimaryPanel extends JPanel {
	
	/**
	 * Compiler Warning if this is not present.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Origin of the canvas that shows up on the screen.
	 */
	private final int m_ORIGINX, m_ORIGINY;
	
	/**
	 * Scales a single pixel into a square with size PIXELSIZE by PIXELSIZE.
	 */
	private final int PIXELSIZE;
	
	/**
	 * Contains the information about the solution that is to be drawn.
	 */
	private final NumericalSolution ns;

	/*
	* In order to make better use of the 0 to 255 grayscale values we do two things.
	* First we scale all the values between 0 and 400. We use 400 because this allows
	* for using the whole range of 0 to 255 a bit longer than starting at 255.
	* Thereafter, we calculate the percentage of the current brightest pixel
	* to the overall brighest pixel seen at the start of the simulation. Over time,
	* will get less bright, thus the percentage smaller. This ensures that all pixels
	* the brighest pixel given an iteratinon eventually reach 0, because
	* without the percentage, when scaling the pixels between
	* 0 and 255, there would always be a pixel with the value 255.
	*/
	private boolean first = true;
	private double MAXIM;
	
	public PrimaryPanel(NumericalSolution ns, int ORIGINX, int ORIGINY, int PIXELSIZE) {
		this.ns = ns;
		m_ORIGINX = ORIGINX;
		m_ORIGINY = ORIGINY;
		this.PIXELSIZE = PIXELSIZE;
	}
	
	/**
	 * When drawing, note that the coordinate system is in "screen mode" / "matrix mode".
	 * Thus, make sure to use minus when increasing and plus when decreasing when dealing
	 * with the y-axis.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		SimpleMatrix matrix = ns.getSolution();

		// Get the element with the highest value (assumed to be positive)
		double maxim = matrix.elementMax();

		// If it is the first numerical solution, store the highest value temperature
		if (first) {
			MAXIM = maxim;
			first = false;
		}

		// Calculate the percentage of the current highest temperature to the overall highest seen
		double percentage = maxim / MAXIM;

		// Scale every temperature between 0 and 400 for better visualization
		double scaler = 400 / maxim;
		
		for (int i = 0; i < matrix.getNumRows(); i++) {
			for (int j = 0; j < matrix.getNumCols(); j++) {
				double val = matrix.get(i, j);
				
				// Scale value between 0 and 400
				val *= scaler;
				// Dim the pixel value depending on the brightest pixel
				val *= percentage;

				// Cast into integer
				int integerVal = (int)val;
				
				//clamp values between 0 and 255 just in case some values are outside
				if 		(integerVal > 255) integerVal = 255;
				else if (integerVal < 0) 	integerVal = 0;
				Color col = new Color(integerVal, integerVal, integerVal);
				
				//scale up pixels by pixelSize
				g.setColor(col);
				for (int k = 0; k < PIXELSIZE; k ++) {
					for (int l = 0; l < PIXELSIZE; l ++) {
						g.drawLine(m_ORIGINX + l + j * PIXELSIZE, m_ORIGINY - k - i * PIXELSIZE, m_ORIGINX + l + j * PIXELSIZE, m_ORIGINY - k - i * PIXELSIZE);
					}
				}
			}
		}
	}
}