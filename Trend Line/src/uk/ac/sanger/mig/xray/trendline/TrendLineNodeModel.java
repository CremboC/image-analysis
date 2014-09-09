package uk.ac.sanger.mig.xray.trendline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.trendline.utils.Fitter;
import uk.ac.sanger.mig.xray.trendline.utils.Fitting;

/**
 * This is the model implementation of TrendLine. Takes an image and using its
 * bright spots finds finds the trend line. nAccuracy may be adjusted using the
 * settings dialog.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineNodeModel extends
		GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMNS = { "Image" };

	/** Column types of the schema */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_FIT_TYPE = "Fitting Type";
	static final String CFGKEY_POWER_DEG = "Power Degree";
	

	static final String[] FITTING_TYPES = Fitting.names();

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	static final Map<String, SettingsModel> settingsModels;
	static {
		settingsModels = new HashMap<String, SettingsModel>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));
		
		settingsModels.put(CFGKEY_POWER_DEG, new SettingsModelInteger(
				CFGKEY_POWER_DEG, 0));

		settingsModels.put(CFGKEY_FIT_TYPE, new SettingsModelString(
				CFGKEY_FIT_TYPE, Fitting.EXP.toString()));
	}

	/**
	 * Constructor for the node model.
	 */
	protected TrendLineNodeModel() {
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

		OutputHelper out = new OutputHelper(COLUMNS, COLUMN_TYPES, exec);

		Iterator<DataRow> iter = inData[INPORT_0].iterator();

		Fitting fitting = Fitting
				.whereName(stringFromSetting(CFGKEY_FIT_TYPE));
		
		int degree = intFromSetting(CFGKEY_POWER_DEG);

		while (iter.hasNext()) {
			DataRow row = iter.next();

			// get the image according to the setting
			ImgPlus<BitType> ip = (ImgPlus<BitType>) imageBySetting(row, CFGKEY_IMAGE_COL);

			Fitter fitter = new Fitter(fitting, degree);

			out.open(row.getKey());

			fitter.fit(ip);

			// out.add();

			out.close();
		}

		// return the output table on the first (0th) outport
		return new BufferedDataTable[] { out.getOutputTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		// TODO: generated method stub
		return new DataTableSpec[] { null };
	}

}
