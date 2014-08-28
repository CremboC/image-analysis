package uk.ac.sanger.mig.aligner;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

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

		addDialogComponent(new DialogComponentString(
				AlignerNodeModel.m_centroidx_name,
				AlignerNodeModel.CFGKEY_CENTROID_X));
		
		addDialogComponent(new DialogComponentString(
				AlignerNodeModel.m_centroidy_name,
				AlignerNodeModel.CFGKEY_CENTROID_Y));
		
		addDialogComponent(new DialogComponentString(
				AlignerNodeModel.m_image_column,
				AlignerNodeModel.CFGKEY_COLUMN));

	}
}
