/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Trend Line.
 * 
 * Trend Line is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.xray.trendline;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "TrendLine" Node.
 * Takes an image and using its bright spots finds finds the trend line. * nAccuracy may be adjusted using the settings dialog.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineNodeView extends NodeView<TrendLineNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link TrendLineNodeModel})
     */
    protected TrendLineNodeView(final TrendLineNodeModel nodeModel) {
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

