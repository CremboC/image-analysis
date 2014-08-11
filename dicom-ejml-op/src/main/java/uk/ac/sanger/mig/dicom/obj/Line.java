package uk.ac.sanger.mig.dicom.obj;


/**
 * A line is two points - from and to
 * 
 * @author pi1@sanger.ac.uk
 *
 */
public class Line {
	
	private Point from;
	private Point to;
	
	public Line(Point from, Point to) {
		this.from = from;
		this.to = to;
	}

	public Point from() {
		return this.from;
	}

	public Point to() {
		return this.to;
	}
	
	@Override
	public boolean equals(Object ob) {
		if (!(ob instanceof Line)) {
			return false;
		}
		
		Line l = (Line) ob;
		
		if (from.equals(l.from()) && to.equals(l.to())) {
			return true;
		}
		
		return false;
	}

}
