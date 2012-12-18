import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.print.*;

import javax.swing.JOptionPane;

class printListener implements ActionListener {

	Main frame = null;	
	
	public printListener(Main main) {
		frame = main;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		PrinterJob job =PrinterJob.getPrinterJob();
		job.setPrintable(new JIGPrinter());
		boolean doPrint = job.printDialog();
		if (doPrint) {
		    try {
		        job.print();
		    } catch (PrinterException e) {
		        JOptionPane.showMessageDialog(frame.frame, "Printing failed!", "Error", JOptionPane.ERROR_MESSAGE);
		    }
		}
	}

}

class JIGPrinter implements Printable {
	
	private final double INCH = 72;

	public int print(Graphics g, PageFormat pageFormat, int page) throws PrinterException {

		int i;
	    Graphics2D g2d;
	    Line2D.Double line = new Line2D.Double();

	    //--- Validate the page number, we only print the first page
	    if (page == 0) {  //--- Create a graphic2D object a set the default parameters
	      g2d = (Graphics2D) g;
	      g2d.setColor(Color.black);

	      //--- Translate the origin to be (0,0)
	      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

	      //--- Print the vertical lines
	      for (i = 0; i < pageFormat.getWidth(); i += INCH / 2) {
	        line.setLine(i, 0, i, pageFormat.getHeight());
	        g2d.draw(line);
	      }

	      //--- Print the horizontal lines
	      for (i = 0; i < pageFormat.getHeight(); i += INCH / 2) {
	        line.setLine(0, i, pageFormat.getWidth(), i);
	        g2d.draw(line);
	      }

	      return (PAGE_EXISTS);
	    } else
	      return (NO_SUCH_PAGE);
	}
	
}