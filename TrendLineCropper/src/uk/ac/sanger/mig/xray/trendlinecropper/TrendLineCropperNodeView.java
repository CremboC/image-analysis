package uk.ac.sanger.mig.xray.trendlinecropper;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "TrendLineCropper" Node.
 * Crops out a region following the trendling. Use the left and right margin to specify how many pixels left and right of the trendline will be removed. * n * nDue to the nature of trend lines, parameters for starting row and ending row are also provided.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineCropperNodeView<T extends RealType<T> & NativeType<T>> extends NodeView<TrendLineCropperNodeModel<T>> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link TrendLineCropperNodeModel})
     */
    protected TrendLineCropperNodeView(final TrendLineCropperNodeModel<T> nodeModel) {
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

