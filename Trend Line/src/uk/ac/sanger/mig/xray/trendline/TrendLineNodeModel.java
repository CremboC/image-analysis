package uk.ac.sanger.mig.xray.trendline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.maths.trendline.Fitting;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.xray.trendline.utils.Fitter;

import com.sun.istack.internal.Nullable;

/**
 * This is the model implementation of TrendLine. Takes an image and using its
 * bright spots finds finds the trend line. nAccuracy may be adjusted using the
 * settings dialog.
 * 
 * @author Wellcome Trust Sanger Institute
 */
public class TrendLineNodeModel extends GenericNodeModel {

	/** Columns in the schema */
	private final static String[] COLUMN_NAMES = { "Image", "Coeficients", "Trend Type" };

	/** Column types of the schema */
	private final static DataType[] COLUMN_TYPES = { ImgPlusCell.TYPE, StringCell.TYPE, StringCell.TYPE };

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_IMAGE_COL = "Image Column";
	static final String CFGKEY_FIT_TYPE = "Fitting Type";
	static final String CFGKEY_POLY_DEG = "Poly Degree";
	static final String CFGKEY_RET_TYPE = "Return Type";	

	static final String[] FITTING_TYPES = Fitting.names();
	static final String[] RETURN_TYPES = ReturnType.names();

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	static final Map<String, SettingsModel> settingsModels;
	static {
		settingsModels = new HashMap<String, SettingsModel>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));

		settingsModels.put(CFGKEY_POLY_DEG, new SettingsModelInteger(
				CFGKEY_POLY_DEG, 0));

		settingsModels.put(CFGKEY_FIT_TYPE, new SettingsModelString(
				CFGKEY_FIT_TYPE, Fitting.EXP.toString()));
		
		settingsModels.put(CFGKEY_RET_TYPE, new SettingsModelString(
				CFGKEY_RET_TYPE, "Original"));
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

		OutputHelper out = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES, exec);

		Iterator<DataRow> iter = inData[INPORT_0].iterator();

		Fitting fitting = Fitting.whereName(stringFromSetting(CFGKEY_FIT_TYPE));
		ReturnType retType = ReturnType.whereName(stringFromSetting(CFGKEY_RET_TYPE));

		int degree = intFromSetting(CFGKEY_POLY_DEG);
		Fitter fitter = new Fitter(fitting, degree);

		while (iter.hasNext()) {
			DataRow row = iter.next();

			// get the image according to the setting
			ImgPlus<BitType> ip = (ImgPlus<BitType>) imageBySetting(row,
					CFGKEY_IMAGE_COL);

			out.open(row.getKey());

			String coefs = fitter.fit(ip);
			
			out.add((retType == ReturnType.ORIG) ? ip : fitter.image());
			out.add(coefs);
			out.add(fitting.toString());

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
	
	private enum ReturnType {
		ORIG("Original"),
		LINED("Lined");
		
		private String name;
		
		private ReturnType(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		/**
		 * Finds enum which has names as provided
		 * @param name name to look for, <b>null</b> if not found
		 */
		@Nullable
		public static final ReturnType whereName(String name) {
			
			for (int i = 0; i < values().length; i++) {
				if (values()[i].toString().equals(name)) {
					return values()[i];
				}
			}
			
			return null;
		}
		
		/**
		 * Returns names of all enums
		 */
		public static final String[] names() {
			String[] names = new String[values().length];
			
			for (int i = 0; i < values().length; i++) {
				names[i] = values()[i].toString();
			}
			
			return names;
		}
	}

}
