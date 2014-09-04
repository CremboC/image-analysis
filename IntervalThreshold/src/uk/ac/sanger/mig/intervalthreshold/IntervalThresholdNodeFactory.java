package uk.ac.sanger.mig.intervalthreshold;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "IntervalThreshold" Node.
 * Select a range of pixel values, between an upper and lower bound setting all other pixels to the background value.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class IntervalThresholdNodeFactory 
        extends NodeFactory<IntervalThresholdNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IntervalThresholdNodeModel createNodeModel() {
        return new IntervalThresholdNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<IntervalThresholdNodeModel> createNodeView(final int viewIndex,
            final IntervalThresholdNodeModel nodeModel) {
        return new IntervalThresholdNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new IntervalThresholdNodeDialog();
    }

}

