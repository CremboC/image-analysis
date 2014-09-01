package uk.ac.sanger.mig.bitmaskcropper;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnFilter;
import org.knime.knip.base.data.img.ImgPlusCell;

/**
 * <code>NodeDialog</code> for the "BitmaskCropper" Node. Takes a bitmask
 * (image) and an image and crops the image according to the bitmask.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Paulius @ WTSI
 */
public class BitmaskCropperNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring BitmaskCropper node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected BitmaskCropperNodeDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) BitmaskCropperNodeModel.settingsModels
						.get(BitmaskCropperNodeModel.CFGKEY_IMAGE_COL),
				BitmaskCropperNodeModel.CFGKEY_IMAGE_COL, 0, new ColumnFilter() {
					
					@Override
					public boolean includeColumn(DataColumnSpec colSpec) {
						return colSpec.getType() == ImgPlusCell.TYPE ? true : false;
					}
					
					@Override
					public String allFilteredMsg() {
						return "Missing Image Column";
					}
				}));
		
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) BitmaskCropperNodeModel.settingsModels
				.get(BitmaskCropperNodeModel.CFGKEY_BITMASK_COL),
				BitmaskCropperNodeModel.CFGKEY_BITMASK_COL, 0, new ColumnFilter() {
					
					@Override
					public boolean includeColumn(DataColumnSpec colSpec) {
						return colSpec.getType() == ImgPlusCell.TYPE ? true : false;
					}
					
					@Override
					public String allFilteredMsg() {
						return "Missing Bitmask Column";
					}
				}));

	}
}
