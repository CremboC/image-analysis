package uk.ac.sanger.mig.xray.activecontour;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.real.DoubleType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.activecontour.utils.ActiveContour;

/**
 * This is the model implementation of ActiveContour.
 * 
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class ActiveContourNodeModel extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "Contoured" };

	/** Column types of the schema */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_GRADX_COL = "Gradient X Column";
	static final String CFGKEY_GRADY_COL = "Gradient Y Column";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	static final Map<String, SettingsModel> settingsModels;

	static {
		settingsModels = new HashMap<>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));
		
		settingsModels.put(CFGKEY_GRADX_COL, new SettingsModelColumnName(
				CFGKEY_GRADX_COL, ""));
		
		settingsModels.put(CFGKEY_GRADY_COL, new SettingsModelColumnName(
				CFGKEY_GRADY_COL, ""));
	}

	/**
	 * Constructor for the node model.
	 */
	protected ActiveContourNodeModel() {
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

		final ActiveContour c = new ActiveContour();

		final Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			final DataRow row = iter.next();

			// get the image according to the setting
			final ImgPlus<DoubleType> ip = (ImgPlus<DoubleType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);
			final ImgPlus<DoubleType> gradX = (ImgPlus<DoubleType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);
			final ImgPlus<DoubleType> gradY = (ImgPlus<DoubleType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);

			out.open(row.getKey());

			// crop out the required part and add it to the output table
			out.add(c.greedy(ip, gradX, gradY));

			out.close();
		}

		// return the output table on the first (0th) outport
		return new BufferedDataTable[] { out.getOutputTable() };
	}

}
