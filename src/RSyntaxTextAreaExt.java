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

import org.fife.ui.autocomplete.*;

public class RSyntaxTextAreaExt extends RSyntaxTextArea implements Constants, DocumentListener{

	//Main object required for updating language menu
	static Main obj;
	File file=null;
	int StyleCodeNo;
	boolean changed=false;
		
	RSyntaxTextAreaExt() {
		StyleCodeNo=0;
		setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		setFont(new Font("Courier New", Font.PLAIN, 13));
		setCodeFoldingEnabled(true);
		setAnimateBracketMatching(true);
		
		CompletionProvider provider = createCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
	    ac.install(this);
				
		getDocument().addDocumentListener(this);
	}

	private CompletionProvider createCompletionProvider() {
		
		DefaultCompletionProvider provider = new DefaultCompletionProvider();
		
		  provider.addCompletion(new BasicCompletion(provider, "abstract"));
	      provider.addCompletion(new BasicCompletion(provider, "assert"));
	      provider.addCompletion(new BasicCompletion(provider, "break"));
	      provider.addCompletion(new BasicCompletion(provider, "case"));
	      provider.addCompletion(new BasicCompletion(provider, "catch"));
	      provider.addCompletion(new BasicCompletion(provider, "class"));
	      provider.addCompletion(new BasicCompletion(provider, "const"));
	      provider.addCompletion(new BasicCompletion(provider, "continue"));
	      provider.addCompletion(new BasicCompletion(provider, "default"));
	      provider.addCompletion(new BasicCompletion(provider, "do"));
	      provider.addCompletion(new BasicCompletion(provider, "else"));
	      provider.addCompletion(new BasicCompletion(provider, "enum"));
	      provider.addCompletion(new BasicCompletion(provider, "extends"));
	      provider.addCompletion(new BasicCompletion(provider, "final"));
	      provider.addCompletion(new BasicCompletion(provider, "finally"));
	      provider.addCompletion(new BasicCompletion(provider, "for"));
	      provider.addCompletion(new BasicCompletion(provider, "goto"));
	      provider.addCompletion(new BasicCompletion(provider, "if"));
	      provider.addCompletion(new BasicCompletion(provider, "implements"));
	      provider.addCompletion(new BasicCompletion(provider, "import"));
	      provider.addCompletion(new BasicCompletion(provider, "instanceof"));
	      provider.addCompletion(new BasicCompletion(provider, "interface"));
	      provider.addCompletion(new BasicCompletion(provider, "native"));
	      provider.addCompletion(new BasicCompletion(provider, "new"));
	      provider.addCompletion(new BasicCompletion(provider, "package"));
	      provider.addCompletion(new BasicCompletion(provider, "private"));
	      provider.addCompletion(new BasicCompletion(provider, "protected"));
	      provider.addCompletion(new BasicCompletion(provider, "public"));
	      provider.addCompletion(new BasicCompletion(provider, "return"));
	      provider.addCompletion(new BasicCompletion(provider, "static"));
	      provider.addCompletion(new BasicCompletion(provider, "strictfp"));
	      provider.addCompletion(new BasicCompletion(provider, "super"));
	      provider.addCompletion(new BasicCompletion(provider, "switch"));
	      provider.addCompletion(new BasicCompletion(provider, "synchronized"));
	      provider.addCompletion(new BasicCompletion(provider, "this"));
	      provider.addCompletion(new BasicCompletion(provider, "throw"));
	      provider.addCompletion(new BasicCompletion(provider, "throws"));
	      provider.addCompletion(new BasicCompletion(provider, "transient"));
	      provider.addCompletion(new BasicCompletion(provider, "try"));
	      provider.addCompletion(new BasicCompletion(provider, "void"));
	      provider.addCompletion(new BasicCompletion(provider, "volatile"));
	      provider.addCompletion(new BasicCompletion(provider, "while"));
	      
	      provider.addCompletion(new ShorthandCompletion(provider, "syso",
	              "System.out.println();", "System.out.println();"));
	        provider.addCompletion(new ShorthandCompletion(provider, "syse",
	              "System.err.println();", "System.err.println();"));

	        return provider;
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