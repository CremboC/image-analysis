package uk.ac.sanger.mig.regioncropper;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;
import uk.ac.sanger.mig.analysis.nodetools.filters.IntColumnFilter;

/**
 * <code>NodeDialog</code> for the "RegionCropper" Node. Crops images according
 * to the given the upper, right, lower and left bounds. Everything outside
 * these boundaries is deleted.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class RegionCropperNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring RegionCropper node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected RegionCropperNodeDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) RegionCropperNodeModel.settingsModels
						.get(RegionCropperNodeModel.CFGKEY_IMAGE_COL),
				RegionCropperNodeModel.CFGKEY_IMAGE_COL, 0, new ImgPlusColumnFilter()));
		
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) RegionCropperNodeModel.settingsModels
						.get(RegionCropperNodeModel.CFGKEY_UPBOUND_COL),
				RegionCropperNodeModel.CFGKEY_UPBOUND_COL, 0, new IntColumnFilter()));
		
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) RegionCropperNodeModel.settingsModels
						.get(RegionCropperNodeModel.CFGKEY_RIGHTBOUND_COL),
				RegionCropperNodeModel.CFGKEY_RIGHTBOUND_COL, 0, new IntColumnFilter()));
		
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) RegionCropperNodeModel.settingsModels
						.get(RegionCropperNodeModel.CFGKEY_LOWBOUND_COL),
				RegionCropperNodeModel.CFGKEY_LOWBOUND_COL, 0, new IntColumnFilter()));
		
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) RegionCropperNodeModel.settingsModels
						.get(RegionCropperNodeModel.CFGKEY_LEFTBOUND_COL),
				RegionCropperNodeModel.CFGKEY_LEFTBOUND_COL, 0, new IntColumnFilter()));
	}
}
