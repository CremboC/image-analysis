package uk.ac.sanger.mig.dicom.helpers;

import uk.ac.sanger.mig.dicom.obj.Point;

/**
 * Uses https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Vector2.java
 * @author pi1@sanger.ac.uk
 *
 */
public class VectorHelper {

	/**
	 * Angle between two vectors, degrees
	 * 
	 * @param original
	 * @param reference
	 * @return
	 */
	public static double angle(Point original, Point reference) {
		return Math.toDegrees(Math.atan2(cross(original, reference), dot(original, reference)));
	}

	public static double cross(Point v, Point v2) {
		return v.x() * v2.y() - v.y() * v2.x();
	}

	public static double dot(Point v, Point v2) {
		return v.x() * v2.x() + v.y() * v2.y();
	}

	/**
	 * Angle to x-axis
	 * 
	 * @return degrees
	 */
	public static double angleToHor(Point p) {
		double angle = Math.toDegrees(Math.atan2(p.y(), p.x()));
		if (angle < 0)
			angle += 360;
		return angle;
	}

	/**
	 * Normalises a vector
	 * @param p
	 * @return
	 */
	public static Point normalise(Point p) {
		double len = length(p);
		double x = p.x(), y = p.y();
		
		if (len != 0) {
			x /= len;
			y /= len;
		}
		
		return new Point(x, y);
	}

	/**
	 * Calculate length of vector
	 * @param p
	 * @return
	 */
	public static double length(Point p) {
		return Math.sqrt(p.x() * p.x() + p.y() * p.y());
	}

}
