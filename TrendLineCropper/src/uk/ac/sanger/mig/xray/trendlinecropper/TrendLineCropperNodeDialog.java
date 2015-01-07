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

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "TrendLineCropper" Node. Crops out a region
 * following the trendling. Use the left and right margin to specify how many
 * pixels left and right of the trendline will be removed. n nDue to the nature
 * of trend lines, parameters for starting row and ending row are also provided.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineCropperNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the TrendLineCropper node.
	 */
	protected TrendLineCropperNodeDialog() {

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(
					TrendLineCropperNodeModel.CFGKEY_IMAGE_COL, "Image"),
				TrendLineCropperNodeModel.CFGKEY_IMAGE_COL, 0,
				new ImgPlusColumnFilter()));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
					TrendLineCropperNodeModel.CFGKEY_START_ROW, 0),
				TrendLineCropperNodeModel.CFGKEY_START_ROW, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
					TrendLineCropperNodeModel.CFGKEY_END_ROW, -1),
				TrendLineCropperNodeModel.CFGKEY_END_ROW, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
					TrendLineCropperNodeModel.CFGKEY_LEFT_MARGIN, 1),
				TrendLineCropperNodeModel.CFGKEY_LEFT_MARGIN, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
					TrendLineCropperNodeModel.CFGKEY_RIGHT_MARGIN, 1),
				TrendLineCropperNodeModel.CFGKEY_RIGHT_MARGIN, 1, 5));

		addDialogComponent(new DialogComponentBoolean(
				new SettingsModelBoolean(
					TrendLineCropperNodeModel.CFGKEY_CROPTOP, false),
				TrendLineCropperNodeModel.CFGKEY_CROPTOP));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
					TrendLineCropperNodeModel.CFGKEY_CT_LEFT_MARGIN, 1),
				TrendLineCropperNodeModel.CFGKEY_CT_LEFT_MARGIN, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
					TrendLineCropperNodeModel.CFGKEY_CT_RIGHT_MARGIN, 1),
				TrendLineCropperNodeModel.CFGKEY_CT_RIGHT_MARGIN, 1, 5));
	}
}
