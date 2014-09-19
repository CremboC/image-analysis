package uk.ac.sanger.mig.xray.activecontour.utils;

import net.imglib2.RandomAccess;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.real.DoubleType;

public class ActiveContour {

	public ImgPlus<DoubleType> greedy(ImgPlus<DoubleType> ip,
			ImgPlus<DoubleType> gradX, ImgPlus<DoubleType> gradY) {

		ImgPlus<DoubleType> image = ip.copy();
		
		final RandomAccess<DoubleType> raGradX = gradX.randomAccess();
		final RandomAccess<DoubleType> raGradY = gradY.randomAccess();
		
		return null;
	}

	private int external() {
		return 0;
	}

	private int internal() {
		return 0;
	}

}
