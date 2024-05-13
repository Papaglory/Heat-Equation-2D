package com;

/**
 * @author Marius
 * A vertex is a triple tuple (x,y,z) containing double values.
 */
public class Vertex {
	
	private double x;
	private double y;
	private double z;
	
	public Vertex( double x, double y, double z) 	{ this.x = x; this.y = y; this.z = z; }
	public Vertex() 								{ this.x = 0; this.y = 0; this.z = 0; }
	
	public double getX() 				{ return x; }
	public double getY() 				{ return y; }
	public double getZ() 				{ return z; }
	
	public void  setX(double x) 		{ this.x = x; }
	public void  setY(double y) 		{ this.y = y; }
	public void  setZ(double z) 		{ this.z = z; }
}