import java.awt.Component;
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
		try{		
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	for(int i=0;i<frame.langs.length;i++)
    		frame.langs[i].setSelected(false);
    	frame.langs[editorCur.StyleCodeNo].setSelected(true);
    	frame.lang.repaint();
    	if(editorCur.canRedo()) frame.redo.setEnabled(true);
    	else frame.redo.setEnabled(false);
    	if(editorCur.canUndo()) frame.undo.setEnabled(true);
    	else frame.undo.setEnabled(false);
    	
    	if(editorCur.file!=null){
    		try{	
				FileReader fileReader=new FileReader(editorCur.file);
				BufferedReader reader=new BufferedReader(fileReader);
				String line=null;
				String curText="";
				while((line=reader.readLine())!=null)
					curText+=line+"\n";
				
				if(!editorCur.getText().equals(curText)){
					int n=JOptionPane.showConfirmDialog(null, "This file is changed outside JIGEditor!\nDo you want to make changes in JIGEditor?","JIGEditor",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(n==JOptionPane.YES_OPTION){
						editorCur.setText(curText);
					}					
					else{
						saveFile(editorCur, editorCur.file);
					}
				}
			}catch(Exception ex){
				System.out.println("ERROR OPENING THE FILE");
				ex.printStackTrace();
			}
    	}    	
		}catch (Exception e) {
			e.printStackTrace();
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
