import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

class SecurityListener implements ActionListener,Constants{
	
	Main frame = null;
	String key = "JIGEditor";
	
	Cipher ecipher;
    Cipher dcipher;
		
	public SecurityListener(Main main) {
		frame = main;		
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
    	String plain = editorCur.getText();
    	 
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
    	byte[] input=plain.getBytes();
		
    	byte[] keyBytes = null;    
    	
    	SecretKeySpec key;
    	
    	boolean failure = true;
    	boolean decr = false;

    	Cipher cipher;
			try {
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");			
    	  
			if(e.getActionCommand().equals("Encrypt")){
	    	//encryption pass			
				
    	    	if(editorCur.file==null){
    	    		JOptionPane.showMessageDialog(frame.frame, "File muste be saved for Encryption", "Caution", JOptionPane.OK_OPTION);
    	    		int val = frame.fileSave.showSaveDialog(frame.frame);
    	    		if(val==JFileChooser.APPROVE_OPTION && frame.fileSave.getSelectedFile()!=null)				
    	    			saveFile(editorCur,frame.fileSave.getSelectedFile());
    	    	}
    			else     			        		
    				saveFile(editorCur,editorCur.file);
    	    	
    	    	if(editorCur.file==null)
    	    		JOptionPane.showMessageDialog(frame.frame, "File muste be saved for Encryption", "Error", JOptionPane.ERROR_MESSAGE);
    	    	else{
    	    	String str = JOptionPane.showInputDialog(null, "Enter your key (must be of at least 16 charecters) : ");
    	    	if(str!=null) {
    	    		keyBytes = str.getBytes();        	    	
        	    	key = new SecretKeySpec(keyBytes, 0, 16, "AES");        	    	
        	    	cipher.init(Cipher.ENCRYPT_MODE, key);
    				byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    				int ctLength = cipher.update(input, 0, input.length, cipherText, 0);				
        	    	ctLength += cipher.doFinal(cipherText, ctLength);  
    	    		FileOutputStream fos = new FileOutputStream(editorCur.file.getAbsoluteFile());
    	    		fos.write(keyEncr);    	    		
        	    	fos.write(cipherText);
        	    	editorCur.setText(new String(cipherText));  
        	    	editorCur.changed = false;       	    			
        	    	editorCur.setEditable(false);
    	    	}
    	    	else JOptionPane.showMessageDialog(frame.frame, "Key is required for Encryption", "Error", JOptionPane.ERROR_MESSAGE);     	    	
    	    	}
			}
			else{
    	    // decryption pass		
				
				decr = true;
				String str = JOptionPane.showInputDialog(null, "Enter your key : ");
    	    	if(str!=null) {    	    		
        	    	keyBytes = str.getBytes();        	    	
        	    	key = new SecretKeySpec(keyBytes,0,16, "AES");        	    	
    	    		FileInputStream fis = new FileInputStream(editorCur.file.getAbsoluteFile());
    	    		//fis.read(input, 0, keyValues.length);
    				int ctLength = fis.read(input); // System.out.println(ctLength);  	
    				ctLength-=keyEncr.length;//System.out.println(ctLength);
    				cipher.init(Cipher.DECRYPT_MODE, key);    	  											    				
    				byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
    				int ptLength = cipher.update(input, 6, ctLength, plainText, 0);    				
    				ptLength += cipher.doFinal(plainText, ptLength);    				
    				editorCur.setText(new String(plainText,0,ptLength));
    				failure = false;
    				saveFile(editorCur, editorCur.file);
    				editorCur.setEditable(true);
    				editorCur.changed = false;
    	    	}
    	    	else JOptionPane.showMessageDialog(frame.frame, "Key is required for Decryption", "Error", JOptionPane.ERROR_MESSAGE);    	    	    	    	
			}
			} catch (Exception e1) {				
				e1.printStackTrace();
			} 
			if(decr&&failure) JOptionPane.showMessageDialog(frame.frame, "Wrong key entered!", "Error", JOptionPane.ERROR_MESSAGE);			
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
