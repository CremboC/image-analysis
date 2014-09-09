package uk.ac.sanger.mig.aligner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;

import uk.ac.sanger.mig.aligner.helpers.Aligner;
import uk.ac.sanger.mig.aligner.helpers.Denominator;
import uk.ac.sanger.mig.aligner.helpers.MatrixHelper;
import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.Image;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;

public class AlignerNodeModel extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "thetamin" };

	/** Column types */
	private final static DataType[] COLUMN_TYPES = { DoubleCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_CENTROID_X = "Centroid X Column";
	static final String CFGKEY_CENTROID_Y = "Centroid Y Column";
	static final String CFGKEY_IMAGE_COL = "Image Column";

	private static final String DEFAULT_IMAGE_COL = "Image";
	private static final String DEFAULT_CENTROIDX_COL = "WeightedCentroid Dim 1";
	private static final String DEFAULT_CENTROIDY_COL = "WeightedCentroid Dim 2";

	protected static final int IN_PORTS = 1;
	protected static final int OUT_PORTS = 1;

	static final Map<String, SettingsModel> settingsModels;
	static {
		settingsModels = new HashMap<String, SettingsModel>();
		
		// Column name which stores the image
		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, DEFAULT_IMAGE_COL));

		// Column name which stores the x coord of the centroid
		settingsModels.put(CFGKEY_CENTROID_X, new SettingsModelColumnName(
				CFGKEY_CENTROID_X, DEFAULT_CENTROIDX_COL));

		// Column name which stores the y coord of the centroid
		settingsModels.put(CFGKEY_CENTROID_Y, new SettingsModelColumnName(
				CFGKEY_CENTROID_Y, DEFAULT_CENTROIDY_COL));

	}

	protected AlignerNodeModel() {
		super(IN_PORTS, OUT_PORTS, settingsModels);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		indices = Utils.indices(inData[INPORT_0].getDataTableSpec());

		OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES, exec);

		// get data from the first port
		Iterator<DataRow> iter = inData[INPORT_0].iterator();

		// have to ensure input image is of bit type for now
		// get the image from the input, goes thru the rows and finds
		// the image cell of each row
		while (iter.hasNext()) {
			DataRow row = iter.next();

			double centroidX = doubleBySetting(row, CFGKEY_CENTROID_X);
			double centroidY = doubleBySetting(row, CFGKEY_CENTROID_Y);

			ImgPlus<BitType> ip = (ImgPlus<BitType>) imageBySetting(row, CFGKEY_IMAGE_COL);

			int[] x = MatrixHelper.x((int) ip.dimension(Image.COL),
					(int) ip.dimension(Image.ROW));
			int[] y = MatrixHelper.y((int) ip.dimension(Image.COL),
					(int) ip.dimension(Image.ROW));

			x = MatrixHelper.deductFromEach(x, centroidX);
			y = MatrixHelper.deductFromEach(y, centroidY);

			Denominator denomCalculator = new Denominator(x, y, ip);
			Aligner aligner = new Aligner(denomCalculator);

			double theta = aligner.thetamin();

			out.open(row.getKey());

			out.add(theta);

			out.close();
		}

		return new BufferedDataTable[] { out.getOutputTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		return new DataTableSpec[] { null };
	}
}