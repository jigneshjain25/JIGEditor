import java.awt.Component;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JOptionPane;
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
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);    	
    	if(editorCur.file!=null){    		
    		long mod = editorCur.file.lastModified();
    		if(mod>editorCur.lastMod){
    			int n=JOptionPane.showConfirmDialog(null, "This file is changed outside JIGEditor!\nDo you want to make changes in JIGEditor?","JIGEditor",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(n==JOptionPane.YES_OPTION){
					String curText = "";
					FileReader fileReader;
					try {
						fileReader = new FileReader(editorCur.file);
					
					BufferedReader reader=new BufferedReader(fileReader);
					String line=null;
					while((line=reader.readLine())!=null)
						curText+=line+"\n";	
					editorCur.setText(curText);	
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}				
    		}
    	}		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		
	}

	void saveFile(RSyntaxTextAreaExt editorCur,File file){
		try{				
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(editorCur.getText());
			writer.close();
			editorCur.changed=false;
			
		}catch(Exception ex){
			System.out.println("Error saving the file");				
		}
	}
}
