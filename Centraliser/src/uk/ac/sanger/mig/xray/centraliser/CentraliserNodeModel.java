/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Centraliser.
 * 
 * Centraliser is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.xray.centraliser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.centraliser.utils.Centraliser;

/**
 * This is the model implementation of Centraliser. Centres a blob of white
 * pixels to the centre of the image.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class CentraliserNodeModel extends GenericNodeModel {

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
	static final String CFGKEY_CENTROID_X = "Centroid X Column";
	static final String CFGKEY_CENTROID_Y = "Centroid Y Column";
	
	static final String DEFAULT_CENTROIDX_COL = "WeightedCentroid Dim 1";
	static final String DEFAULT_CENTROIDY_COL = "WeightedCentroid Dim 2";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	private final Map<String, SettingsModel> settingsModels;

	/**
	 * Constructor for the node model.
	 */
	protected CentraliserNodeModel() {
		super(1, 1);
		
		settingsModels = new HashMap<String, SettingsModel>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));
		
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
		
		final DataTableSpec specs = inData[INPORT_0].getDataTableSpec();

		indices = Utils.indices(specs);

		final OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES,
				exec);
		
		final Centraliser c = new Centraliser();

		final Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			final DataRow row = iter.next();

			// get the image according to the setting
			final ImgPlus<BitType> ip = (ImgPlus<BitType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);
			
			final double centroidX = doubleBySetting(row, CFGKEY_CENTROID_X);
			final double centroidY = doubleBySetting(row, CFGKEY_CENTROID_Y);

			out.open(row.getKey());

			// crop out the required part and add it to the output table
			out.add(c.process(ip, centroidX, centroidY));

			out.close();
		}

		// return the output table on the first (0th) outport
		return new BufferedDataTable[] { out.getOutputTable() };
	}

}
