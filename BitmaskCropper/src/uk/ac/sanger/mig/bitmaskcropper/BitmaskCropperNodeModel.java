package uk.ac.sanger.mig.bitmaskcropper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.knip.base.data.img.ImgPlusCell;

import uk.ac.sanger.mig.analysis.GenericNodeModel;
import uk.ac.sanger.mig.analysis.nodetools.OutputHelper;
import uk.ac.sanger.mig.analysis.nodetools.Utils;
import uk.ac.sanger.mig.bitmaskcropper.utils.Cropper;

/**
 * This is the model implementation of BitmaskCropper. Takes a bitmask (image)
 * and an image and crops the image according to the bitmask.
 *
 * @author Wellcome Trust Sanger Institute
 */
public class BitmaskCropperNodeModel<T extends RealType<T> & NativeType<T>>
		extends GenericNodeModel {

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
	static final String CFGKEY_BITMASK_COL = "Bitmask Column";

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	static final Map<String, SettingsModel> settingsModels;
	static {
		settingsModels = new HashMap<String, SettingsModel>();

		settingsModels.put(CFGKEY_IMAGE_COL, new SettingsModelColumnName(
				CFGKEY_IMAGE_COL, "Image"));

		settingsModels.put(CFGKEY_BITMASK_COL, new SettingsModelColumnName(
				CFGKEY_BITMASK_COL, "Bitmask"));
	}

	/**
	 * Constructor for the node model.
	 */
	protected BitmaskCropperNodeModel() {
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

		final OutputHelper output = new OutputHelper(COLUMN_NAMES, COLUMN_TYPES, exec);

		final Iterator<DataRow> iter = inData[INPORT_0].iterator();
		
		while (iter.hasNext()) {
			final DataRow row = iter.next();

			final ImgPlus<T> ip = (ImgPlus<T>) imageBySetting(row, CFGKEY_IMAGE_COL);
			final ImgPlus<BitType> mask = (ImgPlus<BitType>) imageBySetting(row, CFGKEY_BITMASK_COL);
			
			final Cropper<T> cropper = new Cropper<>(ip, mask);

			output.open(row.getKey());
			
			output.add(cropper.process());
			
			output.close();
		}

		return new BufferedDataTable[] { output.getOutputTable() };
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
