import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.fife.ui.rtextarea.RTextScrollPane;


class prefListener implements ActionListener,Constants{
	
	Main frame=null;
	
	public prefListener(Main main) {
		frame = main;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for(int i=0;i<frame.jTabbedPane.getTabCount();i++){
			RTextScrollPane scroller = (RTextScrollPane)((JScrollPane)((frame.jTabbedPane.getComponentAt(i))));
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getComponentAt(i)))).getComponent(0)).getComponents();
			RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
			editorCur.setLineWrap(frame.wordWrap.getState());
			editorCur.setCodeFoldingEnabled(frame.codeFold.getState());
			scroller.setLineNumbersEnabled(frame.lineNumber.getState());
		}
	}

}
