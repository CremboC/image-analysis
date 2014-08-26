package uk.ac.sanger.mig.aligner;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
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
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;

/**
 * This is the model implementation of Aligner.
 * 
 * 
 * @author pi1
 */
public class AlignerNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(AlignerNodeModel.class);

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_THRESHOLD = "Threshold";

	/** initial default count value. */
	static final int DEFAULT_THRESHOLD = 50;

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".
	public static final SettingsModelIntegerBounded m_threshold = new SettingsModelIntegerBounded(
			AlignerNodeModel.CFGKEY_THRESHOLD, AlignerNodeModel.DEFAULT_THRESHOLD,
			0, 255);

	/**
	 * Constructor for the node model.
	 */
	protected AlignerNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		Iterator<DataRow> iter = inData[0].iterator();
		DataRow row = null;

		// have to ensure input image is an unsigned short byte type for now
		// get the image from the input, goes thru the rows and finds 
		// the image cell of each row (now only works with one row)
		ImgPlus<UnsignedByteType> ip = null;
		while (iter.hasNext()) {
			row = iter.next();
			
			ImgPlusCell<UnsignedByteType> img = null;
			
			for (int i = 0; i < row.getNumCells(); i++) {
				if (row.getCell(i) instanceof ImgPlusCell<?>) {
					img = (ImgPlusCell<UnsignedByteType>) row.getCell(i);
					break;
				}
			}
			
			if (img == null) {
				throw new IllegalStateException("No image in the input.");
			} else {
				ip = img.getImgPlus();
			}
		
		}

		// create a new output image which will house the modifications
		// not really needed for our needs
		ImgPlus<BitType> output = new ImgPlus<BitType>(
				new ArrayImgFactory<BitType>().create(ip, new BitType()));

		// access the pixels of the output image
		final RandomAccess<BitType> outAccess = output.randomAccess();

		// cursor over input image
		final Cursor<UnsignedByteType> inCursor = ip.localizingCursor();

		// iterate over pixels of in input image
		while (inCursor.hasNext()) {
			inCursor.fwd();

			// set outaccess on position of incursor
			outAccess.setPosition(inCursor);

			// set pixel value of output true, if pixel value of incoming image
			// > manual threshold
			outAccess.get().set(
					inCursor.get().getInteger() > m_threshold.getIntValue());
		}

		// start creating the output table which will house all the required info
		
		// this creates the schema for the output table
		DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(
				new String[] { "value", "image" }, new DataType[] { DoubleCell.TYPE, ImgPlusCell.TYPE });		
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);
		
		// actually create the table using the previously created schema
		BufferedDataContainer buf = exec.createDataContainer(tableSpecs);
		
		// factory required to create an image cell
		ImgPlusCellFactory imgFactory = new ImgPlusCellFactory(exec);
		
		// insert the row, must conform the schema
		DataRow insertRow = new DefaultRow("1", new DoubleCell(1.2139012), imgFactory.createCell(output));

		buf.addRowToTable(insertRow);

		buf.close();
		
		BufferedDataTable table = buf.getTable();

		return new BufferedDataTable[] { table };
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

		m_threshold.saveSettingsTo(settings);

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

		m_threshold.loadSettingsFrom(settings);

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

		m_threshold.validateSettings(settings);

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
