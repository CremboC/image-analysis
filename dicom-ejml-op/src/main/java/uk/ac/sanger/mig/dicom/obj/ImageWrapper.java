package uk.ac.sanger.mig.dicom.obj;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.doublematrix.impl.ImageMatrix;

import uk.ac.sanger.mig.dicom.helpers.MathHelper;
import uk.ac.sanger.mig.dicom.helpers.MatrixHelper;

/**
 * A wrapper for an image. Can calculate its centroid, central axis, etc.
 * 
 * <p>
 * Implements <a href="http://academic.thydzik.com/computer-vision-412/lab-2.htm">moments.m</a>.
 * More info at <a href="http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/OWENS/LECT2/node3.html">this page</a>.
 * 
 * @author pi1@sanger.ac.uk
 *
 */
public class ImageWrapper {

	public final static int THRESHOLD = 100;

	private Image originalImage;

	// image in a matrix format
	private Matrix imageMatrix = null, binaryImage = null;

	private Point centroid = null;
	private Line centralAxis = null;

	private double area, meanx, meany;
	
	private double thetamin, thetamax;

	private long cols, rows;

	private Matrix xMatrix, yMatrix;

	public ImageWrapper(String resource) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(resource);
		File file = new File(url.getPath());
		
		try {
			imageMatrix = new ImageMatrix(file);
		} catch (IOException e) {
			System.out.println("File " + file.getAbsolutePath() + " not found.");
			System.exit(-1);
		}

		originalImage = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());

		cols = imageMatrix.getSize(Matrix.X);
		rows = imageMatrix.getSize(Matrix.Y);
		
		// may have to handle it if it is not a square
		if (cols != rows) {
			resizeImage();
		}

		// x = ones(rows, 1) * [1 : cols];
		Matrix xCols = MatrixHelper.singleColumnIncreasing(1, cols);
		Matrix x = Matrix.factory.ones(rows, 1);
		xMatrix = x.mtimes(xCols);

		// y = [1 : rows]' * ones(1, cols);
		Matrix yCols = MatrixHelper.singleColumnIncreasing(1, rows).transpose();
		Matrix y = Matrix.factory.ones(1, cols);
		yMatrix = yCols.mtimes(y);

		binaryImage = MatrixHelper.threshold(imageMatrix, THRESHOLD);
		
