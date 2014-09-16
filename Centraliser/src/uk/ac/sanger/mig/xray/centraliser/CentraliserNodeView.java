package uk.ac.sanger.mig.xray.centraliser;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "Centraliser" Node.
 * Centres a blob of white pixels to the centre of the image.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class CentraliserNodeView extends NodeView<CentraliserNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link CentraliserNodeModel})
     */
    protected CentraliserNodeView(final CentraliserNodeModel nodeModel) {
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
        CentraliserNodeModel nodeModel = 
            (CentraliserNodeModel)getNodeModel();
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

