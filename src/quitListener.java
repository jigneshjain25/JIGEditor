import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class quitListener implements ActionListener{
	
		Main frame=null;
		
		public quitListener(Main main) {
			frame = main;
		}
		
		public void actionPerformed(ActionEvent e){
			
			for(int i=0;i<frame.jTabbedPane.getTabCount();i++)
			{
				Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getComponentAt(i)))).getComponent(0)).getComponents();
				RSyntaxTextAreaExt editorCur =(RSyntaxTextAreaExt) cp[0];
				if(editorCur.changed)
				{
					int n=JOptionPane.showConfirmDialog(null, "Save Changes to "+frame.jTabbedPane.getTitleAt(i)+" before closing ?","JIGEditor",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if(n==JOptionPane.YES_OPTION)
					{
						
    					if(editorCur.file==null){
    					int val = frame.fileSave.showSaveDialog(frame.frame);
    		        		if(val==JFileChooser.APPROVE_OPTION && frame.fileSave.getSelectedFile()!=null)
    		        			editorCur.file=frame.fileSave.getSelectedFile();
    					}
    				    try
    				    {
	    					BufferedWriter writer=new BufferedWriter(new FileWriter(editorCur.file));
	    					writer.write(editorCur.getText());
	    					writer.close();
    					}catch(Exception e1){
    						e1.printStackTrace();
    					}
    					
					}
					if(n==JOptionPane.CANCEL_OPTION)return;
				}
				frame.jTabbedPane.remove(i);
				i--;
			}
			//frame.frame.dispose();
			System.exit(0);
		}

	}