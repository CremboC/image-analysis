package uk.ac.sanger.mig.xray.activecontour;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ActiveContour" Node.
 * 
 *
 * @author Wellcome Trust Sanger Institute
 */
public class ActiveContourNodeFactory 
        extends NodeFactory<ActiveContourNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveContourNodeModel createNodeModel() {
        return new ActiveContourNodeModel();
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
    public NodeView<ActiveContourNodeModel> createNodeView(final int viewIndex,
            final ActiveContourNodeModel nodeModel) {
        return new ActiveContourNodeView(nodeModel);
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
        return new ActiveContourNodeDialog();
    }

}

