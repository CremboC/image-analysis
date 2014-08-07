package uk.ac.sanger.mig.dicom;

/**
 * Container for a point, x y coordinates
 * 
 * @author pi1
 */
public class Point {
	public double x;
	public double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}
}