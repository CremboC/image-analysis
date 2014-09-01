package uk.ac.sanger.mig.boundingbox;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnFilter;
import org.knime.knip.base.data.img.ImgPlusCell;

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
				(SettingsModelString) BoundingBoxNodeModel.settingsModels
						.get(BoundingBoxNodeModel.CFGKEY_IMAGE_COL),
				BoundingBoxNodeModel.CFGKEY_IMAGE_COL, 0, new ColumnFilter() {

					@Override
					public boolean includeColumn(DataColumnSpec colSpec) {
						return colSpec.getType() == ImgPlusCell.TYPE ? true
								: false;
					}

					@Override
					public String allFilteredMsg() {
						return "Missing Image Column";
					}
				}));

		addDialogComponent(new DialogComponentString(
				(SettingsModelString) BoundingBoxNodeModel.settingsModels
						.get(BoundingBoxNodeModel.CFGKEY_COL_THRESHOLD),
				BoundingBoxNodeModel.CFGKEY_COL_THRESHOLD));
		
		addDialogComponent(new DialogComponentString(
				(SettingsModelString) BoundingBoxNodeModel.settingsModels
						.get(BoundingBoxNodeModel.CFGKEY_ROW_THRESHOLD),
				BoundingBoxNodeModel.CFGKEY_ROW_THRESHOLD));

	}
}
