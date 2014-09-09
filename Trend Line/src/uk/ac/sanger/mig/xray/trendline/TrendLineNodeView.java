package uk.ac.sanger.mig.xray.trendline;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "TrendLine" Node.
 * Takes an image and using its bright spots finds finds the trend line. * nAccuracy may be adjusted using the settings dialog.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineNodeView extends NodeView<TrendLineNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link TrendLineNodeModel})
     */
    protected TrendLineNodeView(final TrendLineNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

