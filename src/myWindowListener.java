import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

public class myWindowListener implements WindowListener{
	
		Main frame = null;
		
		public myWindowListener(Main main) {
			frame=main;
		}

		public void windowOpened(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();
			RSyntaxTextAreaExt editorCur =(RSyntaxTextAreaExt) cp[0];
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
		}

		public void windowClosing(WindowEvent e) {
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