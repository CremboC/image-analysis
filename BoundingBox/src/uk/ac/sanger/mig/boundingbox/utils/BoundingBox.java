package uk.ac.sanger.mig.boundingbox.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.RealType;

/**
 * Wrapper class for the main logic for finding and calculating the Bounding
 * Box.
 * 
 * @author Paulius pi1@sanger.ac.uk
 * @param <T>
 * 
 */
public class BoundingBox<T extends RealType<T>> {

	private final static int COL = 0;
	private final static int ROW = 1;

	private final static int UPPER = 0, LOWER = 1, LEFT = 2, RIGHT = 3;

	private ImgPlus<T> image;
	private final int centroidX, centroidY;

	private final int[] rowTs, colTs;


	/**
	 * 
	 * @param ip BitType image of which bounding box will be calculated
	 * @param centroidX
	 * @param centroidY
	 * @param rowTs row (upper/lower) thresholds
	 * @param colTs column (left/right) thresholds
	 */
	public BoundingBox(ImgPlus<T> ip, int centroidX, int centroidY,
			int[] rowTs, int[] colTs) {
		this.image = ip.copy();

		this.centroidX = centroidX;
		this.centroidY = centroidY;

		this.rowTs = rowTs;
		this.colTs = colTs;
	}

	/**
	 * Finds the bounding box
	 * 
	 * @return
	 */
	public int[] find() {
		int[] boundaries = new int[4];

		int cols = (int) image.dimension(COL);
		int rows = (int) image.dimension(ROW);

		int[] colValues = new int[cols];
		int[] rowValues = new int[rows];

		final RandomAccess<T> ra = image.randomAccess();

		// move cursor to central column and top row
		ra.setPosition(0, COL);
		ra.setPosition(0, ROW);

		while (ra.getIntPosition(COL) != cols) {

			ra.setPosition(0, ROW);

			while (ra.getIntPosition(ROW) != rows) {

				int val = (int) ra.get().getRealDouble();

				colValues[ra.getIntPosition(COL)] += val;
				rowValues[ra.getIntPosition(ROW)] += val;

				ra.fwd(ROW);
			}

			ra.fwd(COL);
		}

		boundaries[UPPER] = findDecrementing(centroidY, rowValues, rowTs[UPPER]);
		boundaries[LOWER] = findIncreamenting(centroidY, rowValues, rowTs[LOWER]);
		boundaries[LEFT] = findDecrementing(centroidX, colValues, colTs[0]);
		boundaries[RIGHT] = findIncreamenting(centroidX, colValues, colTs[1]);

		drawLines(boundaries[UPPER], boundaries[LOWER], boundaries[LEFT],
				boundaries[RIGHT], cols, rows);

		return boundaries;
	}

	/**
	 * Helper to find the boundary given the start coordinate, values array and
	 * the threshold at which to stop. Goes downwards (or to the right).
	 * 
	 * @param start
	 * @param values
	 * @param threshold
	 * @return
	 */
	private int findIncreamenting(int start, int[] values, int threshold) {
		int boundary = 0;
		for (int i = start; i < values.length; i++) {
			if (values[i] <= threshold) {
				boundary = i;
				break;
			}
		}

		return boundary;
	}

	/**
	 * Helper to find the boundary given the start coordinate, values array and
	 * the threshold at which to stop. Goes upwards (or to the left).
	 * 
	 * @param start
	 * @param values
	 * @param threshold
	 * @return
	 */
	private int findDecrementing(int start, int[] values, int threshold) {
		int boundary = 0;
		for (int i = start; i > 0; i--) {
			if (values[i] <= threshold) {
				boundary = i;
				break;
			}
		}

		return boundary;
	}

	/**
	 * Draws lines to show the bounding box
	 * 
	 * @param topRow
	 * @param bottomRow
	 * @param leftCol
	 * @param rightCol
	 * @param cols
	 * @param rows
	 */
	private void drawLines(int topRow, int bottomRow, int leftCol,
			int rightCol, int cols, int rows) {
		final RandomAccess<T> ra = image.randomAccess();

		ra.setPosition(0, COL);
		ra.setPosition(topRow, ROW);
		while (ra.getIntPosition(COL) != cols) {
			if (ra.get().getRealDouble() > 128) {
				ra.get().setReal(0);
			} else {
				ra.get().setReal(255);
			}
			ra.fwd(COL);
		}

		ra.setPosition(0, COL);
		ra.setPosition(bottomRow, ROW);
		while (ra.getIntPosition(COL) != cols) {
			if (ra.get().getRealDouble() > 128) {
				ra.get().setReal(0);
			} else {
				ra.get().setReal(255);
			}
			ra.fwd(COL);
		}

		ra.setPosition(leftCol, COL);
		ra.setPosition(0, ROW);
		while (ra.getIntPosition(ROW) != rows) {
			if (ra.get().getRealDouble() > 128) {
				ra.get().setReal(0);
			} else {
				ra.get().setReal(255);
			}
			ra.fwd(ROW);
		}

		ra.setPosition(rightCol, COL);
		ra.setPosition(0, ROW);
		while (ra.getIntPosition(ROW) != rows) {
			if (ra.get().getRealDouble() > 128) {
				ra.get().setReal(0);
			} else {
				ra.get().setReal(255);
			}
			ra.fwd(ROW);
		}
	}

	public ImgPlus<T> image() {
		return image;
	}
}
