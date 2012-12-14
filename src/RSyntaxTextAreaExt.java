
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import javax.swing.UIManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RtfGenerator;
import org.fife.ui.rsyntaxtextarea.RtfTransferable;
import org.fife.ui.rsyntaxtextarea.Token;

public class RSyntaxTextAreaExt extends RSyntaxTextArea{
	boolean saved = false;
	String filePath = null;			//file path for saving file without dialog box
	Clipboard cb = null;
	
	public void copyAsRtf() {

		int selStart = getSelectionStart();
		int selEnd = getSelectionEnd();
		if (selStart==selEnd) {
			return;
		}

		// Make sure there is a system clipboard, and that we can write
		// to it.
		SecurityManager sm = System.getSecurityManager();
		if (sm!=null) {
			try {
				sm.checkSystemClipboardAccess();
			} catch (SecurityException se) {
				UIManager.getLookAndFeel().provideErrorFeedback(null);
				return;
			}
		}
		cb = Toolkit.getDefaultToolkit().getSystemClipboard();

		// Create the RTF selection.
		RtfGenerator gen = getRTFGenerator();
		Token tokenList = getTokenListFor(selStart, selEnd);
		for (Token t=tokenList; t!=null; t=t.getNextToken()) {
			if (t.isPaintable()) {
				if (t.textCount==1 && t.text[t.textOffset]=='\n') {
					gen.appendNewline();
				}
				else {
					Font font = getFontForTokenType(t.type);
					Color bg = getBackgroundForToken(t);
					boolean underline = getUnderlineForToken(t);
					// Small optimization - don't print fg color if this
					// is a whitespace color.  Saves on RTF size.
					if (t.isWhitespace()) {
						gen.appendToDocNoFG(t.getLexeme(), font, bg, underline);
					}
					else {
						Color fg = getForegroundForToken(t);
						gen.appendToDoc(t.getLexeme(), font, fg, bg, underline);
					}
				}
			}
		}

		// Set the system clipboard contents to the RTF selection.
		RtfTransferable contents = new RtfTransferable(gen.getRtf().getBytes());
		//System.out.println("*** " + new String(gen.getRtf().getBytes()));
		try {
			cb.setContents(contents, null);
		} catch (IllegalStateException ise) {
			UIManager.getLookAndFeel().provideErrorFeedback(null);
			return;
		}

	}

}
