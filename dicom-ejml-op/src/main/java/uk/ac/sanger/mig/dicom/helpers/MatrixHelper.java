package uk.ac.sanger.mig.dicom.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.enums.ValueType;

import uk.ac.sanger.mig.dicom.obj.Point;

public class MatrixHelper {

	public static double area(Matrix m) {
		Matrix areaMatrix = m.sum(Ret.NEW, Matrix.ALL, false);
		double area = areaMatrix.getValueSum();

		return area;
	}

	public static double meanOf(Matrix m, Matrix mult, double area) {
		Matrix meanMatrix = m.times(mult).sum(Ret.NEW, Matrix.ALL, false);
		double mean = (meanMatrix.getValueSum() / area);

		return mean;
	}

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
	 * 
	 * @param m
	 *            matrix to work on, does not override
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
	 * 
	 * @param m
	 *            matrix to work on, does not override
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
	 * Threshold a matrix. Auto-converts into ValueType....
	 * 
	 * @param m
	 *            matrix to convert, will not modify
	 * @param threshold
	 *            any pixel with a value above this threshold will be 1, below - 0
	 * @return a new thresholded matrix
	 */
	public static Matrix threshold(Matrix m, int threshold) {
		Matrix mani = m.clone(); // create a clone to not modify the original matrix

		double max = mani.max(Ret.NEW, Matrix.ALL).getValueSum() + Short.MAX_VALUE;
		double min = mani.min(Ret.NEW, Matrix.ALL).getValueSum();

		Map<Integer, Integer> normalized = MathHelper.normalize(min, max);
		
		int normalizedThreshold = normalized.get(threshold);
		boolean value;
		long rows = mani.getRowCount(), cols = mani.getColumnCount();
		
		// loop over the matrix and change the value according to the threshold and pixel value
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				value = true;

				if (mani.getAsInt(row, col) < normalizedThreshold) {
					value = false;
				}

				mani.setAsBoolean(value, row, col);
			}
		}

		return mani.convert(ValueType.DOUBLE);
	}

	/**
	 * Implementation of Bresenham's Line Algorithm as described in
	 * http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	 * <p>
	 * Draws in a matrix.
	 * 
	 * @param m
	 *            the original matrix to draw on, will not override
	 * @param a
	 *            start point
	 * @param b
	 *            end point
	 * 
	 * @return original matrix with line drawn on it
	 */
	public static Matrix bresenham(Matrix m, Point a, Point b) {
		Matrix mani = m.clone();

		int dx = (int) (b.x() - a.x());
		int dy = (int) (b.y() - a.y());

		double err = 0;
		double derr = Math.abs((double) dy / dx);

		int y = (int) a.y();

		for (int i = (int) a.x(); i < (int) b.x(); i++) {
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
	 * @param m
	 *            Original matrix
	 * @param r
	 *            Rotation matrix
	 * @param store
	 *            Matrix where changes will be stored
	 * @return
	 */
	public static Matrix transform(Matrix m, Matrix r, Matrix store, Point centroid) {
		int value;
		Matrix newCoords;

		// loop over the original image matrix to rotate and copy over every pixel
		for (int row = 0; row < m.getRowCount(); row++) {
			for (int col = 0; col < m.getColumnCount(); col++) {

				// create a temporary matrix for the new coords
				newCoords = Matrix.factory.zeros(3, 1);
				newCoords.setAsInt(col, 0, 0);
				newCoords.setAsInt(row, 1, 0);
				newCoords.setAsInt(1, 2, 0);

				// multiply by the rotation matrix to get new coordinates
				newCoords = r.mtimes(Ret.NEW, true, newCoords);

				// get the original value of the pixel
				value = m.getAsInt(row, col);

				int newCol = newCoords.getAsInt(0, 0) + (int) centroid.x();
				int newRow = newCoords.getAsInt(1, 0) + (int) centroid.y();

				// if result is negative, skip it. Makes it easier to debug.
				if (newRow < 0 || newCol < 0) {
					continue;
				}

				try {
					// set the corresponding pixel in the matrix which stores the rotated image
					store.setAsInt(value, newRow, newCol);
				} catch (Exception e) {
					System.out.println("Writing value : " + value);
					System.out.println("Row and Col   : \n" + col + " .. " + row);
					System.out.println("Coords after R: \n" + newCoords);
					System.out.println("Final new coords:" + newRow + " " + (newCol + (int) centroid.x()));
					e.printStackTrace();
					System.exit(-1);
				}

			}
		}

		return store;
	}

	/**
	 * Removes all rows and columns which do not have a single "true" value in them
	 * TODO: rewrite to accomodate for colour images, may not work with them.
	 * 
	 * @param m
	 *            matrix to work on, MAY OVERRIDE
	 * @return
	 */
	public static Matrix removeFalseRowsAndColumns(Matrix m, long size) {
		// represent if the row or col have anything in it
		boolean[] rowsArray = new boolean[(int) Math.round(size)];
		boolean[] colsArray = new boolean[(int) Math.round(size)];

		// finds all the rows and columns which have at least one pixel
		// which is true
		for (int row = 0; row < m.getRowCount(); row++) {
			for (int col = 0; col < m.getColumnCount(); col++) {
				if (m.getAsBoolean(row, col)) {
					colsArray[col] = true;
					rowsArray[row] = true;
					continue;
				}
			}
		}

		// keep track which row and column is the first and last one to
		// have a pixel is to true
		int firstTrueRow = 0, lastTrueRow = 0, firstTrueCol = 0, lastTrueCol = 0;

		for (int i = 1; i < rowsArray.length; i++) {
			if (rowsArray[i] == true && firstTrueRow == 0) {
				firstTrueRow = i;
			}

			if (rowsArray[i] == false && rowsArray[i - 1] == true) {
				lastTrueRow = i - 1;
				break;
			}
		}

		for (int i = 1; i < colsArray.length; i++) {
			if (colsArray[i] == true && firstTrueCol == 0) {
				firstTrueCol = i;
			}

			if (colsArray[i] == true) {
				lastTrueCol = i;
			}
		}

		// stores which columns and rows to remove from the original matrix
		List<Number> colsToRemove = new ArrayList<Number>();
		List<Number> rowsToRemove = new ArrayList<Number>();

		// from 0 to the first column, padding of 10
		for (long i = 0; i < firstTrueCol - 10; i++) {
			colsToRemove.add(i);
		}

		// from last true col + padding of 5 and up to the size
		for (long i = lastTrueCol + 5; i < size; i++) {
			colsToRemove.add(i);
		}

		for (long i = 0; i < firstTrueRow - 10; i++) {
			rowsToRemove.add(i);
		}

		for (long i = lastTrueRow + 5; i < size; i++) {
			rowsToRemove.add(i);
		}

		return m.deleteColumns(Ret.NEW, colsToRemove).deleteRows(Ret.NEW, rowsToRemove);
	}
}
