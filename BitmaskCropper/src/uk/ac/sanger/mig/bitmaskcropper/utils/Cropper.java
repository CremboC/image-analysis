package uk.ac.sanger.mig.bitmaskcropper.utils;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.knip.base.data.img.ImgPlusCell;

public class Cropper {

	private final ImgPlus<UnsignedByteType> image;
	private final ImgPlus<BitType> mask;

	private BufferedDataContainer outputBuffer;

	public Cropper(ImgPlus<UnsignedByteType> image, ImgPlus<BitType> mask) {
		this.image = image;
		this.mask = mask;
	}

	public ImgPlus<UnsignedByteType> crop() {
		ImgPlus<UnsignedByteType> output = new ImgPlus<UnsignedByteType>(
				new ArrayImgFactory<UnsignedByteType>().create(image,
						new UnsignedByteType()));

		return output;
	}

	private ImgPlus<UnsignedByteType> convertMask() {
		ImgPlus<UnsignedByteType> convertedMask = new ImgPlus<UnsignedByteType>(
				new ArrayImgFactory<UnsignedByteType>().create(mask,
						new UnsignedByteType()));
		
		// access the pixels of the output image
		final RandomAccess<UnsignedByteType> convertedAccess = convertedMask.randomAccess();

		// cursor over input image
		final Cursor<BitType> inCursor = mask.localizingCursor();

		// iterate over pixels of in input image
		while (inCursor.hasNext()) {
			inCursor.fwd();
			
			// set outaccess on position of incursor
			convertedAccess.setPosition(inCursor);

			int val = inCursor.get().getInteger();

			convertedAccess.get().set(val == 0 ? -1 : val);
		}

		return convertedMask;
	}

	public void createOutputTable(final ExecutionContext exec) {
		// start creating the output table which will house all the required
		// info this creates the schema for the output table
		DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(
				new String[] { "Image" }, new DataType[] { ImgPlusCell.TYPE });
		DataTableSpec tableSpecs = new DataTableSpec(columnSpecs);

		// actually create the table using the previously created schema
		outputBuffer = exec.createDataContainer(tableSpecs);
	}

	public BufferedDataTable getOutputTable(final ExecutionContext exec) {
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
