package uk.ac.sanger.mig.dicom.obj;

/**
 * Container for a point, x y coordinates
 * 
 * @author pi1
 */
public class Point {
	
	public double x;
	public double y;
	
	// for convience
	
	/** Synonym for x */
	public double col;
	
	/** Synonym for y */
	public double row;
	
	public Point(double x, double y) {
		this.col = this.x = x;
		this.row = this.y = y;
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