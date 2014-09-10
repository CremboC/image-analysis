package uk.ac.sanger.mig.xray.trendlinecropper.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import uk.ac.sanger.mig.analysis.nodetools.Image;

public class TrendCropper<T extends RealType<T> & NativeType<T>> {
	
	private final int leftMargin, rightMargin, startRow, endRow;
	
	public TrendCropper(int leftMargin, int rightMargin, int startRow, int endRow) {
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
		this.startRow = startRow;
		this.endRow = endRow;
	}
	
	/**
	 * Crop out a region of the image following the trend line.
	 * @param inImage Image that will be cropped. Original image is not modified
	 * @param coefs coeficients that are used to predict points of the trendline
	 * @return image with specified region cropped out
	 */
	public ImgPlus<T> process(ImgPlus<T> inImage, String coefs) {
		ImgPlus<T> image = inImage.copy();
		
		long cols = image.dimension(Image.COL);
		long rows = image.dimension(Image.ROW);
		
		RealMatrix coefMatrix = convertCoefs(coefs);
		
		// if user didn't set the ending row, will use the last row
		int actualEndRow = (int) ((endRow == -1) ? rows : endRow);  
		
		final RandomAccess<T> ra = image.randomAccess();
		
		ra.setPosition(startRow, Image.ROW);
		ra.setPosition(0, Image.COL);
		
		return image;
	}

	/** Converts coefs into a matrix */
	private RealMatrix convertCoefs(String coefs) {
		String[] split = coefs.split(",");
		double[] vals = new double[split.length];
		
		for (int i = 0; i < split.length; i++) {
			vals[i] = Double.parseDouble(split[i]);
		}
		
		return MatrixUtils.createColumnRealMatrix(vals);
	}

}
