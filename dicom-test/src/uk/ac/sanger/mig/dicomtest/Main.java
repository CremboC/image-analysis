package uk.ac.sanger.mig.dicomtest;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;

import java.io.File;
import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

public class Main {

	/**
	 * Compute the min and max for any {@link Iterable}, like an {@link Img}.
	 *
	 * The only functionality we need for that is to iterate. Therefore we need no {@link Cursor} that can localize itself, neither do we need a {@link RandomAccess}. So we simply use the
	 * most simple interface in the hierarchy.
	 *
	 * @param input
	 *            - the input that has to just be {@link Iterable}
	 * @param min
	 *            - the type that will have min
	 * @param max
	 *            - the type that will have max
	 */
	public <T extends Comparable<T> & Type<T>> void computeMinMax(final Iterable<T> input, final T min, final T max) {
		// create a cursor for the image (the order does not matter)
		final Iterator<T> iterator = input.iterator();

		// initialize min and max with the first image value
		T type = iterator.next();

		min.set(type);
		max.set(type);

		// loop over the rest of the data and determine min and max value
		while (iterator.hasNext()) {
			// we need this type more than once
			type = iterator.next();

			if (type.compareTo(min) < 0)
				min.set(type);

			if (type.compareTo(max) > 0)
				max.set(type);
		}
	}

	/**
	 * Compute the location of the minimal and maximal intensity for any IterableInterval,
	 * like an {@link Img}.
	 *
	 * The functionality we need is to iterate and retrieve the location. Therefore we need a
	 * Cursor that can localize itself.
	 * Note that we do not use a LocalizingCursor as localization just happens from time to time.
	 *
	 * @param input
	 *            - the input that has to just be {@link IterableInterval}
	 * @param minLocation
	 *            - the location for the minimal value
	 * @param maxLocation
	 *            - the location of the maximal value
	 */
	public <T extends Comparable<T> & Type<T>> void computeMinMaxLocation(final IterableInterval<T> input, final Point minLocation, final Point maxLocation) {
		// create a cursor for the image (the order does not matter)
		final Cursor<T> cursor = input.cursor();

		// initialize min and max with the first image value
		T type = cursor.next();
		T min = type.copy();
		T max = type.copy();

		// loop over the rest of the data and determine min and max value
		while (cursor.hasNext()) {
			// we need this type more than once
			type = cursor.next();

			if (type.compareTo(min) < 0) {
				min.set(type);
				minLocation.setPosition(cursor);
			}

			if (type.compareTo(max) > 0) {
				max.set(type);
				maxLocation.setPosition(cursor);
			}
		}
	}

	// within this method we define <T> to be a NumericType (depends on the type of ImagePlus)
	// you might want to define it as RealType if you know it cannot be an ImageJ RGB Color image
	public <T extends RealType<T> & NativeType<T>> Main() {
		// define the file to open
		File file = new File("xray.dcm");

		// open a file with ImageJ
		final ImagePlus imp = new Opener().openImage(file.getAbsolutePath());

		// wrap it into an ImgLib image (no copying)
		final Img<T> image = ImagePlusAdapter.wrap(imp);
		
		// create two empty variables
		T min = image.firstElement().createVariable();
		T max = image.firstElement().createVariable();

		// compute min and max of the Image
		computeMinMax(image, min, max);

		System.out.println("minimum Value (img): " + min);
		System.out.println("maximum Value (img): " + max);

		// create two location objects
		Point minLocation = new Point(image.numDimensions());
		Point maxLocation = new Point(image.numDimensions());

		computeMinMaxLocation(image, minLocation, maxLocation);

		System.out.println("location of minimum Value (img): " + minLocation);
		System.out.println("location of maximum Value (img): " + maxLocation);

		Img<?> imgT = process(image);

		FileSaver saver = new FileSaver(ImageJFunctions.wrap((RandomAccessibleInterval<T>) imgT, "he"));
		saver.saveAsJpeg("xray.jpg");
	}

	public <T extends RealType<T>> Img<BitType> process(Img<T> img) {
		// define threshold
		T threshold = img.firstElement().copy();
		threshold.setReal(3000);

		// apply threshold to image
		 Img<BitType> thresholdImg = threshold(img, threshold);

//		Img<BitType> thresholdImg = invert(img);

		// show the new Img that contains the threshold
		// ImageJFunctions.show(thresholdImg);

		return thresholdImg;
	}

	public <T extends Comparable<T> & Type<T>> Img<BitType> invert(Img<T> image) {
		// create a new ImgLib2 image of same dimensions
		// but using BitType, which only requires 1 bit per pixel
		ImgFactory<BitType> imgFactory = new PlanarImgFactory<BitType>();
		Img<BitType> invertedImg = imgFactory.create(image, new BitType());

		// create a cursor on the Img and the destination, it will iterate all pixels
		Cursor<T> cursor = image.cursor();
		Cursor<BitType> cursorInvertedImg = invertedImg.cursor();

		// iterate over all pixels
		while (cursor.hasNext()) {
			// get the value of the next pixel in the input
			T pixelValue = cursor.next();

			// get the value of the next pixel in the output
			BitType invertedImgValue = cursorInvertedImg.next();

			BitType aBitType = new BitType();
			aBitType.set(true);

//			if (pixelValue.compareTo())
//				invertedImgValue.set(aBitType);

		}

		return invertedImg;
	}

	public <T extends Comparable<T> & Type<T>> Img<BitType> threshold(Img<T> image, T threshold) {
		// create a new ImgLib2 image of same dimensions
		// but using BitType, which only requires 1 bit per pixel
		ImgFactory<BitType> imgFactory = new PlanarImgFactory<BitType>();
		Img<BitType> thresholdImg = imgFactory.create(image, new BitType());

		// create a cursor on the Img and the destination, it will iterate all pixels
		Cursor<T> cursor = image.cursor();
		Cursor<BitType> cursorThresholdImg = thresholdImg.cursor();

		// iterate over all pixels
		while (cursor.hasNext()) {
			// get the value of the next pixel in the input
			T pixelValue = cursor.next();

			// get the value of the next pixel in the output
			BitType thresholdImgValue = cursorThresholdImg.next();

			// set the 0 or 1 depending on the value
			if (pixelValue.compareTo(threshold) > 0)
				thresholdImgValue.setReal(1);
			else
				thresholdImgValue.setReal(0);
		}

		return thresholdImg;
	}

	public static void main(String[] args) {
		// run the example
		new Main();
		
	}
}
