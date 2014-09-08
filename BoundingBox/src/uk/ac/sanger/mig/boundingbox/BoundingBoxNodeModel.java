package uk.ac.sanger.mig.boundingbox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.boundingbox.utils.BoundingBox;

/**
 * This is the model implementation of BoundingBox. Calculated a bounding box
 * according to a threshold of pixels in a row/column
 * 
 * @author Wellcome Trust Sanger Institute
 * @author Paulius pi1@sanger.ac.uk
 * @author MIG Team team110dev@sanger.ac.uk
 * 
 * @param <T>
 */
public class BoundingBoxNodeModel<T extends RealType<T> & NativeType<T>>
		extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMNS = { "Image", "Upper Boundary",
			"Lower Boundary", "Left Boundary", "Right Boundary" };

	/** Column types */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE,
			IntCell.TYPE, IntCell.TYPE, IntCell.TYPE, IntCell.TYPE };

	private static final String CENTROID_COL_X = "WeightedCentroid Dim 1";
	private static final String CENTROID_COL_Y = "WeightedCentroid Dim 2";

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_ROW_THRESHOLD = "Row Threshold";
	static final String CFGKEY_COL_THRESHOLD = "Column Threshold";

	private static final String DEFAULT_IMAGE_COL = "Image";

	static final Map<String, SettingsModel> settingsModels;
	static {
		settingsModels = new HashMap<String, SettingsModel>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, DEFAULT_IMAGE_COL));

		settingsModels.put(CFGKEY_ROW_THRESHOLD, new SettingsModelString(
				CFGKEY_ROW_THRESHOLD, "0,0"));

		settingsModels.put(CFGKEY_COL_THRESHOLD, new SettingsModelString(
				CFGKEY_COL_THRESHOLD, "0,0"));
	}

	/**
	 * Constructor for the node model.
	 */
	protected BoundingBoxNodeModel() {
		super(1, 1, settingsModels);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		indices = Utils.indices(inData[INPORT_0].getDataTableSpec());

		String rowThresholds = stringFromSetting(CFGKEY_ROW_THRESHOLD);
		String colThresholds = stringFromSetting(CFGKEY_COL_THRESHOLD);

		OutputHelper out = new OutputHelper(COLUMNS, COLUMN_TYPES, exec);

		Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			DataRow row = iter.next();

			ImgPlus<T> ip = (ImgPlus<T>) imageBySetting(row, CFGKEY_IMAGE_COL);

			int centroidX = (int) Utils.doubleByIndex(row,
					indices.get(CENTROID_COL_X));
			int centroidY = (int) Utils.doubleByIndex(row,
					indices.get(CENTROID_COL_Y));

			BoundingBox<T> box = new BoundingBox<T>(ip, centroidX, centroidY,
					Utils.split(rowThresholds), Utils.split(colThresholds));

			int[] boundaries = box.find();

			out.open(row.getKey());

			out.add(box.image());
			out.add(boundaries);

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

		for (int i = 0; i < inSpecs[INPORT_0].getNumColumns(); i++) {
			DataColumnSpec spec = inSpecs[INPORT_0].getColumnSpec(i);

			if (spec.getType() == ImgPlusCell.TYPE) {

				SettingsModel sm = settingsModels.get(CFGKEY_IMAGE_COL);
				((SettingsModelColumnName) sm).setStringValue(spec.getName());

				break;
			}
		}

		return new DataTableSpec[] { null };
	}
}
