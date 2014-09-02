package uk.ac.sanger.mig.regioncropper.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.knip.base.data.img.ImgPlusCellFactory;

/**
 * Helps format the output table
 * 
 * @author Paulius pi1@sanger.ac.uk
 * 
 */
public class OutputHelper {

	private final String[] columns;
	private final DataType[] columnTypes;
	private final ExecutionContext exec;

	private BufferedDataContainer outputBuffer;

	private List<DataCell> cellsToAdd;
	private RowKey currentKey;

	/**
	 * Helper for formatting and storing the output table
	 * 
	 * @param columnTypes
	 * @param columns
	 * 
	 * @param exec
	 */
	public OutputHelper(String[] columns, DataType[] columnTypes,
			final ExecutionContext exec) {
		this.exec = exec;
		this.columns = columns;
		this.columnTypes = columnTypes;

		createOutputTable();
	}

	/**
	 * 
	 */
	private void createOutputTable() {
		// start creating the output table which will house all the required
		// info this creates the schema for the output table
		DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(columns,
				columnTypes);
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);

		// actually create the table using the previously created schema
		outputBuffer = exec.createDataContainer(tableSpecs);
	}

	/**
	 * Starts a row with given key
	 * 
	 * @param key
	 */
	public void open(RowKey key) {
		cellsToAdd = new ArrayList<DataCell>();
		currentKey = key;
	}

	/**
	 * Adds an ImgPlus to the current open row
	 * 
	 * @param img
	 * @throws IOException
	 */
	public void add(ImgPlus<BitType> img) throws IOException {
		check();

		ImgPlusCellFactory imgFactory = new ImgPlusCellFactory(exec);

		cellsToAdd.add(imgFactory.createCell(img));
	}

	/**
	 * Adds a vector2d array to the row, puts them as a string using the format
	 * "x:y"
	 * 
	 * @param vec2ds
	 */
	public void add(Vector2D[] vec2ds) {
		check();

		for (int i = 0; i < vec2ds.length; i++) {
			cellsToAdd.add(new StringCell(vec2ds[i].getX() + ";"
					+ vec2ds[i].getY()));
		}
	}

	/**
	 * Add an int array to the row
	 * 
	 * @param array
	 */
	public void add(int[] array) {
		check();

		for (int i = 0; i < array.length; i++) {
			add(array[i]);
		}
	}

	/**
	 * Add a single int to the row
	 * 
	 * @param val
	 */
	public void add(int val) {
		check();

		cellsToAdd.add(new IntCell(val));
	}

	/** Checks if the the row has been opened */
	private void check() {
		if (cellsToAdd == null || currentKey == null) {
			throw new IllegalStateException(
					"Must open row before adding to it.");
		}
	}

	/**
	 * Closes a row
	 */
	public void close() {
		DataRow toAdd = new DefaultRow(currentKey, cellsToAdd);
		outputBuffer.addRowToTable(toAdd);
		cellsToAdd = null;
		currentKey = null;
	}

	/**
	 * @return the table to output
	 */
	public BufferedDataTable getOutputTable() {
		outputBuffer.close();

		return outputBuffer.getTable();
	}

}