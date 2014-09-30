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
