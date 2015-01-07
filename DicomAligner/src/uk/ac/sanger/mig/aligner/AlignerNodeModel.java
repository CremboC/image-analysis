/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Aligner.
 * 
 * Aligner is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.aligner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.base.node.NodeUtils;

import uk.ac.sanger.mig.aligner.helpers.Aligner;
import uk.ac.sanger.mig.aligner.helpers.Denominator;
import uk.ac.sanger.mig.aligner.helpers.MatrixHelper;
import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.Image;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;

public class AlignerNodeModel extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "thetamin" };

	/** Column types */
	private final static DataType[] COLUMN_TYPES = { DoubleCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_CENTROID_X = "Centroid X Column";
	static final String CFGKEY_CENTROID_Y = "Centroid Y Column";
	static final String CFGKEY_IMAGE_COL = "Image Column";

	static final String DEFAULT_IMAGE_COL = "Image";
	static final String DEFAULT_CENTROIDX_COL = "WeightedCentroid Dim 1";
	static final String DEFAULT_CENTROIDY_COL = "WeightedCentroid Dim 2";

	protected static final int IN_PORTS = 1;
	protected static final int OUT_PORTS = 1;

	private final Map<String, SettingsModel> settingsModels;

	protected AlignerNodeModel() {
		super(IN_PORTS, OUT_PORTS);
		
		settingsModels = new HashMap<String, SettingsModel>();

		// Column name which stores the image
		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, DEFAULT_IMAGE_COL));

		// Column name which stores the x coord of the centroid
		settingsModels.put(CFGKEY_CENTROID_X, new SettingsModelColumnName(
				CFGKEY_CENTROID_X, DEFAULT_CENTROIDX_COL));

		// Column name which stores the y coord of the centroid
		settingsModels.put(CFGKEY_CENTROID_Y, new SettingsModelColumnName(
				CFGKEY_CENTROID_Y, DEFAULT_CENTROIDY_COL));
		
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

		ensureImageExistance(indices, settingsModels.get(CFGKEY_IMAGE_COL),
				inData[INPORT_0].getDataTableSpec());

		final OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES,
				exec);

		// get data from the first port
		final Iterator<DataRow> iter = inData[INPORT_0].iterator();

		// have to ensure input image is of bit type for now
		// get the image from the input, goes thru the rows and finds
		// the image cell of each row
		while (iter.hasNext()) {
			final DataRow row = iter.next();

			final double centroidX = doubleBySetting(row, CFGKEY_CENTROID_X);
			final double centroidY = doubleBySetting(row, CFGKEY_CENTROID_Y);

			final ImgPlus<BitType> ip = (ImgPlus<BitType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);

			int[] x = MatrixHelper.x((int) ip.dimension(Image.COL),
					(int) ip.dimension(Image.ROW));
			int[] y = MatrixHelper.y((int) ip.dimension(Image.COL),
					(int) ip.dimension(Image.ROW));

			x = MatrixHelper.deductFromEach(x, centroidX);
			y = MatrixHelper.deductFromEach(y, centroidY);

			final Denominator denomCalculator = new Denominator(x, y, ip);
			final Aligner aligner = new Aligner(denomCalculator);

			final double theta = aligner.thetamin();

			out.open(row.getKey());

			out.add(theta);

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

		final DataTableSpec specs = inSpecs[INPORT_0];

		final SettingsModelString centroidXSetting = (SettingsModelString) settingsModels
				.get(CFGKEY_CENTROID_X);
		final SettingsModelString centroidYSetting = (SettingsModelString) settingsModels
				.get(CFGKEY_CENTROID_Y);

		if (specs.findColumnIndex(centroidXSetting.getStringValue()) == -1) {
			throw new InvalidSettingsException(
					"Centroid X column doesn't exist in the input table.");
		}

		if (specs.findColumnIndex(centroidYSetting.getStringValue()) == -1) {
			throw new InvalidSettingsException(
					"Centroid Y column doesn't exist in the input table.");
		}

		return new DataTableSpec[] { null };
	}

	/**
	 * Ensure an image column exists to avoid null pointer exception. Basically
	 * this auto-config.
	 * 
	 * @param indices
	 * @param settingsModel
	 * @param dataTableSpec
	 */
	private void ensureImageExistance(Map<String, Integer> indices,
			SettingsModel settingsModel, DataTableSpec dataTableSpec) {

		if (indices.get(CFGKEY_IMAGE_COL) != null) {
			return;
		}

		final SettingsModelString imageSetting = (SettingsModelString) settingsModel;

		final int imgIndex = NodeUtils.autoOptionalColumnSelection(
				dataTableSpec, imageSetting, ImgPlusValue.class);

		indices.remove(DEFAULT_IMAGE_COL);
		indices.put(DEFAULT_IMAGE_COL, imgIndex);
	}
}
