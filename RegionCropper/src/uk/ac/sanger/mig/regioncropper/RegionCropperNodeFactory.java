package uk.ac.sanger.mig.regioncropper;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RegionCropper" Node.
 * Crops images according to the given the upper, right, lower and left bounds. Everything outside these boundaries is deleted.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class RegionCropperNodeFactory 
        extends NodeFactory<RegionCropperNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RegionCropperNodeModel createNodeModel() {
        return new RegionCropperNodeModel();
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
    public NodeView<RegionCropperNodeModel> createNodeView(final int viewIndex,
            final RegionCropperNodeModel nodeModel) {
        return new RegionCropperNodeView(nodeModel);
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
        return new RegionCropperNodeDialog();
    }

}

