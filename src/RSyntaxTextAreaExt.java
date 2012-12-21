import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class RSyntaxTextAreaExt extends RSyntaxTextArea implements Constants, DocumentListener{

	//Main object required for updating language menu
	static Main obj;
	public File file=null;
	int StyleCodeNo;
	boolean changed=false;	
		
	RSyntaxTextAreaExt() {		
		StyleCodeNo=0;
		setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		setFont(new Font("Courier New", Font.PLAIN, 13));
		setCodeFoldingEnabled(true);
		setAnimateBracketMatching(true);		
		getDocument().addDocumentListener(this);		
	}

	RSyntaxTextAreaExt(File file){
		this();
		update(file);
	}

	void update(File file){
		this.file=file;							//do not delete this line, update is called somewhere else too
		setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);

		String result[]=(file.getName()).split("\\.");
		
		if(result.length>=2)
		{
			String fileExtension=result[result.length-1].toLowerCase();
			String temp=EXTCODES.get(fileExtension);
			if(temp!=null)
			{
				setSyntaxEditingStyle(temp);
				for(int i=0;i<STYLECODES.length;i++)
					if(STYLECODES[i].equals(temp))
					{
						StyleCodeNo=i;
						break;
					}
			}
				
		}
		
		//updating langcheckboxes
		for(int i=0;i<obj.langs.length;i++)
    		obj.langs[i].setSelected(false);
    	obj.langs[StyleCodeNo].setSelected(true);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		changed=true;		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changed=true;				
		obj.undo.setEnabled(true);
		if(this.canRedo()) obj.redo.setEnabled(true);
		else obj.redo.setEnabled(false);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changed=true;	
		obj.redo.setEnabled(true);
		if(this.canUndo()) obj.undo.setEnabled(true);
		else obj.undo.setEnabled(false);
	}
}

