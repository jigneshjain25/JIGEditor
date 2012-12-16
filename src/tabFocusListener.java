import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.fife.ui.rtextarea.RTextScrollPane;


public class tabFocusListener implements FocusListener {

	Main frame;
	public tabFocusListener(Main o) {
		frame=o;
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		try{
		
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	for(int i=0;i<frame.langs.length;i++)
    		frame.langs[i].setSelected(false);
    	frame.langs[editorCur.StyleCodeNo].setSelected(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

}
