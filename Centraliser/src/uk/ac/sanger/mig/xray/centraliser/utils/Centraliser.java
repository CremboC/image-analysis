package uk.ac.sanger.mig.xray.centraliser.utils;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import uk.ac.sanger.mig.analysis.nodetools.Image;

/**
 * Contains logic to centralise white pixels
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
 * 
 */
public class Centraliser {

	private final static int BLACK = 0, WHITE = 1;

	/**
	 * 
	 */
	public Centraliser() {

	}

	/**
	 * Moves all the pixels in the provided image to match the center of the
	 * image. Uses the centroid as a reference, meaning the centroid pixel will
	 * be moved to the old center of the image.
	 * 
	 * @param ip
	 *            image to center
	 * @param centroidX
	 *            x coordinate of the centroid
	 * @param centroidY
	 *            y coordinate of the centroid
	 * @return centered image
	 */
	public ImgPlus<BitType> process(ImgPlus<BitType> ip,
			final double centroidX, final double centroidY) {

		// create empty copy of the image
		final ImgPlus<BitType> image = new ImgPlus<BitType>(
				new ArrayImgFactory<BitType>().create(ip, new BitType()));

		final long cols = ip.dimension(Image.COL);
		final long rows = ip.dimension(Image.ROW);

		final long centerX = cols / 2;
		final long centerY = rows / 2;

		final int dx = (int) Math.abs(centerX - centroidX);
		final int dy = (int) Math.abs(centerY - centroidY);

		// accessors for both images. more efficient to just jump to the
		// required location in the new image instead of go pixel by pixel
		final Cursor<BitType> c = ip.localizingCursor();
		final RandomAccess<BitType> ra = image.randomAccess();

		try {
			while (c.hasNext()) {
				c.next();

				final int pixelValue = c.get().getInteger();

				// shortcut to skip black pixels
				if (pixelValue == BLACK)
					continue;

				// pixel is white, get its coordinates
				int x = c.getIntPosition(Image.COL);
				int y = c.getIntPosition(Image.ROW);

				// correct coordinates according to its position
				x = (centroidX > centerX) ? x + dx : x - dx;
				y = (centroidY > centerY) ? y - dy : y + dy;

				// if it's trying to move it to a negative pixel, skip it, we
				// can afford losing some pixels. Probably. Should protect
				// against OutOfBounds exception
				if (x > cols || x < 0 || y > rows || y < 0) {
					continue;
				}

				ra.setPosition(x, Image.COL);
				ra.setPosition(y, Image.ROW);

				ra.get().set(true);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// I don't even know how it can get here
			e.printStackTrace();
		}

		return image;
	}

}
