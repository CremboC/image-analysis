package uk.ac.sanger.mig.aligner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
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

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_CENTROID_X = "Centroid X Column";
	static final String CFGKEY_CENTROID_Y = "Centroid Y Column";
	static final String CFGKEY_COLUMN = "Image Column";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	/** Column name which stores the image */
	static final SettingsModelString m_image_column = new SettingsModelString(
			CFGKEY_COLUMN, "Image");

	/** Column name which stores the x coord of the centroid */
	static final SettingsModelString m_centroidx_name = new SettingsModelString(
			CFGKEY_CENTROID_X, "WeightedCentroid Dim 1");

	/** Column name which stores the y coord of the centroid */
	static final SettingsModelString m_centroidy_name = new SettingsModelString(
			CFGKEY_CENTROID_Y, "WeightedCentroid Dim 2");

	/**
	 * Constructor for the node model.
	 */
	protected AlignerNodeModel() {
		// incoming/outgoing ports
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		exec.setProgress(0.1);

		// get the index of the column which is specified in the settings
		// makes it faster to retrieve it later on
		int imageIndex = -1, centroidXIndex = -1, centroidYIndex = -1;
		String[] as = inData[0].getDataTableSpec().getColumnNames();
		for (int i = 0; i < as.length; i++) {
			if (as[i].equals(m_image_column.getStringValue())) {
				imageIndex = i;
			}
				
			if (as[i].equals(m_centroidx_name.getStringValue())) {
				centroidXIndex = i;
			}
			
			if (as[i].equals(m_centroidy_name.getStringValue())) {
				centroidYIndex = i;
			}
		}
		
		if (imageIndex == -1 || centroidXIndex == -1 || centroidYIndex == -1) {
			throw new IllegalArgumentException("Input table is incorrectly formatted: missing columns");
		}

		exec.setProgress(0.2);

		// start creating the output table which will house all the required
		// info this creates the schema for the output table
		DataColumnSpec[] columnSpecs = DataTableSpec
				.createColumnSpecs(new String[] { "thetamin" },
						new DataType[] { DoubleCell.TYPE });
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);

		// actually create the table using the previously created schema
		BufferedDataContainer buf = exec.createDataContainer(tableSpecs);

		exec.setProgress(0.3);

		// get data from the first port
		Iterator<DataRow> iter = inData[0].iterator();

		// have to ensure input image is of bit type for now
		// get the image from the input, goes thru the rows and finds
		// the image cell of each row
		Map<String, ImgPlusCell<BitType>> imgs = new HashMap<String, ImgPlusCell<BitType>>();
		while (iter.hasNext()) {
			DataRow row = iter.next();

			double centroidX = ((DoubleCell) row.getCell(centroidXIndex))
					.getDoubleValue();
			
			double centroidY = ((DoubleCell) row.getCell(centroidYIndex))
					.getDoubleValue();

			ImgPlusCell<BitType> img = (ImgPlusCell<BitType>) row
					.getCell(imageIndex);

			ImgPlus<BitType> ip = img.getImgPlus();

			int[] x = MatrixHelper.x((int) ip.dimension(0),
					(int) ip.dimension(1));
			int[] y = MatrixHelper.y((int) ip.dimension(0),
					(int) ip.dimension(1));

			exec.setProgress(0.4);

			x = MatrixHelper.deductFromEach(x, centroidX);
			y = MatrixHelper.deductFromEach(y, centroidY);

			exec.setProgress(0.5);

			Denominator denomCalculator = new Denominator(x, y, ip);
			exec.setProgress(0.8);
			Aligner aligner = new Aligner(denomCalculator);
			exec.setProgress(0.9);

			// insert the row, must conform the schema
			DataRow insertRow = new DefaultRow(row.getKey(), new DoubleCell(
					aligner.thetamin()));

			buf.addRowToTable(insertRow);

			imgs.put(row.getKey().getString(), img);

		}

		exec.setProgress(1.0);

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

		m_centroidx_name.saveSettingsTo(settings);
		m_centroidy_name.saveSettingsTo(settings);
		m_image_column.saveSettingsTo(settings);
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

		m_centroidx_name.loadSettingsFrom(settings);
		m_centroidy_name.loadSettingsFrom(settings);
		m_image_column.loadSettingsFrom(settings);
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

		m_centroidx_name.validateSettings(settings);
		m_centroidy_name.validateSettings(settings);
		m_image_column.validateSettings(settings);
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
