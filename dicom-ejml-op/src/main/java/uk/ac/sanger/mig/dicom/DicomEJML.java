package uk.ac.sanger.mig.dicom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import uk.ac.sanger.mig.dicom.helpers.MathHelper;
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
	
	public ImageWrapper image;
	
	public DicomEJML() {
		image = new ImageWrapper("test_images/colorsquare.jpg");
		
		Line centralAxis = image.centralAxis();
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        Image drawImg = image.getOriginalImage();
        double angle = MathHelper.angleToHor(image.getCentralAxis().to());
       
        g2d.rotate(Math.toRadians(angle), drawImg.getWidth(this) / 2, drawImg.getHeight(this) / 2);
        g2d.drawImage(drawImg, 0, 0, this);
        
        g2d.dispose();
    }
}
