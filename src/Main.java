import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.GoToMatchingBracketAction;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.RecordableTextAction;

public class Main {
//testing github -- code sharing	
	//used by langListener to determine highlighting language
	final String[] STYLECODES={
			"text/plain","text/actionscript",
			"text/asm","text/bbcode","text/c","text/clojure",
			"text/cpp","text/cs","text/css","text/delphi",
			"text/dtd","text/fortran","text/groovy","text/html",
			"text/java","text/javascript","text/jsp","text/latex",
			"text/lisp","text/lua","text/makefile","text/mxml",
			"text/perl","text/php","text/properties","text/python",
			"text/ruby","text/sas","text/scala","text/sql","text/tcl",
			"text/unix","text/bat","text/xml"
	};

	//used for supplying strings when creating JCheckBoxMenuItem
	final String[] CHECKMENUCODES= {
			"NONE",	"ACTIONSCRIPT",	"ASSEMBLER_X86","BBCODE","C",
			"CLOJURE","CPLUSPLUS","CSHARP",	"CSS","DELPHI","DTD",
			"FORTRAN","GROOVY",	"HTML",	"JAVA",	"JAVASCRIPT","JSP",
			"LATEX","LISP",	"LUA",	"MAKEFILE",	"MXML",	"PERL",
			"PHP","PROPERTIES_FILE","PYTHON","RUBY","SAS","SCALA","SQL",	
			"TCL",	"UNIX_SHELL","WINDOWS_BATCH","XML"
	};

	boolean fileSaved=false;

	JTabbedPane jTabbedPane=new JTabbedPane();
	RSyntaxTextAreaExt editor=new RSyntaxTextAreaExt();
	RTextScrollPane scroller = new RTextScrollPane(editor);

	JFrame frame=new JFrame("JIGEditor");
	
	JMenuBar myMenu=new JMenuBar();
	java.awt.Font curFont = new Font("Courier New", Font.PLAIN, 13);
	JMenu file=new JMenu("File");	
	JMenuItem neW=new JMenuItem("New");
	JMenuItem open=new JMenuItem("Open");
	JMenuItem save=new JMenuItem("Save");
	JMenuItem saveAs=new JMenuItem("Save As");
	JMenuItem close=new JMenuItem("Close");
	JMenuItem quit=new JMenuItem("Quit");
	
	JMenu edit=new JMenu("Edit");
	JMenuItem Copy=new JMenuItem("Copy");
	JMenuItem cut=new JMenuItem("Cut");
	JMenuItem paste=new JMenuItem("Paste");
	JMenuItem FontMenu = new JMenuItem ("Font");

	JMenu lang=new JMenu("Language");
	JCheckBoxMenuItem[] langs=new JCheckBoxMenuItem[34];
		
	JComboBox fontCombo;

	JFileChooser fileSave=new JFileChooser();
	JFileChooser fileOpen=new JFileChooser();

	public static void main(String[] args) {
		new Main().go();		
	}

