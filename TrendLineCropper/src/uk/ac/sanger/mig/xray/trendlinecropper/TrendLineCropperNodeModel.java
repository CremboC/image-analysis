/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Trend Line Cropper.
 * 
 * Trend Line Cropper is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.xray.trendlinecropper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.trendlinecropper.utils.TrendCropper;

/**
 * This is the model implementation of TrendLineCropper. Crops out a region
 * following the trendling. Use the left and right margin to specify how many
 * pixels left and right of the trendline will be removed. Due to the nature
 * of trend lines, parameters for starting row and ending row are also provided.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineCropperNodeModel<T extends RealType<T> & NativeType<T>>
		extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "Image" };

	/** Column types of the schema */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_START_ROW = "Start at Row";
	static final String CFGKEY_END_ROW = "End at Row";
	static final String CFGKEY_LEFT_MARGIN = "Left Margin";
	static final String CFGKEY_RIGHT_MARGIN = "Right Margin";

	static final String CFGKEY_CROPTOP = "Crop Top";
	static final String CFGKEY_CT_LEFT_MARGIN = "Crop Top Left Margin";
	static final String CFGKEY_CT_RIGHT_MARGIN = "Crop Top Right Margin";

	static final String COEF_COL = "Coeficients";
	static final String TREND_COL = "Trend Type";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	private final Map<String, SettingsModel> settingsModels;

	/**
	 * Constructor for the node model.
	 */
	protected TrendLineCropperNodeModel() {
		super(1, 1);
		
		settingsModels = new HashMap<>();
		
		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));

		settingsModels.put(CFGKEY_START_ROW, new SettingsModelInteger(
				CFGKEY_START_ROW, 0));

		settingsModels.put(CFGKEY_END_ROW, new SettingsModelInteger(
				CFGKEY_END_ROW, -1));

		settingsModels.put(CFGKEY_LEFT_MARGIN, new SettingsModelInteger(
				CFGKEY_LEFT_MARGIN, 1));

		settingsModels.put(CFGKEY_RIGHT_MARGIN, new SettingsModelInteger(
				CFGKEY_RIGHT_MARGIN, 1));

		settingsModels.put(CFGKEY_CROPTOP, new SettingsModelBoolean(
				CFGKEY_CROPTOP, false));

		settingsModels.put(CFGKEY_CT_LEFT_MARGIN, new SettingsModelInteger(
				CFGKEY_CT_LEFT_MARGIN, 1));

		settingsModels.put(CFGKEY_CT_RIGHT_MARGIN, new SettingsModelInteger(
				CFGKEY_CT_RIGHT_MARGIN, 1));
		
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

		final OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES,
				exec);

		final int leftMargin = intFromSetting(CFGKEY_LEFT_MARGIN);
		final int rightMargin = intFromSetting(CFGKEY_RIGHT_MARGIN);

		final int startRow = intFromSetting(CFGKEY_START_ROW);
		final int endRow = intFromSetting(CFGKEY_END_ROW);

		final int topLeftMargin = intFromSetting(CFGKEY_CT_LEFT_MARGIN);
		final int topRightMargin = intFromSetting(CFGKEY_CT_RIGHT_MARGIN);
		
		final boolean cropTop = boolFromSetting(CFGKEY_CROPTOP);

		final TrendCropper<T> cropper = new TrendCropper<T>(leftMargin,
				rightMargin, startRow, endRow, cropTop, topLeftMargin, topRightMargin);

		final Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			final DataRow row = iter.next();

			// get the image according to the setting
			final ImgPlus<T> ip = (ImgPlus<T>) imageBySetting(row,
					CFGKEY_IMAGE_COL);
			final String coefs = stringBySetting(row, COEF_COL);
			final String trend = stringBySetting(row, TREND_COL);

			out.open(row.getKey());

			// crop out the required part and add it to the output table
			out.add(cropper.process(ip, coefs, trend));

			out.close();
		}

		// return the output table on the first (0th) outport
		return new BufferedDataTable[] { out.getOutputTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		// TODO: generated method stub
		return new DataTableSpec[] { null };
	}
}
