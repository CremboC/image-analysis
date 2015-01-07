/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Peak Counter.
 * 
 * Peak Counter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option ) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.sanger.mig.xray.peakcounter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PeakCounter" Node.
 * Scans through the image and using buckets counts number of peaks. * nWhen it finds a white pixel, it creates a bucket. Continue on scanning, every following white pixel is added to the same bucket. When a black pixel is reached, the bucket is closed.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class PeakCounterNodeFactory 
        extends NodeFactory<PeakCounterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PeakCounterNodeModel createNodeModel() {
        return new PeakCounterNodeModel();
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
    public NodeView<PeakCounterNodeModel> createNodeView(final int viewIndex,
            final PeakCounterNodeModel nodeModel) {
        return new PeakCounterNodeView(nodeModel);
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
        return new PeakCounterNodeDialog();
    }

}

