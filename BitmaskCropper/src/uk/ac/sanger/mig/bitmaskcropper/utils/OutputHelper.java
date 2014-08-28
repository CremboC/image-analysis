package uk.ac.sanger.mig.bitmaskcropper.utils;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.knip.base.data.img.ImgPlusCell;

/**
 * Helps format the output table
 * 
 * @author pi1
 *
 */
public class OutputHelper {
	private final ExecutionContext exec;

	private BufferedDataContainer outputBuffer;
	
	/**
	 * Helper for formatting and storing the output table
	 * @param exec
	 */
	public OutputHelper(final ExecutionContext exec) {
		this.exec = exec;
		
		createOutputTable();
	}
	
	/**
	 * 
	 */
	private void createOutputTable() {
		// start creating the output table which will house all the required
		// info this creates the schema for the output table
		DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(
				new String[] { "Image" }, new DataType[] { ImgPlusCell.TYPE });
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);

		// actually create the table using the previously created schema
		outputBuffer = exec.createDataContainer(tableSpecs);
	}
	
	/**
	 * Add an element to the table
	 */
	void addToTable() {
		
	}

	/**
	 * @return the table to output
	 */
	public BufferedDataTable getOutputTable() {
		// ImgPlusCellFactory imgFactory = new ImgPlusCellFactory(exec);
		//
		// ImgPlus<BitType> output = new ImgPlus<BitType>(
		// new ArrayImgFactory<BitType>().create(ip, new BitType()));
		//
		//
		//
		// imgFactory.createCell(output);

		outputBuffer.close();

		return outputBuffer.getTable();
	}
}
