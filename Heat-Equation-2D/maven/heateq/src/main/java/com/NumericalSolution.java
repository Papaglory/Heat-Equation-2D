package com;

import org.ejml.simple.SimpleMatrix;

/**
 * @author Marius
 *
 * Contains the numerical solution given the initial value condition given by the points.
 * 
 * By creating a linear system Ax = b, we can find the change in temperature when time changes
 * i.e. du/dt. This change can be added to the current temperature map (dataMatrix) simulating
 * a small change in the temperature.
 * 
 * Math used:
 * du/dt = d^2u/dx^2 + d^2u/dy^2. Right hand side is approxiamted by the numerical quotient 
 * of second derivative f'' = [f(x-h) - 4*f(x) + f(x+h)] / h^2.
 * Left hand side is approximated by f' = [f(x+h) - f(x)] / h.
 */
public class NumericalSolution {

	/**
	 * Matrix containing the temperature for each pixel.
	 */
	private SimpleMatrix dataMatrix;
	
	/**
	 * Matrix containing the last calculated velocityMatrix given the temperature.
	 */
	private SimpleMatrix velocityMatrix;
	
	private final double alpha;
	
	public NumericalSolution(SimpleMatrix matrix, double alpha) {
		this.dataMatrix = matrix;
		this.alpha = alpha;
	}
	
	/**
	 * Each value (pixel) from the dataMatrix is put into a matrix. Note that the bound
	 * is not included in this new matrix because the temperature of the bound does not change.
	 * 
	 * A linear system Ax = b is then created where we solve for b. x is the vector
	 * containing the temperatures u1, u2, ..., un. When b is found we know the 
	 * change of temperature given time. The values (temperatures) of b is then put
	 * into a matrix and is then being added to the original dataMatrix simulating a change
	 * in temperature. 
	 * @param deltaTime
	 */
	public void updateVelocity(double deltaTime) {
		int rowSize = dataMatrix.getNumRows();
		int colSize = dataMatrix.getNumCols();
		int size = (rowSize - 2) * (colSize - 2);
		double[][] linSys = new double[size][size];
		
		//store the values for the x vector in Ax = b
		double[][] vectorData = new double[size][1];
		int linSysPos = 0; //used to keep track of current index i.e. pos in linSys in the inner loop
		//loop through dataMatrix skipping all boundary values
		for (int i = 1; i < rowSize - 1; i++) {
			for (int j = 1; j < colSize - 1; j++) {
				vectorData[linSysPos][0] = dataMatrix.get(i,j);
				
				linSys[linSysPos][linSysPos] = -4 * Math.pow(alpha, 2);
				//check behind in y
				if (i - 1 != 0)
					linSys[linSysPos][linSysPos - colSize + 2] = 1 * Math.pow(alpha, 2);

				//check in front y
				if (i + 1 != rowSize - 1)
					linSys[linSysPos][linSysPos + colSize - 2] = 1 * Math.pow(alpha, 2);
				
				//check behind in x
				if (j - 1 != 0)
					linSys[linSysPos][linSysPos - 1] = 1 * Math.pow(alpha, 2);
				
				//check in front x
				if (j + 1 != rowSize - 1)
					linSys[linSysPos][linSysPos + 1] = 1 * Math.pow(alpha, 2); 
				
				linSysPos++;
			}
		}
		
		SimpleMatrix linMatrix = new SimpleMatrix(linSys);
		
		SimpleMatrix x = new SimpleMatrix(vectorData);
		SimpleMatrix result = linMatrix.mult(x);
		
		double scalar = deltaTime * deltaTime;
		
		//multiply all elements by the scalar
		for (int i = 0; i < result.getNumElements(); i++) {
			double val = result.get(i);
			result.set(i, val * scalar);
		}
				
		//put result vector into a result matrix with same width and height as currentData
		SimpleMatrix resultMatrix = new SimpleMatrix(rowSize, colSize);
		
		int vectorPosCounter = 0;
		for (int i = 1; i < rowSize-1; i++) {
			for (int j = 1; j < colSize-1; j++) {
				double val = result.get(vectorPosCounter);
				resultMatrix.set(i, j, val);
				vectorPosCounter++;
			}
		}
		//update velocityMatrix
		velocityMatrix = resultMatrix;
	}
	
	public void updateData() {
		dataMatrix = dataMatrix.plus(velocityMatrix);
	}
	
	public SimpleMatrix getSolution() {
		return dataMatrix;
	}
}