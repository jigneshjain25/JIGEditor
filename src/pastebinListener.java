import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.print.CancelablePrintJob;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;

import org.fife.ui.rsyntaxtextarea.RtfGenerator;
import org.fife.ui.rsyntaxtextarea.modes.CPlusPlusTokenMaker;


public class pastebinListener implements ActionListener {

	Main frame;
	
	public pastebinListener(Main o) {
		frame=o;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 		
		PasteBinDialog pad = new PasteBinDialog("PasteBin Uploader", frame.frame,editorCur);
		pad.getRootPane().setDefaultButton(pad.upload);
		pad.upload.requestFocus();
		pad.password.getCaret().setVisible(false);
		pad.username.getCaret().setVisible(false);
		pad.setVisible(true);
		
	}

}

class PasteBinDialog extends JDialog implements ActionListener{
	
	JTextField username = new JTextField(20);
	JPasswordField password = new JPasswordField(20);
	JTextField title = new JTextField(20);
	JComboBox format;
	JComboBox duration;
	JButton upload;
	JButton cancel;
	RSyntaxTextAreaExt editorCur;
	
	String[] life = {
			"Never","10 Minutes","1 Hour","1 Day","1 Month"
	};
	
	String[] parseLife = {
		"N","10M","1H","1D","1M"	
	};
	
	String[] formats = {
			"text","actionscript","6502acme","","c","clojure","cpp","csharp","css","delphi","dot","fortran","groovy","html5","java","javascript","jsp","latex","lisp","lua","make","mxml","perl","php","properties","python","ruby","sas","scala","sql","tcl","bash","winbatch","xml"        															
	};
	
	public PasteBinDialog(String name,JFrame frame,RSyntaxTextAreaExt editor){		
		super(frame,name,true);
		editorCur = editor;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	//	setLocation(400, 400);
		setLayout(new BorderLayout());
		format = new JComboBox(Constants.CHECKMENUCODES);
		duration = new JComboBox(life);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		JPanel panel11 = new JPanel();
		panel11.add(new JLabel("User Name: (Optional)"));
		panel11.add(username);
		JPanel panel12 = new JPanel();
		panel12.add(new JLabel("Password:   (Optional)"));
		panel12.add(password);		
		panel1.add(new JLabel(" (Enter username and password if you have Paste Bin Account)"),BorderLayout.NORTH);
		panel1.add(panel11,BorderLayout.CENTER);
		panel1.add(panel12,BorderLayout.SOUTH);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		JPanel panel21 = new JPanel();
		panel21.add(new JLabel(" Format: "));
		panel21.add(format);
		JPanel panel22 = new JPanel();
		panel22.add(new JLabel("Expiry Date:   "));
		panel22.add(duration);
		JPanel panel23 = new JPanel();
		panel23.add(new JLabel("Title:           (Optional)"));
		panel23.add(title);
		panel2.add(panel23,BorderLayout.NORTH);
		panel2.add(panel22,BorderLayout.CENTER);
		panel2.add(panel21,BorderLayout.SOUTH);
		JPanel panel3 = new JPanel();
		upload = new JButton("Upload");
		upload.addActionListener(this);		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		panel3.add(upload);
		panel3.add(cancel);
		add(panel1,BorderLayout.NORTH);
		add(panel2,BorderLayout.CENTER);
		add(panel3,BorderLayout.SOUTH);
		setSize(420,260);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(cancel==e.getSource()){
			dispose();
		}
		else{
			PasteBin pasteBin = new PasteBin("378ba5ea031e04378da541948087edb0");
			try {			
				if(!username.getText().equals("")){
				String response = pasteBin.login(username.getText(), password.getText());				
				if(response.substring(0, 15).equals("Bad API request")){ 
					dispose();
					JOptionPane.showMessageDialog(null, "Log In Failed");
				}
				else{					
				response = pasteBin.makePaste(editorCur.getText(), title.getText(), formats[format.getSelectedIndex()], parseLife[duration.getSelectedIndex()]);
				dispose();
				if(response.substring(0, 15).equals("Bad API request"))
					JOptionPane.showMessageDialog(null, "Uploading Failed!\nPlease try later!");
				else{										
					int op=JOptionPane.showConfirmDialog(null, "Your PasteBin post URL:\n"+response+"\nDo you want to copy URL?","JIGEditor Message",JOptionPane.YES_NO_OPTION);;
					if(op==JOptionPane.YES_OPTION){
						Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
						// Create the RTF selection.
						cb.setContents(new StringSelection(response),null);
					}
				}
				}
				}
				else{
					String response = pasteBin.makePaste(editorCur.getText(), title.getText(), formats[format.getSelectedIndex()], parseLife[duration.getSelectedIndex()]);
					dispose();
					if(response.substring(0, 15).equals("Bad API request"))
						JOptionPane.showMessageDialog(null, "Uploading Failed!\nPlease try later!");
					else{
						int op=JOptionPane.showConfirmDialog(null, "Your PasteBin post URL:\n"+response+"\nDo you want to copy URL?","JIGEditor Message",JOptionPane.YES_NO_OPTION);;
						if(op==JOptionPane.YES_OPTION){
							Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
							// Create the RTF selection.
							cb.setContents(new StringSelection(response),null);
						}
					}
				}				
			} catch (Exception e1) {			
				System.out.println("Error in connection!");
			}
		}
	}
	
}
