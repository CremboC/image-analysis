package uk.ac.sanger.mig.xray.trendlinecropper;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "TrendLineCropper" Node.
 * Crops out a region following the trendling. Use the left and right margin to specify how many pixels left and right of the trendline will be removed. * n * nDue to the nature of trend lines, parameters for starting row and ending row are also provided.
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

    }
}

