package uk.ac.sanger.mig.regioncropper;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RegionCropper" Node.
 * Crops images according to the given the upper, right, lower and left bounds. Everything outside these boundaries is deleted.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class RegionCropperNodeFactory<T extends RealType<T> & NativeType<T>> 
        extends NodeFactory<RegionCropperNodeModel<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RegionCropperNodeModel<T> createNodeModel() {
        return new RegionCropperNodeModel<T>();
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
    public NodeView<RegionCropperNodeModel<T>> createNodeView(final int viewIndex,
            final RegionCropperNodeModel<T> nodeModel) {
        return new RegionCropperNodeView<T>(nodeModel);
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

