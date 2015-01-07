/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Node Toolset.
 * 
 * Node Toolset is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option ) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.sanger.mig.analysis.nodetools;

import java.util.HashMap;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.base.data.img.ImgPlusCell;

/**
 * Utilities for different, generally unrelated things
 * 
 * @author p1 pi1@sanger.ac.uk
 */
public class Utils {

	/**
	 * Gets all indices of the columns and maps column_name -> index
	 * @param dataTableSpec input table
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

	/**
	 * Gets an image from the provided row by column index.
	 * Column must be ImgPlusCell.
	 * 
	 * @param row get data from here
	 * @param index column index
	 */
	@SuppressWarnings("unchecked")
	public static <T extends RealType<T> & NativeType<T>> ImgPlus<T> imageByIndex(DataRow row,
			Integer index) {
		ImgPlusCell<T> ipcell = (ImgPlusCell<T>) row.getCell(index);
		return ipcell.getImgPlus();
	}

	/**
	 * Gets a double from the provided row by column index. 
	 * Works only if the cell is a DoubleCell
	 * 
	 * @param row get data from this row
	 * @param index column index
	 */
	public static double doubleByIndex(DataRow row, Integer index) {
		DoubleCell c = (DoubleCell) row.getCell(index);
		return c.getDoubleValue();
	}
	
	/**
	 * Gets a int from the provided row by column index. 
	 * Works only if the cell is an IntCell
	 * 
	 * @param row get data from this row
	 * @param index column index
	 */
	public static int intByIndex(DataRow row, Integer index) {
		IntCell c = (IntCell) row.getCell(index);
		return c.getIntValue();
	}
	
	/**
	 * Gets a string from the provided row by column index.
	 * Works only if cell is a StringCell
	 * 
	 * @param row get data from this row
	 * @param index column index
	 * @return
	 */
	public static String stringByIndex(DataRow row, int index) {
		StringCell c = (StringCell) row.getCell(index);
		return c.getStringValue();
	}

	/**
	 * Casts a settings model into a String one and gets its value
	 * @param setting
	 * @return
	 */
	public static String stringFromSetting(SettingsModel setting) {
		return ((SettingsModelString) setting).getStringValue();
	}
	
	/**
	 * Casts a settings model into a Integer one and gets its value
	 * 
	 * @param setting
	 */
	public static int intFromSetting(SettingsModel setting) {
		return ((SettingsModelInteger) setting).getIntValue();
	}
	
	public static double doubleFromSetting(SettingsModel setting) {
		// TODO Auto-generated method stub
		return 0;
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
