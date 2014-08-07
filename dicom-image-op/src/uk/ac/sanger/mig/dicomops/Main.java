package uk.ac.sanger.mig.dicomops;

import ij.io.FileSaver;
import io.scif.config.SCIFIOConfig;
import io.scif.config.SCIFIOConfig.ImgMode;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import java.io.File;

import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class Main {

	@SuppressWarnings({ "unchecked", "unused" })
	public static <T extends RealType<T> & NativeType<T>> void main(String[] args) throws ImgIOException {
		final ImageJ ij = new ImageJ();

		// define the file to open
		File file = new File("small.jpg");
		
		SCIFIOConfig config = new SCIFIOConfig();
		
		config.imgOpenerSetImgModes(ImgMode.ARRAY);

		ImgOpener imgOpener = new ImgOpener();

		Img<T> imp = (Img<T>) imgOpener.openImgs(file.getAbsolutePath(), config).get(0).getImg();
		
		Object object = ij.op().invert(imp, imp);
		
		

//		System.out.println(imp.getClass().toString());
//		
//		for (UnsignedShortType t : imp) {
//			System.out.println(t.);
//		}

		final int cols = (int) imp.dimension(0);
		final int rows = (int) imp.dimension(1);
		
//		ArrayImg<T> x = new ArrayImg<T>();
		
		int area = (int) imp.size();
		
		Img<T> imgT = (Img<T>) imp;

		FileSaver saver = new FileSaver(ImageJFunctions.wrap((RandomAccessibleInterval<T>) imgT, "he"));
		saver.saveAsJpeg("xray.jpg");

//		ij.op().add(imp, 500.0);

//		ij.ui().showUI();
//		ij.ui().show("dicom", imp);

		// how many ops?
		// final int opCount = ij.command().getCommandsOfType(Op.class).size();
		// ij.log().info("Found " + opCount + " ops");
		//
		// // learn about an op
		// ij.log().info(ij.op().help("invert"));
		//
		// // add two numbers
		// final Object seven = ij.op().add(2, 5);
		// ij.log().info("What is 2 + 5? " + seven);
		//
		// // create a new blank image
		// final long[] dims = { 150, 100 };
		// final Object blank = ij.op().create(dims);
		//
		// // fill in the image with a sinusoid using a formula
		// final String formula = "10 * (Math.cos(0.3*p[0]) + Math.sin(0.3*p[1]))";
		// final Object sinusoid = ij.op().equation(blank, formula);
		//
		// // add a constant value to an image
		// ij.op().add(sinusoid, 25.0);
		//
		// // generate a gradient image using a formula
		// final Object gradient = ij.op().equation(ij.op().create(dims), "p[0]+p[1]");
		//
		// // add the two images
		// final Object composite = ij.op().add(sinusoid, gradient);
		//
		// // dump the image to the console
		// final Object ascii = ij.op().ascii(composite);
		// ij.log().info("Composite image:\n" + ascii);
		//
		// // show the image in a window
		// ij.ui().showUI();
		// ij.ui().show("composite", composite);
		//
		// // execute an op on every pixel of an image
		// final Op addOp = ij.op().op("add", DoubleType.class, new DoubleType(5.0));
		// ij.op().map(composite, composite, addOp);
	}

}
