package uk.ac.sanger.mig.xray.activecontour;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.nodetools.filters.ImgPlusColumnFilter;

/**
 * <code>NodeDialog</code> for the "ActiveContour" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class ActiveContourNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ActiveContour node.
     */
    protected ActiveContourNodeDialog() {
    	addDialogComponent(new DialogComponentColumnNameSelection(
    			(SettingsModelString) ActiveContourNodeModel.settingsModels
    			.get(ActiveContourNodeModel.CFGKEY_IMAGE_COL),
    			ActiveContourNodeModel.CFGKEY_IMAGE_COL, 0,
    			new ImgPlusColumnFilter()));
    	
    	addDialogComponent(new DialogComponentColumnNameSelection(
    			(SettingsModelString) ActiveContourNodeModel.settingsModels
    			.get(ActiveContourNodeModel.CFGKEY_GRADX_COL),
    			ActiveContourNodeModel.CFGKEY_GRADX_COL, 0,
    			new ImgPlusColumnFilter()));
    	
    	addDialogComponent(new DialogComponentColumnNameSelection(
    			(SettingsModelString) ActiveContourNodeModel.settingsModels
    			.get(ActiveContourNodeModel.CFGKEY_GRADY_COL),
    			ActiveContourNodeModel.CFGKEY_GRADY_COL, 0,
    			new ImgPlusColumnFilter()));
    }
}

