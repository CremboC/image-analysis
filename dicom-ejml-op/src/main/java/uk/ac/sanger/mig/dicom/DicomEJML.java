package uk.ac.sanger.mig.dicom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import uk.ac.sanger.mig.dicom.obj.ImageWrapper;
import uk.ac.sanger.mig.dicom.obj.Line;

/**
 * Matlab -> Java test
 * 
 * @author pi1 pi1@sanger.ac.uk
 *
 */
public class DicomEJML extends JPanel {

	private static final long serialVersionUID = 3951194747959982274L;

	public final static double normalizationFactor = 1000;
	public final static double MAGIC_NUMBER = 90;

	private ImageWrapper image;
	private double angle;

	public DicomEJML() {
		image = new ImageWrapper("test_images/largentail.jpg", false);
		image.centralAxis();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		final Image drawImg = image.getOriginalImage();
		final Line axis = image.getCentralAxis();

		g2d.rotate(Math.toRadians(MAGIC_NUMBER) - image.getThetamin(), drawImg.getWidth(this) / 2, drawImg.getHeight(this) / 2);
		g2d.drawImage(drawImg, 0, 0, this);
		
		g2d.drawLine((int) axis.from().x(), 
				(int) axis.from().y(), 
				(int) axis.to().x(), 
				(int) axis.to().y()
				);

		g2d.dispose();
	}
}
