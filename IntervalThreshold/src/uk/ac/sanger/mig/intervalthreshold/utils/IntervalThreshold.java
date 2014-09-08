package uk.ac.sanger.mig.intervalthreshold.utils;

import net.imglib2.Cursor;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Thresholds an image using an interval configured by the user
 *  
 * @author David dgm@sanger.ac.uk
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
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
