package uk.ac.sanger.mig.dicom.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import uk.ac.sanger.mig.dicom.DicomEJML;
import uk.ac.sanger.mig.dicom.obj.ImageWrapper;
import uk.ac.sanger.mig.dicom.obj.Point;

public class Tester {

	private DicomEJML ej;

	@Before
	public void setUp() throws Exception {
		ej = new DicomEJML();
	}
	
	@Test
	public void centroidSquareAtSide() {
		ImageWrapper image = new ImageWrapper("test_images/whitesquareside.jpg");
		Point centroid = image.getCentroid();
		
		assertEquals(new Point(287.0,  510.0), centroid);
	}
	
	@Test
	public void centroidSquareAtCentre() {
		ImageWrapper image = new ImageWrapper("test_images/whitesquarecentre.jpg");
		Point centroid = image.getCentroid();
		
		assertEquals(new Point(401.0, 399.0), centroid);
	}
	
	@Test
	public void centroidColorSquareAtCentreSkewed() {
		ImageWrapper image = new ImageWrapper("test_images/colorsquare.jpg");
		Point centroid = image.getCentroid();
		
		assertEquals(new Point(400.0, 313.0), centroid);
	}
	
	@Test
	public void centroidRedSquareAtCentre25Deg() {
		ImageWrapper image = new ImageWrapper("test_images/redsquarecentre.jpg");
		Point centroid = image.getCentroid();
		
		assertEquals(new Point(342.0, 336.0), centroid);
	}

}
