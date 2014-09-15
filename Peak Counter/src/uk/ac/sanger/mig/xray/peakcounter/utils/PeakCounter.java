package uk.ac.sanger.mig.xray.peakcounter.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import uk.ac.sanger.mig.analysis.nodetools.Image;

/**
 * 
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
 *
 */
public class PeakCounter {

	private final static int BLACK = 0, WHITE = 1;

	private final int bucketThreshold;

	/**
	 * 
	 * @param bucketThreshold
	 */
	public PeakCounter(int bucketThreshold) {
		this.bucketThreshold = bucketThreshold;
	}

	/**
	 * Counts the number of peaks
	 * 
	 * @param image
	 */
	public int count(ImgPlus<BitType> image) {

		final int cols = (int) image.dimension(Image.COL);
		final int rows = (int) image.dimension(Image.ROW);

		// random access to traverse the image vertically
		final RandomAccess<BitType> ra = image.randomAccess();

		ra.setPosition(0, Image.ROW);
		ra.setPosition(0, Image.COL);

		// max buckets = peak number, row is used for debugging
		int maxBuckets = 0, maxBucketsRow = 0;

		int lastPixelValue = BLACK;
		while (ra.getIntPosition(Image.ROW) != rows) {

			int rowBuckets = 0;

			ra.setPosition(1, Image.COL);
			while (ra.getIntPosition(Image.COL) != cols) {

				final int currentPixelValue = ra.get().getInteger();

				if (currentPixelValue == WHITE && lastPixelValue == BLACK) {

					if (checkSurround(ra, ra.getIntPosition(Image.COL)))
						++rowBuckets;
				}

				lastPixelValue = currentPixelValue;

				ra.fwd(Image.COL);
			}

			if (rowBuckets > maxBuckets) {
				maxBuckets = rowBuckets;
				maxBucketsRow = ra.getIntPosition(Image.ROW);
			}

			ra.fwd(Image.ROW);
		}

		return maxBuckets;
	}

	/**
	 * Checks previous pixels according to the threshold
	 * @param inRa
	 * @param column
	 * @return whether this the current pixel is a part of a new bucket
	 */
	private boolean checkSurround(RandomAccess<BitType> inRa, int column) {
		final RandomAccess<BitType> ra = inRa.copyRandomAccess();
		int pos;

		// move back by one pixel because current pixel is the white that we
		// detected
		ra.bck(Image.COL);

		while ((pos = ra.getIntPosition(Image.COL)) != column - bucketThreshold) {

			final int pix = ra.get().getInteger();

			if (pix == WHITE) {
				return false;
			}

			if (pos - 1 < 0) {
				break;
			}
			ra.bck(Image.COL);
		}

		return true;
	}

}
