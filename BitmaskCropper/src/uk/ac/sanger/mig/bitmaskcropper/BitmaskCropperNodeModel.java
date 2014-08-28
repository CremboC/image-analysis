package uk.ac.sanger.mig.bitmaskcropper;

import java.io.File;
import java.io.IOException;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

import uk.ac.sanger.mig.bitmaskcropper.utils.Cropper;
import uk.ac.sanger.mig.bitmaskcropper.utils.Utils;

/**
 * This is the model implementation of BitmaskCropper. Takes a bitmask (image)
 * and an image and crops the image according to the bitmask.
 * 
 * @author Paulius @ WTSI
 */
public class BitmaskCropperNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(BitmaskCropperNodeModel.class);

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_COUNT = "Count";

	/** initial default count value. */
	static final int DEFAULT_COUNT = 100;

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".
	private final SettingsModelIntegerBounded m_count = new SettingsModelIntegerBounded(
			BitmaskCropperNodeModel.CFGKEY_COUNT,
			BitmaskCropperNodeModel.DEFAULT_COUNT, Integer.MIN_VALUE,
			Integer.MAX_VALUE);

	/**
	 * Constructor for the node model.
	 */
	protected BitmaskCropperNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(2, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		
		// original image
		ImgPlus<UnsignedByteType> ip = Utils.findImageInInput(inData[0]);
		
		// bitmask
		ImgPlus<BitType> mask = Utils.findImageInInput(inData[1]);


		Cropper cropper = new Cropper(ip, mask);
		
		cropper.createOutputTable(exec);
		cropper.crop();
		
		return new BufferedDataTable[] { cropper.getOutputTable(exec) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO Code executed on reset.
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		// TODO save user settings to the config object.

		m_count.saveSettingsTo(settings);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// TODO load (valid) settings from the config object.
		// It can be safely assumed that the settings are valided by the
		// method below.

		m_count.loadSettingsFrom(settings);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// TODO check if the settings could be applied to our model
		// e.g. if the count is in a certain range (which is ensured by the
		// SettingsModel).
		// Do not actually set any values of any member variables.

		m_count.validateSettings(settings);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {

		// TODO load internal data.
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {

		// TODO save internal models.
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).

	}

}
