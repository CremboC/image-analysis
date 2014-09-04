package uk.ac.sanger.mig.intervalthreshold;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "IntervalThreshold" Node.
 * Select a range of pixel values, between an upper and lower bound setting all other pixels to the background value.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class IntervalThresholdNodeView extends NodeView<IntervalThresholdNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link IntervalThresholdNodeModel})
     */
    protected IntervalThresholdNodeView(final IntervalThresholdNodeModel nodeModel) {
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

