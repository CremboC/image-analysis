/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Interval Threshold.
 * 
 * Interval Threshold is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.intervalthreshold.utils;

import net.imglib2.Cursor;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Thresholds an image using an interval configured by the user
 *  
 * @author dgm dgm@sanger.ac.uk
 * @author pi1 pi1@sanger.ac.uk
 * @author team110 team110dev@sanger.ac.uk
 *
 */
public class IntervalThreshold<T extends RealType<T> & NativeType<T>> {

	private final int lowerThreshold;
	private final int upperThreshold;
	private final int backgroundPixelValue;

	/**
	 * Thresholds an image using an interval
	 * 
	 * @param lowerThreshold lowest pixel value still to be included in the output image
	 * @param upperThreshold highest pixel value to be included in the output
	 * @param backgroundPixelValue new value of pixels which originally were out of the range
	 */
	public IntervalThreshold(int lowerThreshold, int upperThreshold,
			int backgroundPixelValue) {

		this.lowerThreshold = lowerThreshold;
		this.upperThreshold = upperThreshold;
		this.backgroundPixelValue = backgroundPixelValue;

	}

	/**
	 * Applies the thresholding according to the interval provided in the constructor
	 * 
	 * @param image
	 * @return A new, thresholded image
	 */
	public ImgPlus<T> process(ImgPlus<T> image) {

		ImgPlus<T> returnImage = image.copy();

		Cursor<T> cursor = image.localizingCursor();
		Cursor<T> outCursor = returnImage.localizingCursor();

		while (cursor.hasNext()) {
			cursor.next();
			outCursor.next();

			int pixelVal = (int) cursor.get().getRealDouble();

			if (pixelVal < lowerThreshold || pixelVal > upperThreshold) {
				outCursor.get().setReal((float) backgroundPixelValue);
			}
		}

		return returnImage;

	}

}
