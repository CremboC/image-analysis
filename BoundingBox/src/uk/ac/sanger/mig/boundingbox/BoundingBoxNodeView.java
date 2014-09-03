package uk.ac.sanger.mig.boundingbox;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "BoundingBox" Node. Calculated a bounding box
 * according to a threshold of pixels in a row/column
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class BoundingBoxNodeView<T extends RealType<T> & NativeType<T>> extends
		NodeView<BoundingBoxNodeModel<T>> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link BoundingBoxNodeModel})
	 */
	protected BoundingBoxNodeView(final BoundingBoxNodeModel<T> nodeModel) {
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
		BoundingBoxNodeModel<T> nodeModel = (BoundingBoxNodeModel<T>) getNodeModel();
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
