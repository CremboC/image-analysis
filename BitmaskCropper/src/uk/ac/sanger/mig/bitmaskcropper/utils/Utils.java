package uk.ac.sanger.mig.bitmaskcropper.utils;

import java.util.Iterator;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.node.BufferedDataTable;
import org.knime.knip.base.data.img.ImgPlusCell;

public class Utils {

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T>> ImgPlus<T> findImageInInput(BufferedDataTable input) {
		ImgPlusCell<T> img = null;
		
		Iterator<DataRow> iter = input.iterator();
		while (iter.hasNext()) {
			DataRow row = iter.next();
			
			for (int i = 0; i < row.getNumCells(); i++) {
				if (row.getCell(i) instanceof ImgPlusCell<?>) {
					img = (ImgPlusCell<T>) row.getCell(i);
					break;
				}
			}
			
			if (img == null) {
				throw new IllegalStateException("No image in the input.");
			}
		}

		return img.getImgPlus();
	}
}
