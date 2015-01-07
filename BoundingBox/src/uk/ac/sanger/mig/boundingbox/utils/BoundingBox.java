/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Bounding Box.
 * 
 * Bounding Box is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option ) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.sanger.mig.boundingbox.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.RealType;
import uk.ac.sanger.mig.analysis.nodetools.Image;
import uk.ac.sanger.mig.analysis.nodetools.enums.ReturnType;

/**
 * Wrapper class for the main logic for finding and calculating the Bounding
 * Box.
 * 
 * @author pi1 pi1@sanger.ac.uk
 */
public class BoundingBox<T extends RealType<T>> {

	private final static int UPPER = 0, LOWER = 1, LEFT = 2, RIGHT = 3;

	private ImgPlus<T> image;

	private final int[] rowTs, colTs;
	
	private final ReturnType retType;

	/**
	 * 
	 * @param ip
	 *            BitType image of which bounding box will be calculated
	 * @param centroidX
	 * @param centroidY
	 * @param rowTs
	 *            row (upper/lower) thresholds
	 * @param colTs
	 *            column (left/right) thresholds
	 */
	public BoundingBox(int[] rowTs, int[] colTs, ReturnType retType) {
		this.rowTs = rowTs;
		this.colTs = colTs;
		
		this.retType = retType;
	}

	/**
	 * Finds the bounding box
	 * @param centroidY
	 * @param centroidX
	 * 
	 * @return
	 */
	public int[] find(ImgPlus<T> image, int centroidX, int centroidY) {
		int[] boundaries = new int[4];

		int cols = (int) image.dimension(Image.COL);
		int rows = (int) image.dimension(Image.ROW);

		// arrays that will contain the sum of pixel values of all columns and
		// rows
		int[] colValues = new int[cols];
		int[] rowValues = new int[rows];

		// accessor to the image, allows go in any direction
		final RandomAccess<T> ra = image.randomAccess();

		// move cursor to central column and top row
		ra.setPosition(0, Image.COL);
		ra.setPosition(0, Image.ROW);

		while (ra.getIntPosition(Image.COL) != cols) {

			ra.setPosition(0, Image.ROW);

			while (ra.getIntPosition(Image.ROW) != rows) {

				int val = (int) ra.get().getRealDouble();

				colValues[ra.getIntPosition(Image.COL)] += val;
				rowValues[ra.getIntPosition(Image.ROW)] += val;

				ra.fwd(Image.ROW);
			}

			ra.fwd(Image.COL);
		}

		// start at the centroid, go to the specified direction and find the
		// the first row/column which has a total pixel value equal or lower to
		// the one given
		boundaries[UPPER] = findDecrementing(centroidY, rowValues, rowTs[UPPER]);
		boundaries[LOWER] = findIncreamenting(centroidY, rowValues,
				rowTs[LOWER]);
		boundaries[LEFT] = findDecrementing(centroidX, colValues, colTs[0]);
		boundaries[RIGHT] = findIncreamenting(centroidX, colValues, colTs[1]);

		if (retType == ReturnType.MODIFIED) {
			drawLines(boundaries[UPPER], boundaries[LOWER], boundaries[LEFT],
					boundaries[RIGHT], cols, rows, image.copy());
		}


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
			int rightCol, int cols, int rows, ImgPlus<T> image) {
		final RandomAccess<T> ra = image.randomAccess();
		final Debug debug = new Debug(ra, 128, 0, 255);

		ra.setPosition(0, Image.COL);
		ra.setPosition(topRow, Image.ROW);
		debug.loopAndChange(Image.COL, cols);

		ra.setPosition(0, Image.COL);
		ra.setPosition(bottomRow, Image.ROW);
		debug.loopAndChange(Image.COL, cols);

		ra.setPosition(leftCol, Image.COL);
		ra.setPosition(0, Image.ROW);
		debug.loopAndChange(Image.ROW, rows);

		ra.setPosition(rightCol, Image.COL);
		ra.setPosition(0, Image.ROW);
		debug.loopAndChange(Image.ROW, rows);
		
		this.image = image;
	}

	/**
	 * @return The imaged passed to this class, most likely modified if called
	 *         after {@link #find()}
	 */
	public ImgPlus<T> image() {
		return image;
	}

	/**
	 * Helps drawing the lines to debug the bounding box
	 * 
	 * @author pi1
	 * 
	 */
	private class Debug {
		private final RandomAccess<T> ra;

		private final int threshold, min, max;

		/**
		 * Helps drawing the lines to debug the bounding box
		 * 
		 * @param ra
		 *            An accessor to the image
		 * @param threshold
		 *            limit at which pixels are changed
		 * @param min
		 *            new value of the pixel if it is below the threshold
		 * @param max
		 *            new value of the pixel if it is above the threshold
		 */
		public Debug(RandomAccess<T> ra, int threshold, int min, int max) {
			this.ra = ra;
			this.threshold = threshold;
			this.min = min;
			this.max = max;
		}

		/**
		 * Loops over the image and changes them according to <b>threshold</b>.
		 * If the pixel (double value) is higher than the <b>threshold</b>, the
		 * pixel will be set to <b>min</b>, otherwise sets it to <b>max</b>.
		 * 
		 * @param over
		 * @param when
		 */
		public void loopAndChange(int over, int when) {
			while (ra.getIntPosition(over) != when) {
				if (ra.get().getRealDouble() > threshold) {
					ra.get().setReal(min);
				} else {
					ra.get().setReal(max);
				}
				ra.fwd(over);
			}
		}
	}
}
