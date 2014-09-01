package uk.ac.sanger.mig.boundingbox.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.knime.knip.core.util.Triple;

/**
 * Wrapper class for the main logic for finding and calculating the Bounding
 * Box.
 * 
 * @author Paulius pi1@sanger.ac.uk
 * 
 */
public class BoundingBox {

	private final static int COL = 0;
	private final static int ROW = 1;

	private final static int TOPLEFT = 0, TOPRIGHT = 1, BOTTOMLEFT = 2,
			BOTTOMRIGHT = 3;

	private ImgPlus<BitType> image;
	private final int centroidX, centroidY;
	
	public ImgPlus<BitType> image() {
		return image;
	}

	/**
	 * 
	 * @param ip
	 *            BitType image of which bounding box will be calculated
	 */
	public BoundingBox(ImgPlus<BitType> ip, int centroidX, int centroidY,
			int rowThreshold, int columnThreshold) {
		this.image = ip.copy();
		this.centroidX = centroidX;
		this.centroidY = centroidY;
	}

	/**
	 * Finds the bounding box
	 * 
	 * @return a Triple whose <b>first</b> is the top-left coordinate,
	 *         <b>second</b> is width and <b>third</b> is height
	 */
	public Triple<Vector2D, Integer, Integer> find() {
		Vector2D[] coords;
		Vector2D topleft = new Vector2D(0, 0);
		int width = 0, height = 0;

		int cols = (int) image.dimension(COL);
		int rows = (int) image.dimension(ROW);

		int[] colValues = new int[cols];
		int[] rowValues = new int[rows];

		final RandomAccess<BitType> ra = image.randomAccess();

		// move cursor to central column and top row
		ra.setPosition(0, COL);
		ra.setPosition(0, ROW);

		while (ra.getIntPosition(COL) != cols) {
			ra.setPosition(0, ROW);
			while (ra.getIntPosition(ROW) != rows) {
				int val = ra.get().getInteger();

				colValues[ra.getIntPosition(COL)] += val;
				rowValues[ra.getIntPosition(ROW)] += val;

				ra.fwd(ROW);
			}
			ra.fwd(COL);
		}
		
		// find top and bottom row according to threshold
		int topRow = 0, bottomRow = 0;
		for (int i = 0; i < rowValues.length; i++) {
			if (i < centroidY) {
				if (rowValues[i] >= 496 && topRow == 0) {
					topRow = i;
				}
			} else {
				if (rowValues[i] <= 200) {
					bottomRow = i;
					break;
				}
			}
		}
		
		int leftCol = 0, rightCol = 0;
		for (int i = centroidX; i < colValues.length; i++) {
			if (colValues[i] <= 591) {
				rightCol = i;
				break;
			}
		}
		
		for (int i = centroidX; i < colValues.length; i--) {
			if (colValues[i] <= 591) {
				leftCol = i;
				break;
			}
		}
		
//		leftCol = rightCol - centroidX;
		
		drawLines(topRow, bottomRow, leftCol, rightCol, cols, rows);
		
		System.out.println(topRow + ".." + bottomRow);
		
		
		
//		coords[TOPLEFT]

		return new Triple<Vector2D, Integer, Integer>(topleft, width, height);
	}
	
	private void drawLines(int topRow, int bottomRow, int leftCol, int rightCol, int cols, int rows) {
		final RandomAccess<BitType> ra = image.randomAccess();
		
		ra.setPosition(0, COL);
		ra.setPosition(topRow, ROW);
		while (ra.getIntPosition(COL) != cols) {
			if (ra.get().getInteger() == 0) {
				ra.get().set(true);
			} else {
				ra.get().set(false);
			}
			ra.fwd(COL);
		}
		
		ra.setPosition(0, COL);
		ra.setPosition(bottomRow, ROW);
		while (ra.getIntPosition(COL) != cols) {
			if (ra.get().getInteger() == 0) {
				ra.get().set(true);
			} else {
				ra.get().set(false);
			}
			ra.fwd(COL);
		}
		
		ra.setPosition(leftCol, COL);
		ra.setPosition(0, ROW);
		while (ra.getIntPosition(ROW) != rows) {
			if (ra.get().getInteger() == 0) {
				ra.get().set(true);
			} else {
				ra.get().set(false);
			}
			ra.fwd(ROW);
		}
		
		ra.setPosition(rightCol, COL);
		ra.setPosition(0, ROW);
		while (ra.getIntPosition(ROW) != rows) {
			if (ra.get().getInteger() == 0) {
				ra.get().set(true);
			} else {
				ra.get().set(false);
			}
			ra.fwd(ROW);
		}
	}
}
