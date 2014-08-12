package uk.ac.sanger.mig.dicom.obj;

/**
 * Container for a point, x y coordinates
 * 
 * @author pi1
 */
public class Point {
	
	private final double x;
	private final double y;
	
	// for convience
	
	/** Synonym for x */
	private final double col;
	
	/** Synonym for y */
	private final double row;
	
	public Point(double x, double y) {
		this.col = this.x = x;
		this.row = this.y = y;
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	/** Synonym for X */
	public double col() {
		return x();
	}
	
	/** Synonym for Y */
	public double row() {
		return y();
	}
	
	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}
	
	@Override
	public boolean equals(Object ob) {
		if (!(ob instanceof Point)) {
			return false;
		}
		
		Point p = (Point) ob;
		
		if (p.x == this.x && p.y == this.y) {
			return true;
		}
		
		return false;
	}
}