	void go(){

		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));   // Ctrl + s 
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));   // Ctrl + Shift + s
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,ActionEvent.ALT_MASK));   // Alt + F4
		neW.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));    // Ctrl + n
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));  // Ctrl + w
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));   // Ctrl + o
		Copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));	//Ctrl + c
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));	//Ctrl + x
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));	//Ctrl + v

		neW.addActionListener(new neWListener());
		open.addActionListener(new openListener());
		save.addActionListener(new saveListener());
		saveAs.addActionListener(new saveAsListener());
		close.addActionListener(new closeListener());
		quit.addActionListener(new quitListener());
		Copy.addActionListener(new CopyListener());
		cut.addActionListener(new cutListener());
		paste.addActionListener(new pasteListener());
		FontMenu.addActionListener(new FontListener());

		file.setMnemonic('f');		//opens file menu when user presses Alt + f
		file.add(neW);
		file.add(open);
		file.addSeparator();
		file.add(save);
		file.add(saveAs);
		file.add(close);
		file.addSeparator();
		file.add(quit);		
		
		edit.setMnemonic('e');		// open up edit menu when user presses Alt + e
		edit.add(Copy);
		edit.add(cut);
		edit.add(paste);
		edit.addSeparator();
		edit.add(FontMenu);

		lang.setMnemonic('l');		// open up language menu when user presses Alt + l
		for(int i=0;i<34;i++)
			langs[i]=new JCheckBoxMenuItem(CHECKMENUCODES[i]);
		langs[0].setSelected(true);		

		for(int i=0;i<34;i++)
		{
			langs[i].addActionListener(new langListener(i));
			lang.add(langs[i]);		// lang is the Language menu
		}

		myMenu.add(file);
		myMenu.add(edit);
		myMenu.add(lang);
		
		editor.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);		
		editor.setFont(curFont);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.add(jTabbedPane);
		jTabbedPane.add("Untitled"+(jTabbedPane.getTabCount()+1),scroller);
		editor.requestFocusInWindow();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(myMenu);
		frame.addWindowListener(new myWindowListener());
		frame.setSize(1100,900);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		editor.requestFocus();
	}

	class langListener implements ActionListener{

		int codeNo;

		public langListener(int c) {
			codeNo=c;
		}

		public void actionPerformed(ActionEvent e){			

			// first clear all check boxes 
			for(int i=0;i<34;i++)
			{
				langs[i].setSelected(false);
			}
			langs[codeNo].setSelected(true);
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.setSyntaxEditingStyle(STYLECODES[codeNo]);
		}
	}

	class neWListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			RSyntaxTextAreaExt editorCur=new RSyntaxTextAreaExt();
			RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
			editorCur.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JAVA);			
			editorCur.setFont(curFont);
			scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jTabbedPane.add("Untitled"+(jTabbedPane.getTabCount()+1),scrollerCur); 
        	jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount()-1);			
		}
	}
	class openListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			fileOpen.showOpenDialog(frame);
			openFile(fileOpen.getSelectedFile());			
		}
	}
	class saveListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	if(editorCur.filePath==null){
			fileSave.showSaveDialog(frame);
			saveFile(fileSave.getSelectedFile());
        	}
        	else{
        		if(editorCur.canUndo())
        		saveFile(new File(editorCur.filePath));
        	}
		}
	}
	class saveAsListener implements ActionListener{
		public void actionPerformed(ActionEvent e){			
			fileSave.showSaveDialog(frame);
			saveFile(fileSave.getSelectedFile());        	
		}
	}
	class closeListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			jTabbedPane.remove(jTabbedPane.getSelectedIndex());
		}
	}
	class quitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.out.println("sucess");
			System.exit(0);
		}
	}
	void openFile(File file){
			RSyntaxTextAreaExt editorCur=new RSyntaxTextAreaExt();
			RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
			editorCur.filePath = file.getAbsolutePath();	//Saves file path for saving without dialog box
			editorCur.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JAVA);
			editorCur.setFont(new Font("Courier New", Font.PLAIN, 13));
			scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jTabbedPane.add(file.getName(),scrollerCur); 
			jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount()-1);	        						     
		try{	
			FileReader fileReader=new FileReader(file);
			BufferedReader reader=new BufferedReader(fileReader);
			String line=null;
			while((line=reader.readLine())!=null)
				editorCur.append(line+"\n");
			fileSaved=true;
		}catch(Exception ex){
			System.out.println("ERROR OPENING THE FILE");
		}
	}	
	void saveFile(File file){
		try{
			jTabbedPane.setTitleAt(jTabbedPane.getSelectedIndex(), file.getName());
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.filePath = file.getAbsolutePath();	//Saves file path for saving without dialog box
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(editorCur.getText());
			writer.close();
		}catch(Exception ex){
			System.out.println("ERROR WRITING THE FILE");
		}
	}
	class myWindowListener implements WindowListener{

		public void windowOpened(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}

		public void windowClosing(WindowEvent e) {
			// Will need this method to check if user has saved file before quitting
		}
	}
	class CopyListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.copyAsRtf();
		}
	}
	class cutListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.copyAsRtf();
			editorCur.replaceSelection("");			
		}
	}
	class pasteListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	RSyntaxTextAreaEditorKit rstaek = new RSyntaxTextAreaEditorKit();
        	Transferable clipData = editorCur.cb.getContents(editorCur.cb);
            if (clipData != null) {
              try {
                if 
                  (clipData.isDataFlavorSupported
				    (DataFlavor.stringFlavor)) {
                      String s = (String)(clipData.getTransferData(
                        DataFlavor.stringFlavor));
                  editorCur.replaceSelection(s);
                }
              } catch (UnsupportedFlavorException ufe) {
                System.err.println("Flavor unsupported: " + ufe);
              } catch (IOException ioe) {
                System.err.println("Data not available: " + ioe);
              }
		}
	}
}
	class FontListener implements ActionListener {

		public void actionPerformed(ActionEvent e)
		{
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
	        String[] fontNames = ge.getAvailableFontFamilyNames();
	        fontCombo = new JComboBox(fontNames);
	        final JFrame fontFrame = new JFrame("Font Selection");
	        fontFrame.setAlwaysOnTop(true);
	        fontFrame.setLocation(300, 300);
	        final JRadioButton plain = new JRadioButton ("Plain");
	        final JRadioButton bold = new JRadioButton ("Bold");
	        final JRadioButton italic = new JRadioButton ("Italic");
	        final JRadioButton bni = new JRadioButton ("B&I");
	        final JTextField fontsize = new JTextField ();
	        JLabel fs = new JLabel ("Select Font");
	        JLabel ss = new JLabel ("Select Font Size");
	        JLabel sst = new JLabel ("Select Font Style");
	        fontsize.setToolTipText("Font Size");
	        JPanel style = new JPanel();
	        style.setLayout(new BoxLayout(style, BoxLayout.Y_AXIS));
	        JPanel fontspec = new JPanel();
	        fontspec.setLayout(new BoxLayout(fontspec, BoxLayout.Y_AXIS));
	        JPanel buttons = new JPanel ();
	        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	        JLabel blank = new JLabel ("      ");
	        JButton cancel = new JButton("Cancel");
	        JButton ok = new JButton ("OK");
	        ButtonGroup group = new ButtonGroup();
	        fontspec.add(fs);
	        fontspec.add(fontCombo);
	        fontspec.add(ss);
	        fontspec.add(fontsize);
	        fontspec.add(blank);
	        fontspec.add(blank);
	        fontspec.add(blank);
	        group.add(plain);
	        group.add(bold);
	        group.add(italic);
	        group.add(bni);
	        style.add(sst);
	        style.add(plain);
	        style.add(bold);
	        style.add(italic);
	        style.add(bni);
	        buttons.add(ok);
	        buttons.add(blank);
	        buttons.add(cancel);
	        fontFrame.add(style, BorderLayout.EAST);
	        fontFrame.add(buttons, BorderLayout.SOUTH);
	        fontFrame.add(fontspec, BorderLayout.CENTER);
	        fontFrame.setSize(350, 160);
	        fontFrame.setResizable(false);
	        fontFrame.setVisible(true);
	        fontCombo.addActionListener(new ActionListener ()
	        {
				public void actionPerformed(ActionEvent e)
				{
					int style = curFont.getStyle();
					if (plain.isSelected())
						style = java.awt.Font.PLAIN;
					else if (bold.isSelected())
						style = java.awt.Font.BOLD;
					else if (italic.isSelected())
						style = java.awt.Font.ITALIC;
					else if (bni.isSelected())
						style = java.awt.Font.BOLD | java.awt.Font.ITALIC;
					String sizeoffont = fontsize.getText();
					int size = curFont.getSize();
					try
					{
						size = Integer.parseInt(fontsize.getText());
					}
					finally
					{
						size = 13;
					}
					String name = (String)fontCombo.getSelectedItem();
					Font font_dec = java.awt.Font.decode(name);
					Font font = new Font (font_dec.getFontName(), style, size);
					Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();
					for (int i=0; i<cp.length; i++)
					{
						RSyntaxTextAreaExt r = (RSyntaxTextAreaExt)(cp[i]);
						r.setFont(font);
					}
				}
	        
	        });
	        ok.addActionListener(new ActionListener ()
	        {
				public void actionPerformed(ActionEvent e)
				{
					int style = curFont.getStyle();
					if (plain.isSelected())
						style = java.awt.Font.PLAIN;
					else if (bold.isSelected())
						style = java.awt.Font.BOLD;
					else if (italic.isSelected())
						style = java.awt.Font.ITALIC;
					else if (bni.isSelected())
						style = java.awt.Font.BOLD | java.awt.Font.ITALIC;
					String sizeoffont = fontsize.getText();
					int size = curFont.getSize();
					try
					{
						size = Integer.parseInt(fontsize.getText());
					}
					finally
					{
					}
					String name = (String)fontCombo.getSelectedItem();
					Font font_dec = java.awt.Font.decode(name);
					Font font = new Font (font_dec.getFontName(), style, size);
					Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();
					for (int i=0; i<cp.length; i++)
					{
						RSyntaxTextAreaExt r = (RSyntaxTextAreaExt)(cp[i]);
						r.setFont(font);
					}
					curFont = font;
					fontFrame.dispose();
				}
	        
	        });
	        cancel.addActionListener(new ActionListener ()
	        {
				public void actionPerformed(ActionEvent e)
				{
					editor.setFont(curFont);
					fontFrame.dispose();
				}
	        	
	        });

		}
	}
}