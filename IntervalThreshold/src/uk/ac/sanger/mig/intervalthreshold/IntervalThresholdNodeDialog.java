package uk.ac.sanger.mig.intervalthreshold;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "IntervalThreshold" Node. Select a range of
 * pixel values, between an upper and lower bound setting all other pixels to
 * the background value.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class IntervalThresholdNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the IntervalThreshold node.
	 */
	protected IntervalThresholdNodeDialog() {

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(
						IntervalThresholdNodeModel.CFGKEY_IMAGE_COL, "Image"),
				IntervalThresholdNodeModel.CFGKEY_IMAGE_COL, 0,
				new ImgPlusColumnFilter()));

		addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
				IntervalThresholdNodeModel.CFGKEY_UPP_THRESH, 0),
				IntervalThresholdNodeModel.CFGKEY_UPP_THRESH, 0));

		addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
				IntervalThresholdNodeModel.CFGKEY_LOW_THRESH, 0),
				IntervalThresholdNodeModel.CFGKEY_LOW_THRESH, 0));

		addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
				IntervalThresholdNodeModel.CFGKEY_BACKGROUND_VAL, 0),
				IntervalThresholdNodeModel.CFGKEY_BACKGROUND_VAL, 0));

	}
}
