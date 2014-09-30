package uk.ac.sanger.mig.xray.centraliser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;
import uk.ac.sanger.mig.analysis.nodetools.filters.NumberColumnFilter;

/**
 * <code>NodeDialog</code> for the "Centraliser" Node.
 * Centres a blob of white pixels to the centre of the image.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class CentraliserNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring Centraliser node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected CentraliserNodeDialog() {
        super();
        
		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(CentraliserNodeModel.CFGKEY_IMAGE_COL,
						"Image"),
				CentraliserNodeModel.CFGKEY_IMAGE_COL, 0, new ImgPlusColumnFilter()));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(CentraliserNodeModel.CFGKEY_CENTROID_Y,
						CentraliserNodeModel.DEFAULT_CENTROIDY_COL),
				CentraliserNodeModel.CFGKEY_CENTROID_Y, 0, new NumberColumnFilter(
						"No Centroid Y Column?")));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(CentraliserNodeModel.CFGKEY_CENTROID_X,
						CentraliserNodeModel.DEFAULT_CENTROIDX_COL),
				CentraliserNodeModel.CFGKEY_CENTROID_X, 0, new NumberColumnFilter(
						"No Centroid X Column?")));
                    
    }
}

