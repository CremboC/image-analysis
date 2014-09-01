package uk.ac.sanger.mig.boundingbox.utils;

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
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.core.util.Triple;

/**
 * Helps format the output table
 * 
 * @author Paulius pi1@sanger.ac.uk
 * 
 */
public class OutputHelper {
	
	/** Columns in the schema */
	private final static String[] COLUMNS = {
		"Image"
	};
	
	/** Column types */
	private final static DataType[] COLUMN_TYPES = {
		ImgPlusCell.TYPE
	};
	
	private final ExecutionContext exec;

	private BufferedDataContainer outputBuffer;
	
	private List<DataCell> cellsToAdd;
	private RowKey currentKey;

	/**
	 * Helper for formatting and storing the output table
	 * 
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
		DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(COLUMNS, COLUMN_TYPES);
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);

		// actually create the table using the previously created schema
		outputBuffer = exec.createDataContainer(tableSpecs);
	}
	
	/**
	 * Starts a row with given key
	 * @param key
	 */
	public void openRow(RowKey key) {
		cellsToAdd = new ArrayList<DataCell>();
		currentKey = key;
	}
	
	/**
	 * Adds an ImgPlus to the current open row
	 * @param img
	 * @throws IOException 
	 */
	public void addToRow(ImgPlus<BitType> img) throws IOException {
		if (cellsToAdd == null || currentKey == null) {
			throw new IllegalStateException("Must open row before adding to it.");
		}
		
		ImgPlusCellFactory imgFactory = new ImgPlusCellFactory(exec);
		
		cellsToAdd.add(imgFactory.createCell(img));
	}
	
	/**
	 * Closes a row
	 */
	public void closeRow() {
		DataRow toAdd = new DefaultRow(currentKey, cellsToAdd);
		outputBuffer.addRowToTable(toAdd);
		cellsToAdd = null;
		currentKey = null;
	}

	/**
	 * Add an element to the table
	 * @param triple 
	 */
	public void addToTable(Triple<Vector2D, Integer, Integer> triple, DataRow row) {

	}
	
	/**
	 * 
	 * @param img
	 * @throws IOException 
	 */
	public void addToTable(ImgPlus<BitType> img, DataRow row) throws IOException {
		ImgPlusCellFactory imgFactory = new ImgPlusCellFactory(exec);
		imgFactory.createCell(img);
		
		outputBuffer.addRowToTable(row);
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