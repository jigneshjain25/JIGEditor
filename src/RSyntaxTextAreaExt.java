
import java.awt.Font;
import java.io.File;
import java.util.HashMap;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class RSyntaxTextAreaExt extends RSyntaxTextArea implements Constants{
	
	//Main object required for updating language menu
	static Main obj;
	File file=null;
				
	RSyntaxTextAreaExt() {
		this.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		this.setFont(new Font("Courier New", Font.PLAIN, 13));
	}
	
	RSyntaxTextAreaExt(File file){
		this.file=file;
		this.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		this.setFont(new Font("Courier New", Font.PLAIN, 13));
		update(file);
	}

	
	void update(File file){
		this.file=file;
		this.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
		
		String result[]=(file.getName()).split("\\.");
		if(result.length>=2){
			
			String fileExtension=result[result.length-1].toLowerCase();
			if(EXTCODES.get(fileExtension)!=null)
				this.setSyntaxEditingStyle(EXTCODES.get(fileExtension));
		}
		//updating langcheckboxes
		String temp=this.getSyntaxEditingStyle();
		for(int i=0;i<STYLECODES.length;i++)
		{
			if(STYLECODES[i].equals(temp))
				obj.langs[i].setSelected(true);
			else
				obj.langs[i].setSelected(false);
		}
	}
	
}