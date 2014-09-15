package uk.ac.sanger.mig.xray.peakcounter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PeakCounter" Node.
 * Scans through the image and using buckets counts number of peaks. * nWhen it finds a white pixel, it creates a bucket. Continue on scanning, every following white pixel is added to the same bucket. When a black pixel is reached, the bucket is closed.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class PeakCounterNodeFactory 
        extends NodeFactory<PeakCounterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PeakCounterNodeModel createNodeModel() {
        return new PeakCounterNodeModel();
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
    public NodeView<PeakCounterNodeModel> createNodeView(final int viewIndex,
            final PeakCounterNodeModel nodeModel) {
        return new PeakCounterNodeView(nodeModel);
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
        return new PeakCounterNodeDialog();
    }

}

