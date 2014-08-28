package uk.ac.sanger.mig.bitmaskcropper;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "BitmaskCropper" Node.
 * Takes a bitmask (image) and an image and crops the image according to the bitmask.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Paulius @ WTSI
 */
public class BitmaskCropperNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring BitmaskCropper node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected BitmaskCropperNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    BitmaskCropperNodeModel.CFGKEY_COUNT,
                    BitmaskCropperNodeModel.DEFAULT_COUNT,
                    Integer.MIN_VALUE, Integer.MAX_VALUE),
                    "Counter:", /*step*/ 1, /*componentwidth*/ 5));
                    
    }
}

