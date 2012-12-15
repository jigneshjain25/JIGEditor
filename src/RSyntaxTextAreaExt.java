import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class RSyntaxTextAreaExt extends RSyntaxTextArea implements Constants, DocumentListener{

	//Main object required for updating language menu
	static Main obj;
	File file=null;
	int syntaxCode = 0;		//Syntax code for language of editor
	
	//used for detecting if the contents of a  file are changed
	boolean changed;

	RSyntaxTextAreaExt() {
		
		this.getDocument().addDocumentListener(this);
		this.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		this.setFont(new Font("Courier New", Font.PLAIN, 13));
		this.setCodeFoldingEnabled(true);
		this.setAnimateBracketMatching(true);
		changed=false;
	}

	RSyntaxTextAreaExt(File file){
		this.file=file;
		this.setFont(new Font("Courier New", Font.PLAIN, 13));
		this.setCodeFoldingEnabled(true);
		this.setAnimateBracketMatching(true);
		update(file);
		changed=false;
		this.getDocument().addDocumentListener(this);
	}


	void update(File file){
		this.file=file;							//do not delete this line, update is called somewhere else too
		this.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);

		String result[]=(file.getName()).split("\\.");
		if(result.length>=2){

			String fileExtension=result[result.length-1].toLowerCase();
			if(EXTCODES.get(fileExtension)!=null)
				this.setSyntaxEditingStyle(EXTCODES.get(fileExtension));
		}
	/*	//updating langcheckboxes
		String temp=this.getSyntaxEditingStyle();
		for(int i=0;i<STYLECODES.length;i++)
		{
			if(STYLECODES[i].equals(temp))
				obj.langs[i].setSelected(true);
			else
				obj.langs[i].setSelected(false);
		}*/
	}
	
	public void changedUpdate(DocumentEvent e) {
		changed=true;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changed=true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changed=true;
	}

}