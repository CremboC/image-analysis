package uk.ac.sanger.mig.xray.trendlinecropper.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.knime.core.node.NodeLogger;

import uk.ac.sanger.mig.analysis.maths.trendline.ExpTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.Fitting;
import uk.ac.sanger.mig.analysis.maths.trendline.LogTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.OLSTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.PolyTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.PowerTrendLine;
import uk.ac.sanger.mig.analysis.nodetools.Image;
import uk.ac.sanger.mig.xray.trendlinecropper.TrendLineCropperNodeModel;

/**
 * Wraps the logic to crop the region around the trend line
 * 
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
 * 
 */
public class TrendCropper<T extends RealType<T> & NativeType<T>> {
	
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(TrendLineCropperNodeModel.class);

	private final int leftMargin, rightMargin, startRow, endRow, topLeftMargin,
			topRightMargin;

	private final boolean cropTop;
	
	private final static int FORWARD = 0, BACKWARD = 1;

	/**
	 * 
	 * @param leftMargin
	 *            how far to the left to crop out pixels
	 * @param rightMargin
	 *            how far to the right to crop out pixels
	 * @param startRow
	 *            row to start cropping from
	 * @param endRow
	 *            last row to start cropping from. -1 is end of image
	 * @param topRightMargin
	 * @param topLeftMargin
	 * @param cropTop
	 */
	public TrendCropper(int leftMargin, int rightMargin, int startRow,
			int endRow, boolean cropTop, int topLeftMargin, int topRightMargin) {
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
		this.startRow = startRow;
		this.endRow = endRow;

		this.topLeftMargin = topLeftMargin;
		this.topRightMargin = topRightMargin;
		this.cropTop = cropTop;
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
		// random access to travel anywhere in the image to modify the pixel
		// values
		final RandomAccess<T> modRa = ra.copyRandomAccess();

		ra.setPosition(startRow, Image.ROW);
		ra.setPosition(0, Image.COL);

		final int firstY = ra.getIntPosition(Image.ROW);
		final int firstX = (int) trend.predict(firstY);

		try {
			while (ra.getIntPosition(Image.ROW) != actualEndRow) {

				final int y = ra.getIntPosition(Image.ROW);
				final int x = (int) trend.predict(y);

				// set position to predicted x and y
				modRa.setPosition(x, Image.COL);
				modRa.setPosition(y, Image.ROW);

				// delete pixels to the right of the trend line
				setPixelsIn(modRa, Image.COL, (x + rightMargin), 0, FORWARD, (int) cols);

				// delete pixels to the left of the trend line
				setPixelsIn(modRa, Image.COL, (x - leftMargin), 0, BACKWARD, (int) cols);

				ra.fwd(Image.ROW);
			}	
		} catch (ArrayIndexOutOfBoundsException e) {
			// shouldn't get here
			logger.debug(e.getStackTrace());
			logger.fatal("Critical error analysing one of the images.");
		}

		if (cropTop)
			cropTop(image, firstY, firstX);

		return image;
	}

	/**
	 * Crops the top of the spine using the first row of the trend line function.
	 * Uses the user specified left and right margins.
	 * 
	 * @param image
	 * @param firstX
	 * @param firstY
	 */
	private void cropTop(ImgPlus<T> image, int lastRow, int column) {
		final long cols = image.dimension(Image.COL);
		
		// random access to traverse the image vertically
		final RandomAccess<T> ra = image.randomAccess();
		
		// random access to travel anywhere in the image to modify the pixel
		// values
		final RandomAccess<T> modRa = ra.copyRandomAccess();
		
		ra.setPosition(0, Image.ROW);
		ra.setPosition(0, Image.COL);
		
		while (ra.getIntPosition(Image.ROW) != lastRow) {
			
			final int y = ra.getIntPosition(Image.ROW);
			
			// set position to x and y
			modRa.setPosition(column, Image.COL);
			modRa.setPosition(y, Image.ROW);
			
			// delete pixels to the right of the trend line
			setPixelsIn(modRa, Image.COL, (column + topLeftMargin), 0, FORWARD, (int) cols);
			
			// delete pixels to the left of the trend line
			setPixelsIn(modRa, Image.COL, (column - topLeftMargin), 0, BACKWARD, (int) cols);
			
			ra.fwd(Image.ROW);
		}
	}
	
	/**
	 * Can set pixel values in a row or column, avoid "out of bounds" exceptions.
	 * Used for deleting pixels on the side of the trend line.
	 * 
	 * @param ra to access the pixels
	 * @param in Image.ROW or Image.COL usually
	 * @param until will set pixels until this ra position condition is met
	 * @param set what value to set the pixels
	 * @param way go {@link #BACKWARD} or {@link #FORWARD}?
	 * @param avoid last row/column, if reached, will break loop (protects from exceptions)
	 */
	private void setPixelsIn(RandomAccess<T> ra, int in, int until, int set, int way, int avoid) {

		while (ra.getIntPosition(in) != until) {
			ra.get().setReal(set);

			if (way == FORWARD) {
				// if the line goes very close to the right edge, it might try
				// to write black pixels there, to avoid that just break, as
				// we have reached the end
				if ((ra.getIntPosition(in) + 1) > avoid)
					break;
			} else {
				// if the line goes very close to the left edge, it may go
				// to negative coordinates. Break to avoid this.
				if ((ra.getIntPosition(in) - 1) < 0)
					break;
			}

			if (way == FORWARD) {				
				ra.fwd(in);
			} else {
				ra.bck(in);
			}
		}
		
	}

	/**
	 * Converts the trend type specified in the table into a trend line object
	 * and sets the provided ceofs
	 * 
	 * @param trendType
	 *            in form a string
	 * @param coef
	 *            in form of a matrix
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