//		imageMatrix.showGUI();
		binaryImage.showGUI();
	}

	private void resizeImage() {
		// TODO: handle non-square images
		
		/*
		 * old code:
		 * 		// resizing image to be a square, will help later on when dealing with
				// rotation of it
				// FIXME: hardcoded to make a size 800 dicom to be square
				List<Number> toRemove = new ArrayList<Number>();
		
				for (long i = imageMatrix.getSize(Matrix.X); i != imageMatrix.getSize(Matrix.X) - 11; i--) {
					toRemove.add(i);
				}
		
				imageMatrix = imageMatrix.deleteColumns(Ret.NEW, toRemove);
		 */
	}

	/**
	 * Calculates the centroid of an image.
	 * 
	 * @return the centroid
	 */
	public Point centroid() {
		// area = sum(sum(im));
		area = MatrixHelper.area(binaryImage);

		// meanx = sum(sum(double(im) .* x)) / area;
		meanx = MatrixHelper.meanOf(binaryImage, xMatrix, area);

		// meany = sum(sum(double(im) .* y)) / area;
		meany = MatrixHelper.meanOf(binaryImage, yMatrix, area);

		centroid = new Point(meanx, meany);

		return centroid;
	}

	/**
	 * Calculate the central axis of an image. If {@link centroid} wasn't called
	 * before, this function will call it.
	 * 
	 * @return the central axis wrapped as two points in form of a line
	 */
	public Line centralAxis() {
		if (centroid == null)
			centroid();

		// x = x - meanx
		// y = y - meany
		xMatrix.minus(Ret.ORIG, false, meanx);
		yMatrix.minus(Ret.ORIG, false, meany);

		// a = sum(sum(double(im).*x.^2));
		Matrix aMatrix = binaryImage.times(xMatrix).power(Ret.NEW, 2).sum(Ret.NEW, Matrix.ALL, false);
		double a = aMatrix.getValueSum();

		// b = sum(sum(double(im).*x.*y))*2;
		Matrix bMatrix = binaryImage.times(xMatrix).times(yMatrix).sum(Ret.NEW, Matrix.ALL, false);
		double b = bMatrix.getValueSum() * 2.0;

		// c = sum(sum(double(im).*y.^2));
		Matrix cMatrix = binaryImage.times(yMatrix).power(Ret.NEW, 2).sum(Ret.NEW, Matrix.ALL, false);
		double c = cMatrix.getValueSum();

		// denom = b^2 + (a-c)^2;
		double denom = Math.pow(b, 2) + Math.pow((a - c), 2);

		double roundess = 0.0;

		if (denom == 0) {
			/**
			 * thetamin = 2*pi*rand;
			 * thetamax = 2*pi*rand;
			 * roundness = 1;
			 */
			thetamin = 2 * Math.PI * Math.random();
			thetamax = 2 * Math.PI * Math.random();
			roundess = 1.0;
		} else {
			/**
			 * sin2thetamin = b/sqrt(denom); %positive solution
			 * sin2thetamax = -sin2thetamin;
			 * cos2thetamin = (a-c)/sqrt(denom); %positive solution
			 * cos2thetamax = -cos2thetamin;
			 */
			double sin2thetamin, sin2thetamax, cos2thetamin, cos2thetamax;

			sin2thetamin = b / Math.sqrt(denom);
			sin2thetamax = -sin2thetamin;

			cos2thetamin = (a - c) / Math.sqrt(denom);
			cos2thetamax = -cos2thetamin;

			double Imin, Imax;

			/**
			 * thetamin = atan2(sin2thetamin, cos2thetamin)/2;
			 * thetamax = atan2(sin2thetamax, cos2thetamax)/2;
			 * Imin = 0.5*(c+a) - 0.5*(a-c)*cos2thetamin - 0.5*b*sin2thetamin;
			 * Imax = 0.5*(c+a) - 0.5*(a-c)*cos2thetamax - 0.5*b*sin2thetamax;
			 * roundess = Imin/Imax;
			 */
			thetamin = Math.atan2(sin2thetamin, cos2thetamin) / 2;
			thetamax = Math.atan2(sin2thetamax, cos2thetamax) / 2;
			Imin = 0.5 * (c + a) - 0.5 * (a - c) * cos2thetamin - 0.5 * b * sin2thetamin;
			Imax = 0.5 * (c + a) - 0.5 * (a - c) * cos2thetamax - 0.5 * b * sin2thetamax;

			roundess = Imin / Imax;
		}

		/**
		 * rho = sqrt(area)/(roundness + 0.5) + 5;
		 * [X1,Y1] = pol2cart(thetamin, rho);
		 * [X2,Y2] = pol2cart(thetamin + pi, rho);
		 */
		double rho = Math.sqrt(area) / (roundess + 0.5) + 5;

		Point x1y1 = MathHelper.pol2cart(thetamin, rho);
		Point x2y2 = MathHelper.pol2cart(thetamin + Math.PI, rho);

		// line([X1 + meanx, X2 + meanx],[Y1 + meany, Y2 + meany])
		Point to = new Point(x1y1.x + meanx, x1y1.y + meany);
		Point from = new Point(x2y2.x + meanx, x2y2.y + meany);

		centralAxis = new Line(from, to);

		return centralAxis;
	}

	public Image getOriginalImage() {
		return this.originalImage;
	}

	/**
	 * If centroid is not calculated yet, will calculate it using {@link centroid}.
	 * 
	 * @return
	 */
	public Point getCentroid() {
		if (centroid == null)
			centroid();

		return this.centroid;
	}

	/**
	 * If the central axis is not calculate yet, will call {@link centralAxis}.
	 * 
	 * @return
	 */
	public Line getCentralAxis() {
		if (centralAxis == null)
			centralAxis();

		return this.centralAxis;
	}

	public double getArea() {
		return this.area;
	}

	public double getMeanx() {
		return this.meanx;
	}

	public double getMeany() {
		return this.meany;
	}

	public long getCols() {
		return this.cols;
	}

	public long getRows() {
		return this.rows;
	}

	public double getThetamin() {
		return this.thetamin;
	}

	public double getThetamax() {
		return this.thetamax;
	}
}
