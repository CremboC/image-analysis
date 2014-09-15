package uk.ac.sanger.mig.bitmaskcropper;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "BitmaskCropper" Node. Takes a bitmask
 * (image) and an image and crops the image according to the bitmask.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
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
				BitmaskCropperNodeModel.CFGKEY_IMAGE_COL, 0, new ImgPlusColumnFilter()));
		
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) BitmaskCropperNodeModel.settingsModels
				.get(BitmaskCropperNodeModel.CFGKEY_BITMASK_COL),
				BitmaskCropperNodeModel.CFGKEY_BITMASK_COL, 0, new ImgPlusColumnFilter()));

	}
}
