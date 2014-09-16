package uk.ac.sanger.mig.xray.centraliser;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Centraliser" Node.
 * Centres a blob of white pixels to the centre of the image.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class CentraliserNodeFactory 
        extends NodeFactory<CentraliserNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CentraliserNodeModel createNodeModel() {
        return new CentraliserNodeModel();
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
    public NodeView<CentraliserNodeModel> createNodeView(final int viewIndex,
            final CentraliserNodeModel nodeModel) {
        return new CentraliserNodeView(nodeModel);
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
        return new CentraliserNodeDialog();
    }

}

