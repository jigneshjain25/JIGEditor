import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.PlainDocument;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rtextarea.RTextScrollPane;


public class openListener implements ActionListener, Constants {
	
	Main frame=null;
	
	public openListener(Main main) {
		frame = main;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		frame.fileOpen.showOpenDialog(frame.frame);
		try {
			openFile(frame.fileOpen.getSelectedFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}			
	}

	void openFile(File file) throws IOException{
		RSyntaxTextAreaExt editorCur=new RSyntaxTextAreaExt(file);
		editorCur.setFont(Main.curFont);
		RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
		scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ButtonTabComponent btc = new ButtonTabComponent(frame.jTabbedPane);
		frame.jTabbedPane.add(file.getName(),scrollerCur); 
		frame.jTabbedPane.setTabComponentAt(frame.jTabbedPane.getTabCount()-1, btc);
		frame.jTabbedPane.setSelectedIndex(frame.jTabbedPane.getTabCount()-1);	        						     
		
		FileInputStream fis = new FileInputStream(file); 
		byte[] b =new byte[6];
		fis.read(b);		
		fis.close();		
		if(new String(b).equals(new String(Constants.keyEncr))){
			JOptionPane.showMessageDialog(frame.frame, "File is encrypted!\nFor decryption go to\nPreferences->Security->Decrypt", "Security Message", JOptionPane.OK_OPTION);
			
			FileInputStream fis1 = new FileInputStream(file); 
			byte[] str = new byte[1024];
			fis1.read(str);
			editorCur.append(new String(str,6,(str.length-6)));
			while(fis1.read(str)!=-1)			
			{editorCur.append(new String(str,0,str.length));}
			editorCur.setEditable(false);
			fis.close();
			
			}
		else {					
		try{	
				FileReader fileReader=new FileReader(file);
				BufferedReader reader=new BufferedReader(fileReader);
				String line=null;
				while((line=reader.readLine())!=null)
					editorCur.append(line+"\n");
				
			}catch(Exception ex){
				System.out.println("ERROR OPENING THE FILE");
				ex.printStackTrace();
			}
		RSyntaxDocument doc = (RSyntaxDocument)editorCur.getDocument();
		if (doc instanceof PlainDocument) {
			((AbstractDocument) doc).putProperty(PlainDocument.tabSizeAttribute, frame.curTabWidth);				
		}
		}
		editorCur.requestFocusInWindow();
		frame.undo.setEnabled(false);
    	frame.redo.setEnabled(false);
		editorCur.changed=false;
				
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
