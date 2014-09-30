package uk.ac.sanger.mig.intervalthreshold;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "IntervalThreshold" Node.
 * Select a range of pixel values, between an upper and lower bound setting all other pixels to the background value.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class IntervalThresholdNodeFactory<T extends RealType<T> & NativeType<T>>
        extends NodeFactory<IntervalThresholdNodeModel<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IntervalThresholdNodeModel<T> createNodeModel() {
        return new IntervalThresholdNodeModel<T>();
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
    public NodeView<IntervalThresholdNodeModel<T>> createNodeView(final int viewIndex,
            final IntervalThresholdNodeModel<T> nodeModel) {
        return new IntervalThresholdNodeView<T>(nodeModel);
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

