import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JViewport;


public class saveListener implements ActionListener, Constants {

	Main frame=null;
	
	public saveListener(Main main) {
		frame=main;
	}
	@Override
	public void actionPerformed(ActionEvent e){
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	if(editorCur.file==null){
    		int val = frame.fileSave.showSaveDialog(frame.frame);
    		if(val==JFileChooser.APPROVE_OPTION && frame.fileSave.getSelectedFile()!=null)				
    			saveFile(editorCur,frame.fileSave.getSelectedFile());
    	}
		else if(editorCur.changed)        			        		
			saveFile(editorCur,editorCur.file);     				        		
    	
	}

	void saveFile(RSyntaxTextAreaExt editorCur,File file){
		try{
			frame.jTabbedPane.setTitleAt(frame.jTabbedPane.getSelectedIndex(), file.getName());
			//Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        //	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.update(file);
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(editorCur.getText());
			writer.close();
			editorCur.changed=false;
			
		}catch(Exception ex){
			System.out.println("Error saving the file");
			ex.printStackTrace();
		}
	}
}

class saveAsListener implements ActionListener{
	
	Main frame=null;
	
	public saveAsListener(Main main) {
		frame=main;
	}
	
	public void actionPerformed(ActionEvent e){			
		int val = frame.fileSave.showSaveDialog(frame.frame);
		if(val==JFileChooser.APPROVE_OPTION && frame.fileSave.getSelectedFile()!=null)				
			saveFile(frame.fileSave.getSelectedFile());
	}
	
	void saveFile(File file){
		try{
			frame.jTabbedPane.setTitleAt(frame.jTabbedPane.getSelectedIndex(), file.getName());
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.update(file);
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(editorCur.getText());
			writer.close();
			editorCur.changed=false;
			
		}catch(Exception ex){
			System.out.println("Error saving the file");
			ex.printStackTrace();
		}
	}
}


