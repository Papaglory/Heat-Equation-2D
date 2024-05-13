package com;

public class Polynomial {

	/**
	 * Contains coefficients of the polynomial corresponding to the x-axis.
	 * Index 0 corresponding to x of degree 1,
	 * 1 corresponding to x^2, 2 to x^3 and so on.
	 */
	private final double[] m_coefficientsX;
	
	/**
	 * Contains coefficients of the polynomial corresponding to the y-axis.
	 * Index 0 corresponding to y of degree 1,
	 * 1 corresponding to y^2, 2 to y^3 and so on.
	 */
	private final double[] m_coefficientsY;
	
	/**
	 * Total amount of terms in the polynomial including the constant.
	 */
	private final int m_size;
	
	/**
	 * The term having no x or y i.e. the first term. 
	 */
	private double m_constant;
	
	/**
	 * Keeps track of how many terms there are with x in it.
	 */
	private final int m_xSize;
	
	/**
	 * Keeps track of how many terms there are with y in it.
	 */
	private final int m_ySize;

	public Polynomial(double[] coefficientsX, double[] coefficientsY, double constant) {
		m_coefficientsX = coefficientsX;
		m_coefficientsY = coefficientsY;
		m_xSize = coefficientsX.length;
		m_ySize = coefficientsY.length;
		m_constant = constant;
		m_size = coefficientsX.length + coefficientsY.length - 1;
	}
	
	/**
	 * Divide out the size to the x terms and the y terms. x terms will be 
	 * filled out first. Note that the y term is added by 1. That is because
	 * m_coefficientsX[0] = m_coefficientsY[0].
	 *  
	 * @param size	Total amount of terms in the polynomial including both
	 * 				terms with x values and y values.
	 */
	public Polynomial(int size) {
		//dividing with whole numbers round down i.e. 5/2 = 2.
		if (size % 2 == 0) {
			m_coefficientsX = new double[size/2];
			m_coefficientsY = new double[size/2 - 1];
			m_xSize = size/2;
			m_ySize = size/2 - 1;
		}
		else {
			m_coefficientsX = new double[size/2];
			m_coefficientsY = new double[size/2];
			m_xSize = size/2;
			m_ySize = size/2;
		}
		m_size = size;
	}
	
	public double getNthCoefficient(int n, boolean getX) {
		if (getX)			return m_coefficientsX[n];
		else 				return m_coefficientsY[n];
	}
	
	public double getConstant() {
		return m_constant;
	}
	
	public void setConstant(double constant) {
		m_constant = constant;
	}
	
	public void setNthCoefficient(int n, boolean setX, double val) {
		if (setX)			m_coefficientsX[n] = val;
		else 				m_coefficientsY[n] = val;
	}

	public int getXSize() { return m_xSize; }
	public int getYSize() { return m_ySize; }
	
	public int getSize() { return m_size; }
	
	@Override
	public String toString() {
		if (m_size == 0)
			throw new IllegalArgumentException("This polynomial is empty!");
		
		String name = String.valueOf(m_constant);
		for (int i = 0; i < m_coefficientsX.length; i++) {
			if (m_coefficientsX[i] == 0) continue;
			String term = " + " + String.valueOf(m_coefficientsX[i]) + "x^" + String.valueOf(i+1);
			name += term;
		}
		for (int i = 0; i < m_ySize; i++) {
			if (m_coefficientsY[i] == 0) continue;
			String term = " + " + String.valueOf(m_coefficientsY[i]) + "y^" + String.valueOf(i+1);
			name += term;
		}
		return name;
	}	
}