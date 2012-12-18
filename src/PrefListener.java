import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.text.AbstractDocument;
import javax.swing.text.PlainDocument;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
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

class TabSizeListener implements ActionListener,Constants{
	
	Main frame = null;
	int size = 8;
	
	public TabSizeListener(Main main,int s) {
		frame = main;
		size = s;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i=0;i<frame.jTabbedPane.getTabCount();i++){
			for(int j=0;j<16;j++)
			{
				frame.tabSizes[j].setSelected(false);
			}
			frame.tabSizes[size-1].setSelected(true);
			frame.curTabWidth = size;
			RTextScrollPane scroller = (RTextScrollPane)((JScrollPane)((frame.jTabbedPane.getComponentAt(i))));
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getComponentAt(i)))).getComponent(0)).getComponents();
			RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
			RSyntaxDocument doc = (RSyntaxDocument)editorCur.getDocument();
			if (doc instanceof PlainDocument) {
				((AbstractDocument) doc).putProperty(PlainDocument.tabSizeAttribute, size);				
			}
		}
	}
}