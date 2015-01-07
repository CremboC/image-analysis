/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Bounding Box.
 * 
 * Bounding Box is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.boundingbox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.analysis.nodetools.enums.ReturnType;
import uk.ac.sanger.mig.boundingbox.utils.BoundingBox;

/**
 * This is the model implementation of BoundingBox. Calculated a bounding box
 * according to a threshold of pixels in a row/column
 * 
 * @author Wellcome Trust Sanger Institute
 * 
 * @param <T>
 */
public class BoundingBoxNodeModel<T extends RealType<T> & NativeType<T>>
		extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMNS = { "Image", "Upper Boundary",
			"Lower Boundary", "Left Boundary", "Right Boundary" };

	/** Column types */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE,
			IntCell.TYPE, IntCell.TYPE, IntCell.TYPE, IntCell.TYPE };

	private static final String CENTROID_COL_X = "WeightedCentroid Dim 1";
	private static final String CENTROID_COL_Y = "WeightedCentroid Dim 2";

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_ROW_THRESHOLD = "Row Threshold";
	static final String CFGKEY_COL_THRESHOLD = "Column Threshold";
	static final String CFGKEY_RET_TYPE = "Return Type";

	static final String DEFAULT_IMAGE_COL = "Image";

	static final String[] RETURN_TYPES = ReturnType.names();

	private final Map<String, SettingsModel> settingsModels;

	/**
	 * Constructor for the node model.
	 */
	protected BoundingBoxNodeModel() {
		super(1, 1);
		
		settingsModels = new HashMap<String, SettingsModel>();
		
		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, DEFAULT_IMAGE_COL));
		
		settingsModels.put(CFGKEY_RET_TYPE, new SettingsModelString(
				CFGKEY_RET_TYPE, "Original"));
		
		settingsModels.put(CFGKEY_ROW_THRESHOLD, new SettingsModelString(
				CFGKEY_ROW_THRESHOLD, "0,0"));
		
		settingsModels.put(CFGKEY_COL_THRESHOLD, new SettingsModelString(
				CFGKEY_COL_THRESHOLD, "0,0"));
		
		setSettings(settingsModels);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		indices = Utils.indices(inData[INPORT_0].getDataTableSpec());

		String rowThresholds = stringFromSetting(CFGKEY_ROW_THRESHOLD);
		String colThresholds = stringFromSetting(CFGKEY_COL_THRESHOLD);

		ReturnType retType = ReturnType
				.whereName(stringFromSetting(CFGKEY_RET_TYPE));

		OutputHelper out = new OutputHelper(COLUMNS, COLUMN_TYPES, exec);

		BoundingBox<T> box = new BoundingBox<T>(Utils.split(rowThresholds),
				Utils.split(colThresholds), retType);

		Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			DataRow row = iter.next();

			ImgPlus<T> ip = (ImgPlus<T>) imageBySetting(row, CFGKEY_IMAGE_COL);

			int centroidX = (int) Utils.doubleByIndex(row,
					indices.get(CENTROID_COL_X));
			int centroidY = (int) Utils.doubleByIndex(row,
					indices.get(CENTROID_COL_Y));

			int[] boundaries = box.find(ip, centroidX, centroidY);

			out.open(row.getKey());

			out.add((retType == ReturnType.ORIG) ? ip : box.image());
			out.add(boundaries);

			out.close();
		}

		return new BufferedDataTable[] { out.getOutputTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		for (int i = 0; i < inSpecs[INPORT_0].getNumColumns(); i++) {
			DataColumnSpec spec = inSpecs[INPORT_0].getColumnSpec(i);

			if (spec.getType() == ImgPlusCell.TYPE) {

				SettingsModel sm = settingsModels.get(CFGKEY_IMAGE_COL);
				((SettingsModelColumnName) sm).setStringValue(spec.getName());

				break;
			}
		}

		return new DataTableSpec[] { null };
	}
}
