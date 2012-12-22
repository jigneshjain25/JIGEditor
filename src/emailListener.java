import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;


public class emailListener implements ActionListener {
	Main frame;
	
	public emailListener(Main o) {
		frame=o;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);
    	String title = frame.jTabbedPane.getTitleAt(frame.jTabbedPane.getSelectedIndex());
		String message = editorCur.getText();
		String recipientEmail=(String)JOptionPane.showInputDialog(frame.frame, "Enter your Email ID", "JIGEditor", JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if(recipientEmail==null)return;
		try {
			GoogleMail.Send("jigeditor", "assignmnt", recipientEmail, title, message);
			JOptionPane.showMessageDialog(frame.frame, "Mail sent successfully !", "JIGEditor", JOptionPane.INFORMATION_MESSAGE, null);

		} catch (AddressException e1) {
			JOptionPane.showMessageDialog(frame.frame, "Invalid Email Address", "JIGEditor", JOptionPane.WARNING_MESSAGE, null);
			e1.printStackTrace();
		} catch (MessagingException e1) {
			JOptionPane.showMessageDialog(frame.frame, "Unable to send email, try later !", "JIGEditor", JOptionPane.WARNING_MESSAGE, null);
			e1.printStackTrace();
		}
	}

}
