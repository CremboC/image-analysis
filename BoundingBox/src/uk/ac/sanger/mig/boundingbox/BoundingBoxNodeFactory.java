package uk.ac.sanger.mig.boundingbox;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "BoundingBox" Node.
 * Calculated a bounding box according to a threshold of pixels in a row/column
 *
 * @author Wellcome Trust Sanger Institute
 */
public class BoundingBoxNodeFactory 
        extends NodeFactory<BoundingBoxNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBoxNodeModel createNodeModel() {
        return new BoundingBoxNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<BoundingBoxNodeModel> createNodeView(final int viewIndex,
            final BoundingBoxNodeModel nodeModel) {
        return new BoundingBoxNodeView(nodeModel);
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
        return new BoundingBoxNodeDialog();
    }

}

