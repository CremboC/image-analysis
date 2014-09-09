package uk.ac.sanger.mig.xray.trendline.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.util.Pair;

import uk.ac.sanger.mig.analysis.nodetools.Image;

public class Fitter {

	private ImgPlus<BitType> image;
	
	private final Fitting fitting;
	private final int degree;

	/**
	 * 
	 * @param fitting
	 * @param degree
	 */
	public Fitter(Fitting fitting, int degree) {
		this.fitting = fitting;
		this.degree = degree;
	}

	/**
	 * 
	 * @param image
	 */
	public void fit(ImgPlus<BitType> image) {
		this.image = image;
		
		Pair<int[], int[]> points = points();

		switch (fitting) {
		case EXP:
			exp(points);
			break;

		case LOG:
			log(points);
			break;

		case POLY:
			poly(points);
			break;

		case POWER:
			power(points, degree);
			break;

		default:
			throw new IllegalArgumentException(
					"Fitting is not defined. Something went terribly wrong.");
		}
	}

	private void power(Pair<int[],int[]> points, int degree) {
		
	}

	private void exp(Pair<int[],int[]> points) {

	}

	private void log(Pair<int[],int[]> points) {

	}

	private void poly(Pair<int[],int[]> points) {

	}
	
	private Pair<int[], int[]> points() {
		long rows = image.dimension(Image.ROW);
		long cols = image.dimension(Image.COL);
		
		int[] xs = new int[(int) cols];
		int[] ys = new int[(int) rows];
		
		final RandomAccess<BitType> ra = image.randomAccess();
		
		ra.setPosition(0, Image.ROW);
		ra.setPosition(0, Image.COL);
		
		int i = 0;
		while (ra.getLongPosition(Image.COL) != cols) {
			ra.setPosition(0, Image.ROW);
			
			while (ra.getLongPosition(Image.ROW) != rows) {
				
				int pixelValue = ra.get().getInteger();
				
				if (pixelValue == 1) {
					xs[i] = ra.getIntPosition(Image.COL);
					ys[i] = ra.getIntPosition(Image.ROW);
					++i;
				}
				
				ra.fwd(Image.ROW);
			}
			
			ra.fwd(Image.COL);
		}
		
		return new Pair<int[], int[]>(xs, ys);
	}
}
