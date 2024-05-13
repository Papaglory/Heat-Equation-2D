package com;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marius
 * 
 * Description of main():
 * 		- Setup of different parameters concerning the heat equation 2D.
 * 		- Asks the HeatSimulation to create a NumericalSolution containing the solution.
 * 		- Asks Drawer to create a canvas to draw upon.
 * 		- Runs the simulation loop, increasing time t, animating the solution.
 */
public class MainHeat {
	public static void main (String[] args) {
		//DRAWER PARAMETER SETTINGS:
		final int PADDINGX 		= -210; //pad the simulation in the x direction in the app window
		final int PADDINGY 		= -200; //pad the simulation in the y direction in the app window
		final int SCREENWIDTH 	= 700; //width of the app window
		final int SCREENHEIGHT	= 700; //height of the app window
		final int PIXELSIZE		= 8; //1 pixel is scaled up to a PIXELSIZE by PIXELSIZE pixel
		//HEAT EQUATION PARAMETER SETTINGS:
		/*
		 * Thermal diffusion constant.
		 * Determines how quickly heat spreads through our surface (material).
		 * Example:
		 * 		Steel has a higher value than wood due "to heat traveling 
		 * 		faster through steel".
		 * 
		 * We use an unrealistic high value for better animation purposes (speed).
		 * For instance, thermal diffusivity of copper is approx. 1.11e-4 m^2/s. 
		 */
		final double alpha 			= 5f; 
		//NUMERICAL PARAMETER SETTINGS:
		/*
		 * Time step in seconds used to obtain numerical solution.
		 * It represents the size of each time increment used in the numerical solution.
		 */
		final double deltaTime		= 0.1; 
		final int meshX			 	= 50; // determines the width of our simulation
		final int meshY	 			= 50; // determines the height of our simulation
		//ANIMATION PARAMETER SETTINGS:
		final boolean animate 		= true; // set to false for just a frame
		final double deltaFrame		= 0.05f; //interval between every frame in seconds
		
		// List containing the points used to create a spline surface
		List<Vertex> points = new ArrayList<>();

		//Example 1
		Vertex p0 = new Vertex(0,25,240);
		Vertex p1 = new Vertex(25,0,80);
		Vertex p2 = new Vertex(49,49,192);
		Vertex p3 = new Vertex(3,1,240);
		
		points.add(p0);
		points.add(p1);
		points.add(p2);
		points.add(p3);

		// Example 2
		// Vertex p0 = new Vertex(1,1,0);
		// Vertex p1 = new Vertex(1,2,0);
		// Vertex p2 = new Vertex(20,10,40);
		
		// points.add(p0);
		// points.add(p1);
		// points.add(p2);

		//Example 3
		// Vertex p0 = new Vertex(1,1,100);
		// Vertex p1 = new Vertex(1,49,120);
		// Vertex p2 = new Vertex(45,25,100);
		
		// points.add(p0);
		// points.add(p0);
		// points.add(p1);
		// points.add(p2);

		// Simulation setup
		HeatSimulation simulation = new HeatSimulation();
		NumericalSolution ns = simulation.setupSimulation(points, alpha, meshX, meshY);
		
		// Window setup
		Drawer.createWindow(ns, SCREENWIDTH, SCREENHEIGHT, PADDINGX, PADDINGY, PIXELSIZE);
		
		// Start simulation
		ns.updateVelocity(deltaTime);
		runSimulation(ns, animate, deltaTime, deltaFrame);		
	}
	
	/**
	 * Run the simulation, creating an animation.
	 * @param ns			Solution to be animated.
	 * @param animate		Either animate or give a frozen image.
	 * @param deltaTime	    Time step in seconds used to obtain numerical solution.
	 * @param deltaFrame	Time in seconds between the frames.
	 */
	private static void runSimulation(NumericalSolution ns, boolean animate, double deltaTime, double deltaFrame) {
		double startTime = System.currentTimeMillis();
		double time = 0d;
		if (animate)
			while(true) time = nextFrame(ns, deltaTime, startTime, time, deltaFrame);
	}
	
	/**
	 * This method creates a new frame given the current time in the simulation.
	 * 
	 * All calculations are done in milliseconds because it somehow is more accurate than doing it in seconds.
	 * 
	 * @param ns			Solution to be animated.
	 * @param deltaTime	    Time step in seconds used to obtain numerical solution.
	 * @param startTime		System start time in milliseconds.
	 * @param time			The time in milliseconds when we want to create the next frame.
	 * @param deltaFrame	Time in seconds between the frames.
	 * @return				The time in milliseconds for when we want to create the next frame.
	 */
	private static double nextFrame(NumericalSolution ns, double deltaTime, double startTime, double time, double deltaFrame) {
		if (time < System.currentTimeMillis() - startTime) {
			time = (System.currentTimeMillis() - startTime);
			// Update the time for creating the next window
			time += deltaFrame*1000; // Multiply to get in milliseconds
			//giving the Graph the new time so that it can update the points it wants to draw
			ns.updateVelocity(deltaTime);
			ns.updateData();
			Drawer.UpdateFrame((int) (time/1000)); //divide by 1000 to get in seconds
		}
		return time;
	}
}