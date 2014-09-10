package uk.ac.sanger.mig.xray.trendlinecropper.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import uk.ac.sanger.mig.analysis.maths.trendline.ExpTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.Fitting;
import uk.ac.sanger.mig.analysis.maths.trendline.LogTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.OLSTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.PolyTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.PowerTrendLine;
import uk.ac.sanger.mig.analysis.nodetools.Image;

/**
 * Wraps the logic to crop the region around the trend line
 *
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
 *
 */
public class TrendCropper<T extends RealType<T> & NativeType<T>> {

	private final int leftMargin, rightMargin, startRow, endRow;

	/**
	 *
	 * @param leftMargin how far to the left to crop out pixels
	 * @param rightMargin how far to the right to crop out pixels
	 * @param startRow row to start cropping from
	 * @param endRow last row to start cropping from. -1 is end of image
	 */
	public TrendCropper(int leftMargin, int rightMargin, int startRow,
			int endRow) {
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
		this.startRow = startRow;
		this.endRow = endRow;
	}

	/**
	 * Crop out a region of the image following the trend line.
	 *
	 * @param inImage
	 *            Image that will be cropped. Original image is not modified
	 * @param coefs
	 *            coeficients that are used to predict points of the trendline
	 * @param trendType
	 *            the trend type, produced by the Trend Line node
	 * @return image with specified region cropped out
	 */
	public ImgPlus<T> process(ImgPlus<T> inImage, String coefs, String trendType) {
		final ImgPlus<T> image = inImage.copy();

		final long cols = image.dimension(Image.COL);
		final long rows = image.dimension(Image.ROW);

		// if user didn't set the ending row, will use the last row
		final int actualEndRow = (int) ((endRow == -1) ? rows : endRow);

		final OLSTrendLine trend = getTrendType(trendType, convertCoefs(coefs));

		// random access to traverse the image vertically
		final RandomAccess<T> ra = image.randomAccess();
		// random access to travel anywhere in the image to modify the pixel values
		final RandomAccess<T> modRa = ra.copyRandomAccess();

		ra.setPosition(startRow, Image.ROW);
		ra.setPosition(0, Image.COL);

		while (ra.getIntPosition(Image.ROW) != actualEndRow) {

			final int y = ra.getIntPosition(Image.ROW);
			final int x = (int) trend.predict(y);

			// set position to predicted x and y
			modRa.setPosition(x, Image.COL);
			modRa.setPosition(y, Image.ROW);

			// delete pixels to the right of the trend line
			while (modRa.getIntPosition(Image.COL) != x + rightMargin) {
				modRa.get().setReal(0);

				// if the line goes very close to the right edge, it might try
				// to write black pixels there, to avoid that just break, as
				// we have reached the end
				if (modRa.getIntPosition(Image.COL) + 1 > cols)
					break;

				modRa.fwd(Image.COL);
			}

			// delete pixels to the left of the trend line
			while (modRa.getIntPosition(Image.COL) != x - leftMargin) {
				modRa.get().setReal(0);

				// if the line goes very close to the left edge, it may go
				// to negative coordinates. Break to avoid this.
				if (modRa.getIntPosition(Image.COL) - 1 < 0)
					break;

				modRa.bck(Image.COL);
			}

			ra.fwd(Image.ROW);
		}

		return image;
	}

	/**
	 * Converts the trend type specified in the table into a trend line object and sets
	 * the provided ceofs
	 *
	 * @param trendType in form a string
	 * @param coef in form of a matrix
	 * @return
	 */
	private OLSTrendLine getTrendType(String trendType, RealMatrix coef) {
		Fitting trend = null;
		int degree = 0;

		// special case for polynomial trend as we need the degree as well
		if (trendType.contains("Poly")) {
			final String[] parts = trendType.split(":");
			degree = Integer.parseInt(parts[1]);

			trend = Fitting.whereName(parts[0]);
		} else {
			trend = Fitting.whereName(trendType);
		}

		OLSTrendLine trendLine = null;

		switch (trend) {
		case EXP:
			trendLine = new ExpTrendLine();
			break;

		case LOG:
			trendLine = new LogTrendLine();
			break;

		case POLY:
			trendLine = new PolyTrendLine(degree);
			break;

		case POWER:
			trendLine = new PowerTrendLine();
			break;

		default:
			throw new IllegalArgumentException(
					"Non-existant fitting type. Either table was modified or something went terribly wrong.");
		}

		trendLine.setCoef(coef);
		return trendLine;
	}

	/** Converts coefs into a matrix */
	private RealMatrix convertCoefs(String coefs) {
		final String[] split = coefs.split(",");
		final double[] vals = new double[split.length];

		for (int i = 0; i < split.length; i++) {
			vals[i] = Double.parseDouble(split[i]);
		}

		return MatrixUtils.createColumnRealMatrix(vals);
	}

}
