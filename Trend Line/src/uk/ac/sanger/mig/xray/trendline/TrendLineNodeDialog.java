/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Trend Line.
 * 
 * Trend Line is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.xray.trendline;

import java.util.Arrays;
import java.util.Collection;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.maths.trendline.Fitting;
import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "TrendLine" Node. Takes an image and using
 * its bright spots finds finds the trend line. nAccuracy may be adjusted using
 * the settings dialog.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the TrendLine node.
	 */
	protected TrendLineNodeDialog() {
		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(
						TrendLineNodeModel.CFGKEY_IMAGE_COL, "Image"),
				TrendLineNodeModel.CFGKEY_IMAGE_COL, 0,
				new ImgPlusColumnFilter()));
		
		Collection<String> rets = Arrays.asList(TrendLineNodeModel.RETURN_TYPES);

		addDialogComponent(new DialogComponentStringSelection(
				new SettingsModelString(
						TrendLineNodeModel.CFGKEY_RET_TYPE, "Original"),

				TrendLineNodeModel.CFGKEY_RET_TYPE, rets));

		Collection<String> fits = Arrays.asList(TrendLineNodeModel.FITTING_TYPES);

		addDialogComponent(new DialogComponentStringSelection(
				new SettingsModelString(
						TrendLineNodeModel.CFGKEY_FIT_TYPE, Fitting.EXP.toString()),
				TrendLineNodeModel.CFGKEY_FIT_TYPE, fits));
		
		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
						TrendLineNodeModel.CFGKEY_POLY_DEG, 0),
				TrendLineNodeModel.CFGKEY_POLY_DEG, 1, 5));

	}
}
