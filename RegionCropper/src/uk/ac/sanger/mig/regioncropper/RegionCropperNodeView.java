package uk.ac.sanger.mig.regioncropper;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RegionCropper" Node.
 * Crops images according to the given the upper, right, lower and left bounds. Everything outside these boundaries is deleted.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class RegionCropperNodeView<T extends RealType<T> & NativeType<T>> extends NodeView<RegionCropperNodeModel<T>> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RegionCropperNodeModel})
     */
    protected RegionCropperNodeView(final RegionCropperNodeModel<T> nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        RegionCropperNodeModel<T> nodeModel = 
            (RegionCropperNodeModel<T>)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

