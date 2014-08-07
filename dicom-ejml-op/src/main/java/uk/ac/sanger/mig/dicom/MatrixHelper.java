package uk.ac.sanger.mig.dicom;

import java.util.Map;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.enums.ValueType;

public class MatrixHelper {
	
	/**
	 * Equivalent to matlab x = [from : to]
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static Matrix singleColumnIncreasing(int from, long to) {
		Matrix xCols = Matrix.factory.zeros(from, to);

		for (int i = 0; i < to; i++) {
			xCols.setAsInt(i + 1, i, 0);
		}

		return xCols;
	}
	
	/**
	 * Convert an image from its default type to a limited range
	 * <p>
	 * Uses the default 0-255 map
	 * @param m matrix to work on, does not override
	 * @return
	 */
	public static Matrix convertImageByMap(Matrix m) {
		double max = m.max(Ret.NEW, Matrix.ALL).getValueSum();
		double min = m.min(Ret.NEW, Matrix.ALL).getValueSum();

		Map<Integer, Integer> map = MapHelper.flipMap(MathHelper.normalize(min, max));
		
		return convertImageByMap(m, map);
	}
	
	/**
	 * Convert an image from its default type to a limited range using a specified map
	 * @param m matrix to work on, does not override
	 * @param map
	 * @return
	 */
	public static Matrix convertImageByMap(Matrix m, Map<Integer, Integer> map) {
		Matrix mani = m.clone();
		
		// loop over the matrix and change the value
		for (int i = 0; i < mani.getRowCount(); i++) {
			for (int j = 0; j < mani.getColumnCount(); j++) {
				
				int old = mani.getAsInt(i, j);
				int n = map.get(old);

				mani.setAsInt(n, i, j);
			}
		}
		
		return mani;
	}
	
	/**
	 * Threshold a matrix. Auto-converts into ValueType.SHORT
	 * 
	 * @param m
	 *            matrix to convert, will not modify
	 * @param threshold
	 *            any pixel with a value above this threshold will be 1, below - 0
	 * @return a new thresholded matrix
	 */
	public static Matrix threshold(Matrix m, int threshold) {
		Matrix mani = m.clone(); // create a clone to not modify the original matrix

		mani = mani.convert(ValueType.SHORT);

		int max = (int) mani.max(Ret.NEW, Matrix.ALL).getValueSum();
		int min = (int) mani.min(Ret.NEW, Matrix.ALL).getValueSum();

		Map<Integer, Integer> normalized = MathHelper.normalize(min, max);

		// loop over the matrix and change the value according to the threshold and pixel value
		for (int i = 0; i < mani.getRowCount(); i++) {
			for (int j = 0; j < mani.getColumnCount(); j++) {
				boolean value = true;

				if (mani.getAsShort(i, j) > normalized.get(threshold)) {
					value = false;
				}

				mani.setAsBoolean(value, i, j);
			}
		}

		return mani;
	}

	/**
	 * Implementation of Bresenham's Line Algorithm as described in 
	 * http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	 * <p>
	 * Draws in a matrix.
	 * 
	 * @param m the original matrix to draw on, will not override
	 * @param a start point
	 * @param b end point
	 * 
	 * @return original matrix with line drawn on it
	 */
	public static Matrix bresenham(Matrix m, Point a, Point b) {
		Matrix mani = m.clone();
		
		int dx = (int) (b.x - a.x);
		int dy = (int) (b.y - a.y);
		
		double err = 0;
		double derr =  Math.abs((double) dy / dx);
		
		int y = (int) a.y;
		
		for (int i = (int) a.x; i < (int) b.x; i++) {
			// absolute values to ignore negative Point as they don't exist in this matrix
			mani.setAsBoolean(false, Math.abs(i), Math.abs(y));
			
			err = err + derr;
			if (err >= 0.5d) {
				y++;
				err--;
			}
		}
		
		return mani;
	}
	
	/**
	 * Applies a transformation matrix over another matrix and stores it in the provided matrix
	 * 
	 * @param m Original matrix
	 * @param r Rotation matrix
	 * @param store Matrix where changes will be stored
	 * @return
	 */
	public static Matrix transform(Matrix m, Matrix r, Matrix store) {
		boolean value;
		Matrix newCoords;
		for (int row = 0; row < m.getRowCount(); row++) {
			for (int col = 0; col < m.getColumnCount(); col++) {

				newCoords = Matrix.factory.zeros(1, 2);
				newCoords.setAsInt(col, 0, 0);
				newCoords.setAsInt(row, 0, 1);
				newCoords.times(Ret.ORIG, true, r);
				
				// if (newCoords.getAsInt(0, 0) <= 0.0000000000000001 || newCoords.getAsInt(0, 1) <= 0.0000000000001) {
				// System.out.println("why?");
				// System.out.println(newCoords);
				// System.out.println(row + " " + col);
				// }
				
				value = m.getAsBoolean(row, col);
				
				int newRow = newCoords.getAsInt(0, 1);
				int newCol = newCoords.getAsInt(0, 0);
						
				store.setAsBoolean(value, 
						(newRow < 0 ) ? -newRow : newRow, 
						(newCol < 0 ) ? -newCol : newCol);
			}
		}
		
		return store;
	}
}
