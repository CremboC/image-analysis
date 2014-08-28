package uk.ac.sanger.mig.bitmaskcropper.utils;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Cropper {

	private final ImgPlus<UnsignedByteType> image;
	private final ImgPlus<BitType> mask;
	
	private final OutputHelper output;

	/**
	 * Container class which handles all of the cropping and other logic for this node
	 * @param image
	 * @param mask
	 * @param output
	 */
	public Cropper(ImgPlus<UnsignedByteType> image, ImgPlus<BitType> mask, OutputHelper output) {
		this.image = image;
		this.mask = mask;
		
		if (image.dimension(0) != mask.dimension(0) || image.dimension(1) != mask.dimension(1)) {
			throw new IllegalArgumentException("Mask and Image must be of same size.");
		}
		
		this.output = output;
	}

	/**
	 * Crops the image using the mask
	 * @return
	 */
	public ImgPlus<UnsignedByteType> crop() {
		ImgPlus<UnsignedByteType> output = new ImgPlus<UnsignedByteType>(
				new ArrayImgFactory<UnsignedByteType>().create(image,
						new UnsignedByteType()));

		return output;
	}

	/**
	 * Converts a Bit Type bitmask containing 0s and 1s into an Unsigned Byte
	 * Type containing -1s instead of 0 but retaining the 1s
	 * 
	 * @return
	 */
	private ImgPlus<UnsignedByteType> convertMask() {
		ImgPlus<UnsignedByteType> convertedMask = new ImgPlus<UnsignedByteType>(
				new ArrayImgFactory<UnsignedByteType>().create(mask,
						new UnsignedByteType()));

		// access the pixels of the output image
		final RandomAccess<UnsignedByteType> convertedAccess = convertedMask
				.randomAccess();

		// cursor over input image
		final Cursor<BitType> inCursor = mask.localizingCursor();

		// iterate over pixels of in input image
		while (inCursor.hasNext()) {
			inCursor.fwd();

			// set outaccess on position of incursor
			convertedAccess.setPosition(inCursor);

			int val = inCursor.get().getInteger();

			convertedAccess.get().set(val == 0 ? -1 : val);
		}

		return convertedMask;
	}

}
