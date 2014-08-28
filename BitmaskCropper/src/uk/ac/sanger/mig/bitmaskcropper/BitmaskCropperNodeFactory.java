package uk.ac.sanger.mig.bitmaskcropper;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "BitmaskCropper" Node.
 * Takes a bitmask (image) and an image and crops the image according to the bitmask.
 *
 * @author Paulius @ WTSI
 */
public class BitmaskCropperNodeFactory 
        extends NodeFactory<BitmaskCropperNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BitmaskCropperNodeModel createNodeModel() {
        return new BitmaskCropperNodeModel();
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
    public NodeView<BitmaskCropperNodeModel> createNodeView(final int viewIndex,
            final BitmaskCropperNodeModel nodeModel) {
        return new BitmaskCropperNodeView(nodeModel);
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
        return new BitmaskCropperNodeDialog();
    }

}

