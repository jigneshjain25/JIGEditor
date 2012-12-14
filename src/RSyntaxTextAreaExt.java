
import java.awt.Font;
import java.io.File;
import java.util.HashMap;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class RSyntaxTextAreaExt extends RSyntaxTextArea implements Constants{
	
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
	}
	
}