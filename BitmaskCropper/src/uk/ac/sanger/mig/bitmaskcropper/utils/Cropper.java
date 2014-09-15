package uk.ac.sanger.mig.bitmaskcropper.utils;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import uk.ac.sanger.mig.analysis.nodetools.Image;

public class Cropper<T extends RealType<T> & NativeType<T>> {

	private final ImgPlus<T> image;
	private final ImgPlus<BitType> mask;

	/**
	 * Container class which handles all of the cropping and other logic for
	 * this node
	 *
	 * @param image
	 * @param mask
	 * @param output
	 */
	public Cropper(ImgPlus<T> image, ImgPlus<BitType> mask) {
		this.image = image.copy();
		this.mask = mask;

		if ((image.dimension(Image.COL) != mask.dimension(Image.COL))
				|| (image.dimension(Image.ROW) != mask.dimension(Image.ROW))) {
			throw new IllegalArgumentException(
					"Mask and Image must be of same size.");
		}
	}

	/**
	 * Crops the image using the mask
	 *
	 * @return
	 */
	public ImgPlus<T> process() {

		return image;
	}

	/**
	 * Converts a Bit Type bitmask containing 0s and 1s into an Unsigned Byte
	 * Type containing -1s instead of 0 but retaining the 1s
	 *
	 * @return
	 */
	private ImgPlus<UnsignedByteType> convertMask() {
		final ImgPlus<UnsignedByteType> convertedMask = new ImgPlus<UnsignedByteType>(
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

			final int val = inCursor.get().getInteger();

			convertedAccess.get().set(val == 0 ? -1 : val);
		}

		return convertedMask;
	}

}
