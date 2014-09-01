package uk.ac.sanger.mig.boundingbox.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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
	
	private final int[] rowTs, colTs;

	/**
	 * 
	 * @param ip
	 *            BitType image of which bounding box will be calculated
	 * @param is 
	 */
	public BoundingBox(ImgPlus<BitType> ip, int centroidX, int centroidY, int[] rowTs, int[] colTs) {
		this.image = ip.copy();
		
		this.centroidX = centroidX;
		this.centroidY = centroidY;
		
		this.rowTs = rowTs;
		this.colTs = colTs;
	}

	/**
	 * Finds the bounding box
	 * 
	 * @return a Triple whose <b>first</b> is the top-left coordinate,
	 *         <b>second</b> is width and <b>third</b> is height
	 */
	public Vector2D[] find() {
		Vector2D[] coords = new Vector2D[4];

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
				if (rowValues[i] >= rowTs[0] && topRow == 0) {
					topRow = i;
				}
			} else {
				if (rowValues[i] <= rowTs[1]) {
					bottomRow = i;
					break;
				}
			}
		}
		
		int leftCol = 0, rightCol = 0;
		for (int i = centroidX; i < colValues.length; i++) {
			if (colValues[i] <= colTs[0]) {
				rightCol = i;	
				break;
			}
		}
		
		for (int i = centroidX; i < colValues.length; i--) {
			if (colValues[i] <= colTs[1]) {
				leftCol = i;
				break;
			}
		}
		
//		drawLines(topRow, bottomRow, leftCol, rightCol, cols, rows);
		
		coords[TOPLEFT] = new Vector2D(topRow, leftCol);
		coords[TOPRIGHT] = new Vector2D(topRow, rightCol);
		coords[BOTTOMLEFT] = new Vector2D(bottomRow, leftCol);
		coords[BOTTOMRIGHT] = new Vector2D(bottomRow, rightCol);

		return coords;
	}
	
	/**
	 * Draws lines to show the bounding box
	 * @param topRow
	 * @param bottomRow
	 * @param leftCol
	 * @param rightCol
	 * @param cols
	 * @param rows
	 */
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
	
	public ImgPlus<BitType> image() {
		return image;
	}
}
