package uk.ac.sanger.mig.xray.trendline;

import java.util.Arrays;
import java.util.Collection;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

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
				(SettingsModelString) TrendLineNodeModel.settingsModels
						.get(TrendLineNodeModel.CFGKEY_IMAGE_COL),
				TrendLineNodeModel.CFGKEY_IMAGE_COL, 0,
				new ImgPlusColumnFilter()));

		Collection<String> c = Arrays.asList(TrendLineNodeModel.FITTING_TYPES);

		addDialogComponent(new DialogComponentStringSelection(
				(SettingsModelString) TrendLineNodeModel.settingsModels
						.get(TrendLineNodeModel.CFGKEY_FIT_TYPE),
				TrendLineNodeModel.CFGKEY_FIT_TYPE, c));
		
		addDialogComponent(new DialogComponentNumber(
				(SettingsModelInteger) TrendLineNodeModel.settingsModels
						.get(TrendLineNodeModel.CFGKEY_POWER_DEG),
				TrendLineNodeModel.CFGKEY_POWER_DEG, 1, 5));

	}
}
