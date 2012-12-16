import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

class langListener implements ActionListener,Constants{

		int codeNo;
		Main frame=null;

		public langListener(Main frame,int c) {
			this.frame=frame;
			codeNo=c;
		}

		public void actionPerformed(ActionEvent e){			

			// first clear all check boxes 
			for(int i=0;i<34;i++)
			{
				frame.langs[i].setSelected(false);
			}
			frame.langs[codeNo].setSelected(true);			
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);
        	editorCur.syntaxCode = codeNo;
        	editorCur.setSyntaxEditingStyle(STYLECODES[codeNo]);
		}
	}