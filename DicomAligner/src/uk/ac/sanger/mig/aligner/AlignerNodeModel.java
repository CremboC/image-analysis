package uk.ac.sanger.mig.aligner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

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
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.aligner.helpers.Aligner;
import uk.ac.sanger.mig.aligner.helpers.Denominator;
import uk.ac.sanger.mig.aligner.helpers.MatrixHelper;

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
	static final String CFGKEY_CENTROID_X = "Centroid X";
	static final String CFGKEY_CENTROID_Y = "Centroid Y";

	/** initial default count value. */
	static final int DEFAULT_THRESHOLD = 50;

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".
	static final SettingsModelIntegerBounded m_threshold = new SettingsModelIntegerBounded(
			AlignerNodeModel.CFGKEY_THRESHOLD,
			AlignerNodeModel.DEFAULT_THRESHOLD, 0, 255);

	static final SettingsModelDouble m_centroidx = new SettingsModelDouble(
			CFGKEY_CENTROID_X, 0);

	static final SettingsModelDouble m_centroidy = new SettingsModelDouble(
			CFGKEY_CENTROID_Y, 0);

	/**
	 * Constructor for the node model.
	 */
	protected AlignerNodeModel() {

		// TODO number of incoming and outgoing ports
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

		double centroidX = m_centroidx.getDoubleValue(), 
			centroidY = m_centroidy.getDoubleValue();

		// have to ensure input image is of bit type for now
		// get the image from the input, goes thru the rows and finds
		// the image cell of each row 
		List<ImgPlus<BitType>> ips = new ArrayList<ImgPlus<BitType>>();
		while (iter.hasNext()) {
			row = iter.next();

			ImgPlusCell<BitType> img = null;

			for (int i = 0; i < row.getNumCells(); i++) {
				if (row.getCell(i) instanceof ImgPlusCell<?>) {
					img = (ImgPlusCell<BitType>) row.getCell(i);
					break;
				}
			}

			if (img == null) {
				throw new IllegalStateException("No image in the input.");
			} else {
				ips.add(img.getImgPlus());
			}

		}
		
		// start creating the output table which will house all the required
		// info
		// this creates the schema for the output table
		DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(
				new String[] { "thetamin" }, new DataType[] {
						DoubleCell.TYPE });
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);

		// actually create the table using the previously created schema
		BufferedDataContainer buf = exec.createDataContainer(tableSpecs);
		
		int key = 0;
		for (ImgPlus<BitType> ip : ips) {
			int[] x = MatrixHelper.generateX((int) ip.dimension(0), (int) ip.dimension(1));
			int[] y = MatrixHelper.generateY((int) ip.dimension(0), (int) ip.dimension(1));
			
			x = MatrixHelper.deductFromEach(x, centroidX);
			y = MatrixHelper.deductFromEach(y, centroidY);
			
			Denominator denomCalculator = new Denominator(x, y, ip);
			Aligner aligner = new Aligner(denomCalculator);
			
			System.out.println(denomCalculator.denom());
			System.out.println(aligner.thetamin());
			

			// insert the row, must conform the schema
			DataRow insertRow = new DefaultRow("" + key++, new DoubleCell(aligner.thetamin()));

			buf.addRowToTable(insertRow);
		}
		
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
		m_centroidx.saveSettingsTo(settings);
		m_centroidy.saveSettingsTo(settings);
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
		m_centroidx.loadSettingsFrom(settings);
		m_centroidy.loadSettingsFrom(settings);

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
		m_centroidx.validateSettings(settings);
		m_centroidy.validateSettings(settings);
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
