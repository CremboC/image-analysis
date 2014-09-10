package uk.ac.sanger.mig.xray.trendline.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import uk.ac.sanger.mig.analysis.maths.trendline.ExpTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.Fitting;
import uk.ac.sanger.mig.analysis.maths.trendline.LogTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.OLSTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.PolyTrendLine;
import uk.ac.sanger.mig.analysis.maths.trendline.PowerTrendLine;
import uk.ac.sanger.mig.analysis.nodetools.Image;
import uk.ac.sanger.mig.analysis.nodetools.enums.ReturnType;

/**
 * Contains all logic to calculate the trending line (actually a wrapper for an
 * inner package)
 * 
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
 * 
 */
public class Fitter {

	private ImgPlus<BitType> image;

	private final ReturnType retType;
	private final Fitting fitting;
	private final int degree;

	private Vector2D firstBrightPixel;

	/**
	 * Fits a line in regard to the bright pixels (1s) in the image.
	 * 
	 * @param fitting
	 *            fitting type: poly, power, exp, log etc.
	 * @param degree
	 *            if polynomial, uses degree from settings
	 * @param retTyp
	 *            whether the original image or a one with the trend line will
	 *            be returned
	 */
	public Fitter(Fitting fitting, int degree, ReturnType retTyp) {
		this.fitting = fitting;
		this.degree = degree;
		this.retType = retTyp;
	}

	/**
	 * Fits a line to the provided image using the previously passed Fitting
	 * Type.
	 * 
	 * @param image
	 */
	public String fit(ImgPlus<BitType> image) {
		this.image = image;

		Pair<double[], double[]> points = points();

		firstBrightPixel = new Vector2D((int) points.getFirst()[0],
				(int) points.getSecond()[0]);

		RealMatrix matrix = null;

		switch (fitting) {
		case EXP:
			matrix = trend(new ExpTrendLine(), points);
			break;

		case LOG:
			matrix = trend(new LogTrendLine(), points);
			break;

		case POLY:
			matrix = trend(new PolyTrendLine(degree), points);
			break;

		case POWER:
			matrix = trend(new PowerTrendLine(), points);
			break;

		default:
			throw new IllegalArgumentException(
					"Fitting is not defined. Something went terribly wrong.");
		}

		return matrixToString(matrix);
	}

	/** Converts the matrix provided trend line calculator into a string */
	private String matrixToString(RealMatrix matrix) {
		double[][] data = matrix.getData();

		String res = "";

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				res += data[i][j] + ",";
			}
		}

		return res;
	}

	/**
	 * Calculates the coefficients using the given trend type and points
	 * 
	 * @param trend
	 * @param points
	 * @return
	 */
	private RealMatrix trend(OLSTrendLine trend, Pair<double[], double[]> points) {
		trend.setValues(points.getFirst(), points.getSecond());

		if (retType == ReturnType.LINED) {
			this.image = image.copy();
			new Debug(image.randomAccess(), trend).loopAndChange(Image.ROW,
					(int) image.dimension(Image.ROW));
		}

		return trend.coef();
	}

	/**
	 * @return all the bright spots in the image, getFirst() is xs, getSecond()
	 *         is ys
	 */
	private Pair<double[], double[]> points() {
		long rows = image.dimension(Image.ROW);
		long cols = image.dimension(Image.COL);

		double[] xs = new double[(int) (rows * cols)];
		double[] ys = new double[(int) (rows * cols)];

		final RandomAccess<BitType> ra = image.randomAccess();

		ra.setPosition(0, Image.ROW);
		ra.setPosition(0, Image.COL);

		int i = 0;
		while (ra.getLongPosition(Image.ROW) != rows) {
			ra.setPosition(0, Image.COL);

			while (ra.getLongPosition(Image.COL) != cols) {

				int pixelValue = ra.get().getInteger();

				if (pixelValue == 1) {
					xs[i] = ra.getIntPosition(Image.COL);
					ys[i] = ra.getIntPosition(Image.ROW);
					++i;
				}

				ra.fwd(Image.COL);
			}

			ra.fwd(Image.ROW);
		}

		return new Pair<double[], double[]>(xs, ys);
	}

	public ImgPlus<BitType> image() {
		return image;
	}

	/**
	 * Helps drawing the trending line
	 * 
	 * @author pi1
	 * 
	 */
	private class Debug {
		private final RandomAccess<BitType> ra;

		private final OLSTrendLine trend;

		/**
		 * Helps drawing the lines to debug the bounding box
		 * 
		 * @param ra
		 *            An accessor to the image
		 * @param trend
		 *            the trend which will be used to predict the line
		 * 
		 */
		public Debug(RandomAccess<BitType> ra, OLSTrendLine trend) {
			this.ra = ra;
			this.trend = trend;

		}

		/**
		 * 
		 * @param over
		 * @param when
		 */
		public void loopAndChange(int over, int when) {

			ra.setPosition((int) firstBrightPixel.getY(), Image.ROW);
			ra.setPosition(0, Image.COL);

			final RandomAccess<BitType> modRa = ra.copyRandomAccess();

			while (ra.getIntPosition(over) != when) {

				int y = ra.getIntPosition(over);
				int x = (int) trend.predict((double) y);

				modRa.setPosition(y, Image.ROW);
				modRa.setPosition(x, Image.COL);
				int oldVal = modRa.get().getInteger();

				modRa.get().set((oldVal == 1) ? false : true);

				ra.fwd(over);
			}

		}
	}
}
