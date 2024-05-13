package com;

/**
 * @author Marius
 * 
 * The inner workings of this mesh uses matrix notation i.e. (y,x) instead of (x,y).
 *
 */
public class Mesh {
	
	private final double[][] data;
	
	private final int xSize;
	private final int ySize;
	
	public Mesh(int xSize, int ySize) {
		data = new double[ySize][xSize];
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	public Mesh(double[][] data) {
		this.data = data;
		ySize = data.length;
		xSize = data[0].length;
		
	}
	
	public double getElement(int i, int j) {
		return data[i][j];
	}
	
	public void setElement(int i, int j, double val) {
		data[i][j] = val;
	}
	
	public int getXSize() { return xSize; }
	public int getYSize() { return ySize; }
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				result += " " + data[i][j] + " ";
			}
			result += "\n";
		}
		return result;
	}
}