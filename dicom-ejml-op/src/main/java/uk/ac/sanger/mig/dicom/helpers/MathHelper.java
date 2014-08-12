package uk.ac.sanger.mig.dicom.helpers;

import java.util.HashMap;
import java.util.Map;

import uk.ac.sanger.mig.dicom.DicomEJML;
import uk.ac.sanger.mig.dicom.obj.Point;

public class MathHelper {
	
	public static Point yFromLine(double x, float[] mc) {
		return new Point(x, mc[0] * x + mc[1]);
	}
	
	public static float[] lineEquation(Point p1, Point p2) {
		float[] mc = new float[2];
		double c, m;
		
		m = (p1.y() - p2.y()) / (p1.x() - p2.x());
		c = p2.x() - p2.y() * m;
		
		mc[0] = (float) m;
		mc[1] = (float) c;
		
		return mc;
	}
	
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
