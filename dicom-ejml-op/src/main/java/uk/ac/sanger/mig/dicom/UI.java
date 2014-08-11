package uk.ac.sanger.mig.dicom;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class UI extends JFrame {

	private static final long serialVersionUID = 8914389830504353108L;

	public UI() {
		
        setTitle("DICOM X-RAY");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new DicomEJML());

        setSize(800, 800);
        setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) throws IOException {
	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                UI ui = new UI();
                ui.setVisible(true);
            }
        });
	}
}
