package uk.ac.sanger.mig.dicom;

import java.util.HashMap;
import java.util.Map;

public class MathHelper {
	/**
	 * Converts polar coordinates to cartesian
	 * 
	 * @param rho
	 * @param theta must be radians
	 * @return
	 */
	public static Point pol2cart(double theta, double rho) {
		double x = rho * Math.cos(theta);
		double y = rho * Math.sin(theta);
		
		return new Point(x, y);
	}
	
	/**
	 * Angle to horizontal line in DEGREES
	 * @return DEGREES
	 */
	public static double angleToHor(Point p) {
		double angle =  Math.toDegrees(Math.atan2(p.y, p.x));
		if (angle < 0) angle += 360;
		return  angle; 
	}
	
	/**
	 * Get a normalized range (0 - 1.0) * normalizationFactor
	 * 
	 * @param min
	 * @param max
	 * @return HashMap in format of [normalised value, original value]
	 */
	public static Map<Integer, Integer> normalize(double min, double max) {
		return normalize(min, max, DicomEJML.normalizationFactor);
	}
	
	/**
	 * Get a normalized range (0 - 1.0) * factor
	 * 
	 * @param min
	 * @param max
	 * @param factor
	 * @return HashMap in format of [normalised value, original value]
	 */
	public static Map<Integer, Integer> normalize(double min, double max, double factor) {
		
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();

		int[] x = new int[(int) (max - min)];

		int j = 0;
		for (int i = (int) min; i < max; i++) {
			x[j++] = i;
		}

		for (int i = 0; i < x.length; i++) {
			// formula gets a normalised value 0.0 to 1.0, factor multiplies
			// it to extend the range then convert it to an int so retrieving
			// mapped values would be simpler (this reduces the resolution)
			m.put((int) ((x[i] - min) / (max - min) * factor), x[i]);
		}

		return m;
	}
}
