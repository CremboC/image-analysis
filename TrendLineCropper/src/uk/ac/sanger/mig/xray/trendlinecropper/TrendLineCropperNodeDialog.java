package uk.ac.sanger.mig.xray.trendlinecropper;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

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
				(SettingsModelString) TrendLineCropperNodeModel.settingsModels
						.get(TrendLineCropperNodeModel.CFGKEY_IMAGE_COL),
				TrendLineCropperNodeModel.CFGKEY_IMAGE_COL, 0,
				new ImgPlusColumnFilter()));

		addDialogComponent(new DialogComponentNumber(
				(SettingsModelInteger) TrendLineCropperNodeModel.settingsModels
						.get(TrendLineCropperNodeModel.CFGKEY_START_ROW),
				TrendLineCropperNodeModel.CFGKEY_START_ROW, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				(SettingsModelInteger) TrendLineCropperNodeModel.settingsModels
						.get(TrendLineCropperNodeModel.CFGKEY_END_ROW),
				TrendLineCropperNodeModel.CFGKEY_END_ROW, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				(SettingsModelInteger) TrendLineCropperNodeModel.settingsModels
						.get(TrendLineCropperNodeModel.CFGKEY_LEFT_MARGIN),
				TrendLineCropperNodeModel.CFGKEY_LEFT_MARGIN, 1, 5));

		addDialogComponent(new DialogComponentNumber(
				(SettingsModelInteger) TrendLineCropperNodeModel.settingsModels
						.get(TrendLineCropperNodeModel.CFGKEY_RIGHT_MARGIN),
				TrendLineCropperNodeModel.CFGKEY_RIGHT_MARGIN, 1, 5));

	}
}
