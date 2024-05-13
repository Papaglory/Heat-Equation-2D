package com;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

/**
 * @author Marius
 * Simulate the heat equation in 2D. Given the input points, this class will
 * create a spline polynomial surface intersecting the points. This surface is used as the
 * initial value condition. The surface is then put into an object called NumericalSolution
 * which is returned to the user. Class NumericalSolution contains all the information about
 * the solution.
 * 
 * Assumptions that need to be satisfied:
 * 			- Boundary condition is 0.
 */
public class HeatSimulation {
		
	/**
	 * Given points are turned into a spline surface. This spline surface is then projected
	 * onto a 2D mesh plane which contains the z-value.
	 * 
	 * Mesh explanation:
	 * 		The mesh consist of size meshX by meshY. Note that the outer values, that is, the bound of 
	 * 		the mesh is set to value 0. Values put in the mesh comes from the rectangle
	 * 		ranging from origin to boundX in one direction and from origin to boundY in the
	 * 		other direction.
	 * 
	 * 		Origin    ...     (boundX,0)
	 * 		.                     .
	 * 		.                     .
	 * 		.                     .
	 * 		(0,boundY) ... (boundX,boundY)
	 * 
	 * 		meshX and meshY are the amount of cells to create and deltaStep is the size of
	 * 		the steps taken when extracting from the spline surface.
	 * 
	 * @param points	Used to create the spline surface.
	 * @param alpha		Thermal diffusivity constant.	
	 * @param meshX		Mesh size in x direction.
	 * @param meshY		Mesh size in y direction.
	 * @return			A NumericalSoulution that contains all the information about the solution
	 * 					to the equation.
	 */
	public NumericalSolution setupSimulation(List<Vertex> points, double alpha, int meshX, int meshY) {
		if (points.isEmpty())
			throw new IllegalArgumentException("The list containing points is empty!");
		
		Polynomial poly = CreateSplineSurface(points);
		System.out.println(poly);
		//Project polynomial into a 2d mesh
		double[][] data = new double[meshY][meshX];
		double xPos = 0;
		double yPos = 0;
		for (int i = 0; i < meshY; i++) {
			for (int j = 0; j < meshX; j++) {
				//check if on the bound, set it fixed to zero.
				if (i == 0 || i == meshY-1 || j == 0 || j == meshX-1) {
					data[i][j] = 0;
					xPos += 1;
					continue;
				}
				//get polynomial value and put it into the mesh
				double val = poly.getConstant();
				//loop through xCoefficients for polynomial
				for (int termIndex = 0; termIndex < poly.getXSize(); termIndex++) {
					double coeff = poly.getNthCoefficient(termIndex, true);
					val += coeff * Math.pow(xPos, termIndex + 1);
				}
				//loop through yCoefficients for polynomial
				for (int termIndex = 0; termIndex < poly.getYSize(); termIndex++) {
					double coeff = poly.getNthCoefficient(termIndex, false);
					val += coeff * Math.pow(yPos, termIndex + 1);
				}
				
				data[i][j] = val; 
				
				xPos += 1;
			}
			//start over again but this time with an increase in yPos
			xPos = 0;
			yPos += 1;
		}
		
		SimpleMatrix matrix = new SimpleMatrix(data);
		
		return new NumericalSolution(matrix, alpha);
	}

	/**
	 * Create a spline surface given the points. Can choose any
	 * amount of points as long as there is at least one.
	 * 
	 * Note that this is a weaker implementation of a general spline surfare,
	 * that is, it does not model the interaction between the x and y
	 * varaibles. Non-linear relationships between x and y can thus
	 * not be modeled (there are no terms on the form x^ay^b where a,b are positive integers).
	 * The result is a potensial less accurate spline model in cases of non-linearity between x,y.
	 * 
	 * @param points	Points used to create the spline surface.
	 * @return			Returns a polynomial that is the surface. 
	 */
	private Polynomial CreateSplineSurface(List<Vertex> points) {
		Polynomial poly = new Polynomial(points.size());
		//used for readability
		int size = poly.getSize();
		int xSize = poly.getXSize();
		int ySize = poly.getYSize();
		
		/**
		 * Start creating the equations for the system:
		 *
		 * For Loop Description:
		 * -	First term is the constant value without any x or y.
		 * -	Loop adding all the terms containing x added in increasing order.
		 * -	Loop adding all the terms containing y added in increasing order.
		 * 
		 * Outer loop creates the equations.
		 * Inner loop loops through the equation term wise.
		 * 
		 * The loop also creates the "b-vector" which contains all the values that
		 * the polynomial are going to equal, i.e. this b vector: Ax = b. 
		 */
		//b vector
		double[][] vectorData = new double[size][1];
		//coefficient matrix A in Ax = b
		double[][] matrixData = new double[size][size];
		//Adding values to matrix and vector
		for (int eqIndex = 0; eqIndex < size; eqIndex++) {
			Vertex point = points.get(eqIndex);
			vectorData[eqIndex][0] = point.getZ();
			matrixData[eqIndex][0] = 1; //equal to 1 because of constant
			//Add x terms to matrix
			for (int termIndex = 0; termIndex < xSize; termIndex++) {
				double xPos = point.getX();
				double val = Math.pow(xPos, termIndex+1);
				matrixData[eqIndex][termIndex+1] = val;
			}
			//add y terms to matrix
			for (int termIndex = xSize+1; termIndex < size; termIndex++) {
				double yPos = point.getY();
				double val = Math.pow(yPos, termIndex - xSize);
				matrixData[eqIndex][termIndex] = val;
			}
		}
		//creating b vector
		SimpleMatrix m = new SimpleMatrix(matrixData);
		SimpleMatrix b = new SimpleMatrix(vectorData);
		SimpleMatrix inverse = m.invert();
		SimpleMatrix result = inverse.mult(b);
		
		//extract coefficients
		double[] pCoefficients = new double[size];
		for(int i = 0; i < result.getNumElements(); i++)
			pCoefficients[i] = result.get(i); 

		//assigning coefficients to the polynomial
		poly.setConstant(pCoefficients[0]);
		
		//add x coefficients to polynomial
		for (int i = 0; i < xSize; i++)
			poly.setNthCoefficient(i, true, pCoefficients[i+1]);
		
		//add y coefficients to polynomial
		for (int i = 0; i < ySize; i++) {
			int index = xSize + 1;
			poly.setNthCoefficient(i, false, pCoefficients[index]);
		}
		
		return poly;
	}
}