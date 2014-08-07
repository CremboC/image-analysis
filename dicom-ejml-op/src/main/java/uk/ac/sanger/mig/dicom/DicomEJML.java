package uk.ac.sanger.mig.dicom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.doublematrix.impl.ImageMatrix;

/**
 * Matlab -> Java test
 * 
 * @author pi1
 * @param <K>
 *
 */
public class DicomEJML {

	public static void main(String[] args) throws IOException {
		DicomEJML ej = new DicomEJML();
		
		Matrix matrix;

		matrix = ej.moments();
	}

	public final static int THRESHOLD = 500;
	public final static double normalizationFactor = 1000;

	/**
	 * Implements moments.m. See link.
	 * 
	 * @throws IOException
	 * @see <a href="http://academic.thydzik.com/computer-vision-412/lab-2.htm">moments.m</a>
	 */
	private Matrix moments() throws IOException {
		long startTime = System.currentTimeMillis();

		// loads the image file into a matrix
		File file = new File("largentail.jpg");
		Matrix imageMatrix = new ImageMatrix(file);

		// resizing image to be a square, will help later on when dealing with
		// rotation of it
		// FIXME: hardcoded to make a size 800 dicom to be square
		List<Number> toRemove = new ArrayList<Number>();

		for (long i = imageMatrix.getSize(Matrix.X); i != imageMatrix.getSize(Matrix.X) - 11; i--) {
			toRemove.add(i);
		}

		imageMatrix = imageMatrix.deleteColumns(Ret.NEW, toRemove);
		
		// [rows, cols] = size(im);
		long cols = imageMatrix.getSize(Matrix.X);
		long rows = imageMatrix.getSize(Matrix.Y);

		// used to change the image pixel values to range 0-255, broken.
		// imageMatrix = imageMatrix.convert(ValueType.SHORT);
		// imageMatrix = MatrixHelper.convertImageByMap(imageMatrix);

		// x = ones(rows, 1) * [1 : cols];
		Matrix xCols = MatrixHelper.singleColumnIncreasing(1, cols);
		Matrix x = Matrix.factory.ones(rows, 1);
		x = x.mtimes(xCols);

		// y = [1 : rows]' * ones(1, cols);
		Matrix yCols = MatrixHelper.singleColumnIncreasing(1, rows).transpose();
		Matrix y = Matrix.factory.ones(1, cols);
		y = yCols.mtimes(y);

		// convert image to binary 0/1 (b&w)
		imageMatrix = MatrixHelper.threshold(imageMatrix, THRESHOLD);

		// area = sum(sum(im));
		Matrix areaMatrix = imageMatrix.sum(Ret.NEW, Matrix.ALL, false);
		double area = areaMatrix.getValueSum();

		// meanx = sum(sum(double(im) .* x)) / area;
		Matrix meanxMatrix = imageMatrix.times(x).sum(Ret.NEW, Matrix.ALL, false);
		double meanx = (meanxMatrix.getValueSum() / area);

		// meany = sum(sum(double(im) .* y)) / area;
		Matrix meanyMatrix = imageMatrix.times(y).sum(Ret.NEW, Matrix.ALL, false);
		double meany = (meanyMatrix.getValueSum() / area);

		// draw the centroid
		imageMatrix.setAsBoolean(false, (int) meanx, (int) meany);

		// x = x - meanx
		// y = y - meany
		x.minus(Ret.ORIG, false, meanx);
		y.minus(Ret.ORIG, false, meany);

		// a = sum(sum(double(im).*x.^2));
		Matrix aMatrix = imageMatrix.times(x).power(Ret.NEW, 2).sum(Ret.NEW, Matrix.ALL, false);
		double a = aMatrix.getValueSum();

		// b = sum(sum(double(im).*x.*y))*2;
		Matrix bMatrix = imageMatrix.times(x).times(y).sum(Ret.NEW, Matrix.ALL, false);
		double b = bMatrix.getValueSum() * 2.0;

		// c = sum(sum(double(im).*y.^2));
		Matrix cMatrix = imageMatrix.times(y).power(Ret.NEW, 2).sum(Ret.NEW, Matrix.ALL, false);
		double c = cMatrix.getValueSum();

		// denom = b^2 + (a-c)^2;
		double denom = Math.pow(b, 2) + Math.pow((a - c), 2);

		double thetamin = 0.0, thetamax = 0.0, roundess = 0.0;

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
		// corected to get rid of negative coordinates
		Point correctedFrom = MathHelper.pol2cart(360 - MathHelper.angleToHor(from), 0.1);

		imageMatrix = MatrixHelper.bresenham(imageMatrix, correctedFrom, to);

		// time checking
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("moments:" + elapsedTime);

		return rotate(imageMatrix, to);
	}
	
	/**
	 * Rotates the matrix to correct it
	 * @param matrix
	 * @return
	 */
	private Matrix rotate(Matrix matrix, Point to) {
		long startTime = System.currentTimeMillis();
		
		Matrix imageMatrix = matrix;
		Matrix rotatedMatrix = null;
		
//		double angle = MathHelper.angleToHor(to);
		double angle = 95.0;
		
		// create the rotation matrix [cos a, -sin a; sin a, cos a]
		Matrix rotationMatrix = Matrix.factory.zeros(3, 3); // (row , col)
		rotationMatrix.setAsDouble(Math.cos(Math.toRadians(angle)), 0, 0);
		rotationMatrix.setAsDouble(-Math.sin(Math.toRadians(angle)), 0, 1);
		rotationMatrix.setAsDouble(Math.sin(Math.toRadians(angle)), 1, 0);
		rotationMatrix.setAsDouble(Math.cos(Math.toRadians(angle)), 1, 1);
		rotationMatrix.setAsDouble(0, 2, 0);
		rotationMatrix.setAsDouble(0, 2, 1);
		rotationMatrix.setAsDouble(0, 0, 2);
		rotationMatrix.setAsDouble(0, 1, 2);
		rotationMatrix.setAsDouble(1, 2, 2);
		
		long cols = imageMatrix.getColumnCount();

		Matrix lastCoord = Matrix.factory.zeros(1, 3);
		lastCoord.setAsInt((int) cols, 0, 0);
		lastCoord.setAsInt(0, 0, 1);
		lastCoord.setAsInt(1, 0, 2);
		lastCoord.times(Ret.ORIG, true, rotationMatrix);
		
		// get the new side size
		double side = Math.sqrt(Math.pow(cols, 2) - Math.pow(lastCoord.getAsDouble(0, 0), 2)) + lastCoord.getAsDouble(0, 0);
		
		// set the size of the new rotated matrix
		rotatedMatrix = Matrix.factory.zeros((int) Math.round(side), (int) Math.round(side));
		
		// transform the whole matrix
		rotatedMatrix = MatrixHelper.transform(imageMatrix, rotationMatrix, rotatedMatrix);
		
		// time checking
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("rotation: " + elapsedTime);
		
		rotatedMatrix.showGUI();
		
		return rotatedMatrix;
	}
}
