package uk.ac.sanger.mig.analysis;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

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
	protected Map<String, Integer> indices;

	private final Map<String, SettingsModel> settingsModels;

	/**
	 * Constructor for the node model.
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
	 * Helper method to get the index
	 * 
	 * @param name
	 * @return
	 */
	protected int indexByColumnName(String name) {
		return indices.get(Utils.stringFromSetting(settingsModels.get(name)));
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

}
