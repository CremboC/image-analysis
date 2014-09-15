package uk.ac.sanger.mig.xray.peakcounter;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "PeakCounter" Node. Scans through the image
 * and using buckets counts number of peaks. nWhen it finds a white pixel, it
 * creates a bucket. Continue on scanning, every following white pixel is added
 * to the same bucket. When a black pixel is reached, the bucket is closed.
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class PeakCounterNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the PeakCounter node.
	 */
	protected PeakCounterNodeDialog() {
		addDialogComponent(new DialogComponentColumnNameSelection(
				(SettingsModelString) PeakCounterNodeModel.settingsModels
						.get(PeakCounterNodeModel.CFGKEY_IMAGE_COL),
				PeakCounterNodeModel.CFGKEY_IMAGE_COL, 0,
				new ImgPlusColumnFilter()));

		addDialogComponent(new DialogComponentNumber(
				(SettingsModelInteger) PeakCounterNodeModel.settingsModels
						.get(PeakCounterNodeModel.CFGKEY_BUC_THRESH),
				PeakCounterNodeModel.CFGKEY_BUC_THRESH, 1, 5));
	}
}
