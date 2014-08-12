package uk.ac.sanger.mig.dicom.tests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import uk.ac.sanger.mig.dicom.obj.ImageWrapper;
import uk.ac.sanger.mig.dicom.obj.Point;

/**
 * Tests cases for different images, due to rounding need to check roughly, within 1px accuracy
 * @author pi1@sanger.ac.uk
 *
 */
public class Tester {


	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void centroidSquareAtSide() {
		ImageWrapper image = new ImageWrapper("test_images/whitesquareside.jpg", false);
		Point centroid = image.getCentroid();
		
		if (centroid.x() < 286 || centroid.x() > 288) {
			fail("Centroid X not equal");
		}
		
		if (centroid.y() < 509 || centroid.y() > 511) {
			fail("Centroid Y not equal");
		}
	}
	
	@Test
	public void centroidSquareAtCentre() {
		ImageWrapper image = new ImageWrapper("test_images/whitesquarecentre.jpg", false);
		Point centroid = image.getCentroid();
		
		if (centroid.x() < 400 || centroid.x() > 402) {
			fail("Centroid X not equal");
		}
		
		if (centroid.y() < 398 || centroid.y() > 400) {
			fail("Centroid Y not equal");
		}
	}
	
	@Test
	public void centroidColorSquareAtCentreSkewed() {
		ImageWrapper image = new ImageWrapper("test_images/colorsquare.jpg", false);
		Point centroid = image.getCentroid();
		
		if (centroid.x() < 399 || centroid.x() > 401) {
			fail("Centroid X not equal");
		}
		
		if (centroid.y() < 312 || centroid.y() > 314) {
			fail("Centroid Y not equal");
		}
	}
	
	@Test
	public void centroidRedSquareAtCentre25Deg() {
		ImageWrapper image = new ImageWrapper("test_images/redsquarecentre.jpg", false);
		Point centroid = image.getCentroid();
		
		if (centroid.x() < 341 || centroid.x() > 343) {
			fail("Centroid X not equal");
		}
		
		if (centroid.y() < 335 || centroid.y() > 337) {
			fail("Centroid Y not equal");
		}
	}

}
