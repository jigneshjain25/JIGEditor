import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;


public class closeListener implements ActionListener, Constants {
	
	Main frame=null;
	
	public closeListener(Main main) {
		frame = main;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
		RSyntaxTextAreaExt editorCur =(RSyntaxTextAreaExt) cp[0];
		//check if user has saved the file before closing it
		if(editorCur.changed)
		{
			int n=JOptionPane.showConfirmDialog(null, "Save Changes to "+frame.jTabbedPane.getTitleAt(frame.jTabbedPane.getSelectedIndex())+" before closing ?","JIGEditor",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(n==JOptionPane.YES_OPTION)
			{
				
				if(editorCur.file==null){
	        		int val = frame.fileSave.showSaveDialog(frame.frame);
	        		if(val==JFileChooser.APPROVE_OPTION && frame.fileSave.getSelectedFile()!=null)				
	        			saveFile(editorCur,frame.fileSave.getSelectedFile());
	        	}
				else       			        		
					saveFile(editorCur,frame.editor.file);  
				
				frame.jTabbedPane.remove(frame.jTabbedPane.getSelectedIndex());
			}
			
			else if(n==JOptionPane.NO_OPTION)
				frame.jTabbedPane.remove(frame.jTabbedPane.getSelectedIndex());
		}
		else
			frame.jTabbedPane.remove(frame.jTabbedPane.getSelectedIndex());
		
	}
	
	void saveFile(RSyntaxTextAreaExt editorCur,File file){
		try{
			frame.jTabbedPane.setTitleAt(frame.jTabbedPane.getSelectedIndex(), file.getName());
			//Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	//RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.update(file);
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(editorCur.getText());
			writer.close();
			editorCur.changed=false;
			
		}catch(Exception ex){
			System.out.println("ERROR WRITING THE FILE");
			ex.printStackTrace();
		}
	}
}
