package uk.ac.sanger.mig.aligner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Aligner" Node.
 * 
 *
 * @author pi1
 */
public class AlignerNodeFactory
        extends NodeFactory<AlignerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AlignerNodeModel createNodeModel() {
        return new AlignerNodeModel();
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
    public NodeView<AlignerNodeModel> createNodeView(final int viewIndex,
            final AlignerNodeModel nodeModel) {
        return new AlignerNodeView(nodeModel);
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
        return new AlignerNodeDialog();
    }

}

