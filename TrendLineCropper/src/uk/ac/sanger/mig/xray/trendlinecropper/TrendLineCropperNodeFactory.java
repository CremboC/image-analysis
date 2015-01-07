/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Trend Line Cropper.
 * 
 * Trend Line Cropper is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.xray.trendlinecropper;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "TrendLineCropper" Node.
 * Crops out a region following the trendling. Use the left and right margin to specify how many pixels left and right of the trendline will be removed. * n * nDue to the nature of trend lines, parameters for starting row and ending row are also provided.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineCropperNodeFactory<T extends RealType<T> & NativeType<T>>
        extends NodeFactory<TrendLineCropperNodeModel<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TrendLineCropperNodeModel<T> createNodeModel() {
        return new TrendLineCropperNodeModel<T>();
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
    public NodeView<TrendLineCropperNodeModel<T>> createNodeView(final int viewIndex,
            final TrendLineCropperNodeModel<T> nodeModel) {
        return new TrendLineCropperNodeView<T>(nodeModel);
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
        return new TrendLineCropperNodeDialog();
    }

}

