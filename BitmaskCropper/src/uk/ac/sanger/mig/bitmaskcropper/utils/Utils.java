package uk.ac.sanger.mig.bitmaskcropper.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.knip.base.data.img.ImgPlusCell;

public class Utils {

	/**
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends RealType<T>> ImgPlus<T> findImageInInput(
			BufferedDataTable input) {
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

	/**
	 * 
	 * @param dataTableSpec
	 * @param columns
	 * @return
	 */
	public static Map<String, Integer> findIndices(DataTableSpec dataTableSpec,
			Map<String, SettingsModel> columns) {

		Map<String, Integer> indices = new HashMap<String, Integer>();
		
		// get the index of the column which is specified in the settings
		// makes it faster to retrieve it later on
		String[] as = dataTableSpec.getColumnNames();
		for (int i = 0; i < as.length; i++) {
			for (Entry<String, SettingsModel> e : columns.entrySet()) {
				if (e.getValue() instanceof SettingsModelColumnName) {
					if (as[i].equals(((SettingsModelColumnName) e.getValue()).getStringValue())) {
						indices.put(e.getKey(), i);
					}
				}
			}
		}

		return indices;
	}

	@SuppressWarnings("unchecked")
	public static <T extends RealType<T>> ImgPlus<T> imageByIndex(DataRow row, Integer index) {
		ImgPlusCell<T> ipcell = (ImgPlusCell<T>) row.getCell(index);
		return ipcell.getImgPlus();
	}
}
