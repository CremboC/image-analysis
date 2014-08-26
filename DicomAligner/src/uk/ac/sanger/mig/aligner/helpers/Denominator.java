package uk.ac.sanger.mig.aligner.helpers;

import net.imglib2.Cursor;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

public class Denominator {

	private final int[] x;
	private final int[] y;
	private final ImgPlus<BitType> im;

	private double denom;
	private double a = 0, b = 0, c = 0;

	public Denominator(int[] x, int[] y, ImgPlus<BitType> im) {
		this.x = x;
		this.y = y;
		this.im = im;
	}

	/**
	 * a component of the denominator: sum(sum(double(im).*x.^2))
	 * 
	 * @return -0 if denom 0, meaning probably wasn't calculate yet
	 */
	public double a() {
		if (denom == 0)
			return -0;

		return a;
	}

	/**
	 * b component of the denominator: sum(sum(double(im).*x.*y))*2
	 * 
	 * @return -0 if denom 0, meaning probably wasn't calculate yet
	 */
	public double b() {
		if (denom == 0)
			return -0;

		return b;
	}

	/**
	 * c component of the denominator: sum(sum(double(im).*y.^2))
	 * 
	 * @return -0 if denom 0, meaning probably wasn't calculate yet
	 */
	public double c() {
		if (denom == 0)
			return -0;

		return c;
	}

	// b^2 + (a-c)^2
	public double denom() {
		if (denom != 0)
			return denom;

		// cursor over input image
		final Cursor<BitType> inCursor = im.localizingCursor();

		// iterate over pixels of in input image
		int i = 0;
		while (inCursor.hasNext()) {
			inCursor.fwd();

			double val = inCursor.get().getRealDouble();

			int xi = x[i], yi = y[i];

			a += Math.pow((val * xi), 2);
			b += val * xi * yi;
			c += Math.pow((val * yi), 2);

			i++;
		}

		b *= 2;
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);

		return (denom = Math.pow(b, 2) + Math.pow((a - c), 2));
	}
}
