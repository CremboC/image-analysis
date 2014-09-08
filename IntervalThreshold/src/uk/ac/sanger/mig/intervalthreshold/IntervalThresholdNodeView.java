package uk.ac.sanger.mig.intervalthreshold;

import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "IntervalThreshold" Node. Select a range of
 * pixel values, between an upper and lower bound setting all other pixels to
 * the background value.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class IntervalThresholdNodeView<T extends RealType<T> & NativeType<T>>
		extends NodeView<IntervalThresholdNodeModel<T>> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link IntervalThresholdNodeModel})
	 */
	protected IntervalThresholdNodeView(
			final IntervalThresholdNodeModel<T> nodeModel) {
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
