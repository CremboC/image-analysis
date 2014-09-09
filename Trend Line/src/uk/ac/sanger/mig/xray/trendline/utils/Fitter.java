package uk.ac.sanger.mig.xray.trendline.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import uk.ac.sanger.mig.analysis.nodetools.Image;
import uk.ac.sanger.mig.xray.trendline.utils.maths.ExpTrendLine;
import uk.ac.sanger.mig.xray.trendline.utils.maths.LogTrendLine;
import uk.ac.sanger.mig.xray.trendline.utils.maths.OLSTrendLine;
import uk.ac.sanger.mig.xray.trendline.utils.maths.PolyTrendLine;
import uk.ac.sanger.mig.xray.trendline.utils.maths.PowerTrendLine;

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
	 */
	public Fitter(Fitting fitting, int degree) {
		this.fitting = fitting;
		this.degree = degree;
	}

	/**
	 * Fits a line to the provided image using the previously passed Fitting
	 * Type.
	 * 
	 * @param image
	 */
	public RealMatrix fit(ImgPlus<BitType> image) {
		this.image = image.copy();

		Pair<double[], double[]> points = points();

		firstBrightPixel = new Vector2D((int) points.getFirst()[0],
				(int) points.getSecond()[0]);

		switch (fitting) {
		case EXP:
			return exp(points);

		case LOG:
			return log(points);

		case POLY:
			return poly(points, degree);

		case POWER:
			return power(points);

		default:
			throw new IllegalArgumentException(
					"Fitting is not defined. Something went terribly wrong.");
		}
	}

	/**
	 * Calculates the trending line using a power equation
	 * 
	 * @param points
	 * @return
	 */
	private RealMatrix power(Pair<double[], double[]> points) {
		OLSTrendLine trend = new PowerTrendLine();

		trend.setValues(points.getFirst(), points.getSecond());

		new Debug(image.randomAccess(), trend).loopAndChange(Image.ROW,
				(int) image.dimension(Image.ROW));

		return trend.coef();
	}

	/**
	 * Calculates the trending line using an exponential equation
	 * 
	 * @param points
	 * @return
	 */
	private RealMatrix exp(Pair<double[], double[]> points) {
		OLSTrendLine trend = new ExpTrendLine();

		trend.setValues(points.getFirst(), points.getSecond());

		new Debug(image.randomAccess(), trend).loopAndChange(Image.ROW,
				(int) image.dimension(Image.ROW));

		return trend.coef();
	}

	/**
	 * Calculates the trending line using a logarithmic equation
	 * 
	 * @param points
	 * @return
	 */
	private RealMatrix log(Pair<double[], double[]> points) {
		OLSTrendLine trend = new LogTrendLine();

		trend.setValues(points.getFirst(), points.getSecond());

		new Debug(image.randomAccess(), trend).loopAndChange(Image.ROW,
				(int) image.dimension(Image.ROW));

		return trend.coef();
	}

	/**
	 * Calculates the trending line using a polynomial equation
	 * 
	 * @param points
	 * @param degree
	 * @return
	 */
	private RealMatrix poly(Pair<double[], double[]> points, int degree) {
		OLSTrendLine trend = new PolyTrendLine(degree);

		trend.setValues(points.getFirst(), points.getSecond());

		new Debug(image.randomAccess(), trend).loopAndChange(Image.ROW,
				(int) image.dimension(Image.ROW));

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
					long x = ra.getLongPosition(Image.COL);
					long y = ra.getLongPosition(Image.ROW);

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
