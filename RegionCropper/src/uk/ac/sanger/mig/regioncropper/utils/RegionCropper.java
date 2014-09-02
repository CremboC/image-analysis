package uk.ac.sanger.mig.regioncropper.utils;

import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * 
 * 
 * @author Paulius pi1@sanger.ac.uk
 * 
 */
public class RegionCropper {
	
	private final static int COL = 0, ROW = 1;
	
	private final int upperBoundary, lowerBoundary, rightBoundary, leftBoundary;

	public RegionCropper(int upperBoundary, int lowerBoundary,
			int rightBoundary, int leftBoundary) {

		this.upperBoundary = upperBoundary;
		this.lowerBoundary = lowerBoundary;
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
	}

	public ImgPlus<UnsignedByteType> crop(ImgPlus<?> image) {
		int cols = (int) image.dimension(COL);
		int rows = (int) image.dimension(ROW);
		
		int[] dimensions = { rightBoundary, lowerBoundary };
		
		
		ImgPlus<UnsignedByteType> convertedMask = new ImgPlus<UnsignedByteType>(
				new ArrayImgFactory<UnsignedByteType>().create(dimensions,
						new UnsignedByteType()));
		
		
		return null;

	}

}
