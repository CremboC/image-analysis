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

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;
import uk.ac.sanger.mig.analysis.nodetools.filters.NumberColumnFilter;

/**
 * <code>NodeDialog</code> for the "Aligner" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author pi1
 */
public class AlignerNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring Aligner node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected AlignerNodeDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(AlignerNodeModel.CFGKEY_IMAGE_COL,
						AlignerNodeModel.DEFAULT_IMAGE_COL),
				AlignerNodeModel.CFGKEY_IMAGE_COL, 0, new ImgPlusColumnFilter()));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(AlignerNodeModel.CFGKEY_CENTROID_Y,
						AlignerNodeModel.DEFAULT_CENTROIDY_COL),
				AlignerNodeModel.CFGKEY_CENTROID_Y, 0, new NumberColumnFilter(
						"No Centroid Y Column?")));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(AlignerNodeModel.CFGKEY_CENTROID_X,
						AlignerNodeModel.DEFAULT_CENTROIDX_COL),
				AlignerNodeModel.CFGKEY_CENTROID_X, 0, new NumberColumnFilter(
						"No Centroid X Column?")));

	}
}
