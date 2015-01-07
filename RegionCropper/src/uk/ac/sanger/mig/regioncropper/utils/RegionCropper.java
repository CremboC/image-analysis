/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Region Cropper.
 * 
 * Region Cropper is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.regioncropper.utils;

import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Main class that contains all the logic to actually crop out the desired area
 * 
 * @author pi1 pi1@sanger.ac.uk
 * @param <T>
 * 
 */
public class RegionCropper<T extends RealType<T> & NativeType<T>> {

	private final static int COL = 0, ROW = 1;
	private final static int UPPER = 0, LOWER = 1, LEFT = 2, RIGHT = 3;

	private final int[] boundaries;

	/**
	 * Main class that contains all the logic to actually crop out the desired area
	 * @param boundaries boundaries which will of the wanted area
	 */
	public RegionCropper(int[] boundaries) {
		this.boundaries = boundaries;
	}

	/**
	 * Crops the given image, must pass its type as well
	 * @param image
	 * @param type
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ImgPlus<T> crop(ImgPlus<T> image)
			throws InstantiationException, IllegalAccessException {
		// generate the dimensions for the new image
		int[] dimensions = { 
				boundaries[RIGHT] - boundaries[LEFT], // cols
				boundaries[LOWER] - boundaries[UPPER] // rows
		};

		// create a new image in the required size
		ImgPlus<T> cropped = new ImgPlus<T>(new ArrayImgFactory<T>().create(
				dimensions, image.firstElement().duplicateTypeOnSameNativeImg()));
		
		// get accessors both for the original image and the cropped
		final RandomAccess<T> ra = image.randomAccess();
		final RandomAccess<T> croppedRa = cropped.randomAccess();
		
		// move to the start of the cropping area on the original picture
		ra.setPosition(boundaries[LEFT], COL);
		ra.setPosition(boundaries[UPPER], ROW);
		
		croppedRa.setPosition(0, COL);
		croppedRa.setPosition(0, ROW);
		
		// loop over the original image and copy to the cropped one
		while (ra.getIntPosition(COL) != boundaries[RIGHT]) {
			
			ra.setPosition(boundaries[UPPER], ROW);
			croppedRa.setPosition(0, ROW);
			
			while (ra.getIntPosition(ROW) != boundaries[LOWER]) {
				
				double val = ra.get().getRealDouble();
				
				croppedRa.get().setReal(val);
				
				ra.fwd(ROW);
				croppedRa.fwd(ROW);
			}
			
			ra.fwd(COL);
			croppedRa.fwd(COL);
		}

		return cropped;
	}
}
