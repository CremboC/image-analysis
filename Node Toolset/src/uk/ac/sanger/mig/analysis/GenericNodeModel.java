package uk.ac.sanger.mig.analysis;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import net.imglib2.meta.ImgPlus;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import uk.ac.sanger.mig.analysis.nodetools.Utils;

/**
 * This is the model implementation of Aligner.
 * 
 * @author Wellcome Trust Sanger Institute
 * @author Paulius pi1@sanger.ac.uk
 * 
 */
public abstract class GenericNodeModel extends NodeModel {

	protected final static int INPORT_0 = 0;
	
	/** Maps Column Name -> Index */
	protected Map<String, Integer> indices;

	private final Map<String, SettingsModel> settingsModels;

	/**
	 * Constructor for the node model
	 * @param inPorts number of incoming ports
	 * @param outPorts number of outgoing ports
	 * @param settingsModels settings which will appear in the dialog
	 */
	protected GenericNodeModel(int inPorts, int outPorts,
			Map<String, SettingsModel> settingsModels) {
		// incoming/outgoing ports
		super(inPorts, outPorts);

		this.settingsModels = settingsModels;
	}
	
	protected abstract BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception;
	
	protected abstract DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException;
	
	/**
	 * Given a row, fetches an image from the correct column based on settings key
	 * 
	 * @param row row to fetch from
	 * @param cfgkeyImageCol 
	 */
    protected ImgPlus<?> imageBySetting(DataRow row, String cfgkeyImageCol) {
    	int imageIndex = indexByColumnName(cfgkeyImageCol);

		return Utils.imageByIndex(row, imageIndex);
	}

    /**
     * Given a row, fetches an int from the correct column based on settings key
     * 
     * @param row row to fetch from
     * @param cfgKey settings key which defines the column name
     */
    protected int intBySetting(DataRow row, String cfgKey) {
    	int intIndex = indexByColumnName(cfgKey);
    	
    	return Utils.intByIndex(row, intIndex);
    }
    
    /**
     * Given a row, fetches an double from the correct column based on settings key
     * 
     * @param row row to fetch from
     * @param cfgKey settings key which defines the column name
     */
    protected double doubleBySetting(DataRow row, String cfgKey) {
    	int intIndex = indexByColumnName(cfgKey);
    	
    	return Utils.doubleByIndex(row, intIndex);
    }
    
    /**
     * Gets a double value from the settings (provided SettingsModel is of Double type)
     * by the SettingsModel key
     * 
     * @param cfgKey settings key which defines the column name
     */
    protected double doubleFromSetting(String cfgKey) {
    	SettingsModel setting = settingsModels.get(cfgKey);
    	
    	return ((SettingsModelDouble) setting).getDoubleValue();
    }
    
    /**
     * Gets a integer value from the settings (provided SettingsModel is of Integer type)
     * by the SettingsModel key
     * 
     * @param cfgKey settings key which defines the column name
     */
    protected int intFromSetting(String cfgKey) {
    	SettingsModel setting = settingsModels.get(cfgKey);
    	
    	return ((SettingsModelInteger) setting).getIntValue();
    }
    
    /**
     * Gets a string value from the settings (provided SettingsModel is of String type or derived)
     * by the SettingsModel key
     * 
     * @param cfgKey settings key which defines the column name
     */
    protected String stringFromSetting(String cfgKey) {
    	SettingsModel setting = settingsModels.get(cfgKey);
    	
    	return ((SettingsModelString) setting).getStringValue();
    }
    
    /**
     * 
     * @param cfgkeyCroptop
     * @return
     */
	protected boolean boolFromSettings(String cfgKey) {
    	SettingsModel setting = settingsModels.get(cfgKey);
    	
    	return ((SettingsModelBoolean) setting).getBooleanValue();
	}
    
	/**
	 * 
	 * @param row
	 * @param columnName
	 * @return
	 */
    protected String stringFromRow(DataRow row, String columnName) {
    	int index = indices.get(columnName);
    	
    	return Utils.stringByIndex(row, index);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		for (Entry<String, SettingsModel> e : settingsModels.entrySet()) {
			e.getValue().saveSettingsTo(settings);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (Entry<String, SettingsModel> e : settingsModels.entrySet()) {
			e.getValue().loadSettingsFrom(settings);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (Entry<String, SettingsModel> e : settingsModels.entrySet()) {
			e.getValue().validateSettings(settings);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * Helper method to get the index
	 * 
	 * @param name settings model key
	 * @return
	 */
	private int indexByColumnName(String name) {
		SettingsModel setting = settingsModels.get(name);
		String settingValue = ((SettingsModelColumnName) setting).getStringValue();
		
		return indices.get(settingValue);
	}
}
