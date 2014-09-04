package uk.ac.sanger.mig.intervalthreshold.utils;

import net.imglib2.Cursor;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class IntervalThreshold<T extends RealType<T> & NativeType<T>> {
	
	private final int lowerThreshold;
	private final int upperThreshold;
	private final int backgroundPixelValue;

	public IntervalThreshold(int lowerThreshold,
			int upperThreshold, int backgroundPixelValue) {
		
		this.lowerThreshold = lowerThreshold;
		this.upperThreshold = upperThreshold;
		this.backgroundPixelValue = backgroundPixelValue;
		
	}
	
	public ImgPlus<T> process(ImgPlus<T> image) {
		
		ImgPlus<T> returnImage = image.copy();
		
		Cursor<T> cursor = image.localizingCursor();
		Cursor<T> outCursor = returnImage.localizingCursor();
		
		while(cursor.hasNext()){
			cursor.next();
			outCursor.next();
			
			int pixelVal = (int)cursor.get().getRealDouble();
			
			if(pixelVal < lowerThreshold || pixelVal > upperThreshold) {
				outCursor.get().setReal((float)backgroundPixelValue);
			}
		}

		return returnImage;
		
	}

}
