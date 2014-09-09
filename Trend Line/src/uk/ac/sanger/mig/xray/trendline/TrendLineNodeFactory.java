package uk.ac.sanger.mig.xray.trendline;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "TrendLine" Node.
 * Takes an image and using its bright spots finds finds the trend line. * nAccuracy may be adjusted using the settings dialog.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineNodeFactory 
        extends NodeFactory<TrendLineNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TrendLineNodeModel createNodeModel() {
        return new TrendLineNodeModel();
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
    public NodeView<TrendLineNodeModel> createNodeView(final int viewIndex,
            final TrendLineNodeModel nodeModel) {
        return new TrendLineNodeView(nodeModel);
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
        return new TrendLineNodeDialog();
    }

}

