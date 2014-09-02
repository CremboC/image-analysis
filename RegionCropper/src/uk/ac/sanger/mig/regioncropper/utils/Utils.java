package uk.ac.sanger.mig.regioncropper.utils;

import java.util.HashMap;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.knip.base.data.img.ImgPlusCell;

/**
 * Utilities for different, generally unrelated things
 * 
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team mig@sanger.ac.uk
 * 
 */
public class Utils {

	/**
	 * 
	 * @param dataTableSpec
	 * @param columns
	 * @return
	 */
	public static Map<String, Integer> indices(DataTableSpec dataTableSpec) {

		Map<String, Integer> indices = new HashMap<String, Integer>();

		// get the index of the column which is specified in the settings
		// makes it faster to retrieve it later on
		String[] as = dataTableSpec.getColumnNames();
		for (int i = 0; i < as.length; i++) {
			indices.put(as[i], i);
		}

		return indices;
	}

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T>> ImgPlus<T> imageByIndex(DataRow row,
			Integer index) {
		ImgPlusCell<T> ipcell = (ImgPlusCell<T>) row.getCell(index);
		return ipcell.getImgPlus();
	}

	public static double doubleByIndex(DataRow row, Integer index) {
		DoubleCell c = (DoubleCell) row.getCell(index);
		return c.getDoubleValue();
	}

	public static String stringFromSetting(SettingsModel setting) {
		return ((SettingsModelColumnName) setting).getStringValue();
	}

	public static int[] split(String threshold) {
		String[] thresholds = threshold.split(",");
		
		if (thresholds.length > 2) {
			throw new IllegalArgumentException(
					"Row and Col thresholds must follow a 'X,Y' format or a single number, i.e. 10");
		}
		
		int[] ret = new int[2];
		
		ret[0] = Integer.parseInt(thresholds[0]);
		
		if (thresholds.length == 1) {
			ret[1] = Integer.parseInt(thresholds[0]);
		} else {
			ret[1] = Integer.parseInt(thresholds[1]);
		}
		
		return ret;
	}
}
