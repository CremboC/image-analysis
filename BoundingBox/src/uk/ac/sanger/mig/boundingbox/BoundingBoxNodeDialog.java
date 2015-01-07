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

import java.util.Arrays;
import java.util.Collection;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "BoundingBox" Node. Calculated a bounding box
 * according to a threshold of pixels in a row/column
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class BoundingBoxNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring BoundingBox node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected BoundingBoxNodeDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(
						BoundingBoxNodeModel.CFGKEY_IMAGE_COL, BoundingBoxNodeModel.DEFAULT_IMAGE_COL),
				BoundingBoxNodeModel.CFGKEY_IMAGE_COL, 0, new ImgPlusColumnFilter()));
		
		Collection<String> rets = Arrays.asList(BoundingBoxNodeModel.RETURN_TYPES);

		addDialogComponent(new DialogComponentStringSelection(
				new SettingsModelString(
						BoundingBoxNodeModel.CFGKEY_RET_TYPE, "Original"),
				BoundingBoxNodeModel.CFGKEY_RET_TYPE, rets));

		addDialogComponent(new DialogComponentString(
				new SettingsModelString(
						BoundingBoxNodeModel.CFGKEY_ROW_THRESHOLD, "0,0"),
				BoundingBoxNodeModel.CFGKEY_COL_THRESHOLD));
		
		addDialogComponent(new DialogComponentString(
				new SettingsModelString(
						BoundingBoxNodeModel.CFGKEY_COL_THRESHOLD, "0,0"),
				BoundingBoxNodeModel.CFGKEY_ROW_THRESHOLD));

	}
}
