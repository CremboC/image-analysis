package uk.ac.sanger.mig.boundingbox;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "BoundingBox" Node.
 * Calculated a bounding box according to a threshold of pixels in a row/column
 *
 * @author Wellcome Trust Sanger Institute
 */
public class BoundingBoxNodeFactory<T extends RealType<T> & NativeType<T>> 
        extends NodeFactory<BoundingBoxNodeModel<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBoxNodeModel<T> createNodeModel() {
        return new BoundingBoxNodeModel<T>();
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
    public NodeView<BoundingBoxNodeModel<T>> createNodeView(final int viewIndex,
            final BoundingBoxNodeModel<T> nodeModel) {
        return new BoundingBoxNodeView<T>(nodeModel);
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

