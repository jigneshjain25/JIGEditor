import java.awt.Component;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

class CopyListener implements ActionListener{
	
	Main frame=null;
	
	public CopyListener(Main main) {
		frame = main;
	}
	
	public void actionPerformed(ActionEvent e){
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	editorCur.copyAsRtf();
	}
}
class cutListener implements ActionListener{
	
	Main frame=null;
	
	public cutListener(Main main) {
		frame = main;
	}
	
	public void actionPerformed(ActionEvent e){
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	editorCur.copyAsRtf();
		editorCur.replaceSelection("");	
	}
}
class pasteListener implements ActionListener{
	
	Main frame=null;
	
	public pasteListener(Main main) {
		frame = main;
	}
	
	public void actionPerformed(ActionEvent e){
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	editorCur.paste();
}
}
class FontListener implements ActionListener {
	
	Main frame=null;
	
	public FontListener(Main main) {
		frame = main;
	}

	public void actionPerformed(ActionEvent e)
	{
		/*FontDialog fontDialog = new FontDialog(frame.frame,"Font & Style",true,frame.jTabbedPane);*/
		
		JFontChooser fontChooser = new JFontChooser();
		int result = fontChooser.showDialog(frame.frame);
		if(result == JFontChooser.CANCEL_OPTION)return;
		Main.curFont = fontChooser.getSelectedFont();
		for(int i=0;i<frame.jTabbedPane.getTabCount();i++){
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getComponentAt(i)))).getComponent(0)).getComponents();
			RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
			editorCur.setFont(Main.curFont);
		}
		
	}
}

class undoListener implements ActionListener{
	
	Main frame=null;
	
	public undoListener(Main main) {
		frame = main;
	}
		public void actionPerformed(ActionEvent arg0) {
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);
        	editorCur.undoLastAction();
		}

	}
	class redoListener implements ActionListener{
		
		Main frame=null;
		
		public redoListener(Main main) {
			frame = main;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);  
        	editorCur.redoLastAction();
		}

	}