package uk.ac.sanger.mig.xray.peakcounter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.peakcounter.utils.PeakCounter;

/**
 * This is the model implementation of PeakCounter. Scans through the image and
 * using buckets counts number of peaks. When it finds a white pixel, it
 * creates a bucket. Continue on scanning, every following white pixel is added
 * to the same bucket. When a black pixel is reached, the bucket is closed.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class PeakCounterNodeModel extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "Peaks" };

	/** Column types of the schema */
	private final static DataType[] COLUMN_TYPES = { IntCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_BUC_THRESH = "Bucket Threshold";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	static final Map<String, SettingsModel> settingsModels;

	static {
		settingsModels = new HashMap<>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));

		settingsModels.put(CFGKEY_BUC_THRESH, new SettingsModelInteger(
				CFGKEY_BUC_THRESH, 0));
	}

	/**
	 * Constructor for the node model.
	 */
	protected PeakCounterNodeModel() {
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

		final OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES,
				exec);
		
		final int bucketThreshold = intFromSetting(CFGKEY_BUC_THRESH);

		final PeakCounter c = new PeakCounter(bucketThreshold);

		final Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			final DataRow row = iter.next();

			// get the image according to the setting
			final ImgPlus<BitType> ip = (ImgPlus<BitType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);

			out.open(row.getKey());

			// crop out the required part and add it to the output table
			out.add(c.count(ip));

			out.close();
		}

		// return the output table on the first (0th) outport
		return new BufferedDataTable[] { out.getOutputTable() };
	}

}