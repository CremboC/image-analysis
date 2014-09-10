package uk.ac.sanger.mig.xray.trendlinecropper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.trendlinecropper.utils.TrendCropper;

/**
 * This is the model implementation of TrendLineCropper.
 * Crops out a region following the trendling. Use the left and right margin to specify how many pixels left and right of the trendline will be removed. * n * nDue to the nature of trend lines, parameters for starting row and ending row are also provided.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineCropperNodeModel<T extends RealType<T> & NativeType<T>> extends GenericNodeModel {
	
	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "Image" };

	/** Column types of the schema */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_START_ROW = "Start at Row";
	static final String CFGKEY_END_ROW = "End at Row";
	static final String CFGKEY_LEFT_MARGIN = "Left Margin";
	static final String CFGKEY_RIGHT_MARGIN = "Right Margin";
	
	static final String COEF_COL = "Coeficients";
	static final String TREND_COL = "Trend Type";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	static final Map<String, SettingsModel> settingsModels;

	static {
		settingsModels = new HashMap<String, SettingsModel>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));

		settingsModels.put(CFGKEY_START_ROW, new SettingsModelInteger(
				CFGKEY_START_ROW, 0));

		settingsModels.put(CFGKEY_END_ROW, new SettingsModelInteger(
				CFGKEY_END_ROW, -1));

		settingsModels.put(CFGKEY_LEFT_MARGIN, new SettingsModelInteger(
				CFGKEY_LEFT_MARGIN, 1));

		settingsModels.put(CFGKEY_RIGHT_MARGIN, new SettingsModelInteger(
				CFGKEY_RIGHT_MARGIN, 1));
	}
    
    /**
     * Constructor for the node model.
     */
    protected TrendLineCropperNodeModel() {
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

		OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES, exec);
		
		int leftMargin = intFromSetting(CFGKEY_LEFT_MARGIN);
		int rightMargin = intFromSetting(CFGKEY_RIGHT_MARGIN);
		
		int startRow = intFromSetting(CFGKEY_START_ROW);
		int endRow = intFromSetting(CFGKEY_END_ROW);
		
		TrendCropper<T> cropper = new TrendCropper<T>(leftMargin, rightMargin, startRow, endRow);

		Iterator<DataRow> iter = inData[INPORT_0].iterator();
		while (iter.hasNext()) {
			DataRow row = iter.next();

			// get the image according to the setting
			ImgPlus<T> ip = (ImgPlus<T>) imageBySetting(row, CFGKEY_IMAGE_COL);
			String coefs = stringFromRow(row, COEF_COL);
			String trend = stringFromRow(row, TREND_COL);

			out.open(row.getKey());

			// crop out the required part and add it to the output table
			out.add(cropper.process(ip, coefs, trend));
			
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
        return new DataTableSpec[]{null};
    }
}

