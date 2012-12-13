import java.awt.Font;
import java.awt.Component;
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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Main2 {
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
	RSyntaxTextArea editor=new RSyntaxTextArea();
	RTextScrollPane scroller = new RTextScrollPane(editor);
	
	JFrame frame=new JFrame("JIGEditor");
	JMenuBar myMenu=new JMenuBar();
	
	JMenu file=new JMenu("File");
	JMenuItem neW=new JMenuItem("New");
	JMenuItem open=new JMenuItem("Open");
	JMenuItem save=new JMenuItem("Save");
	JMenuItem close=new JMenuItem("Close");
	JMenuItem quit=new JMenuItem("Quit");
	
	JMenu lang=new JMenu("Language");
	JCheckBoxMenuItem[] langs=new JCheckBoxMenuItem[34];
	
	JFileChooser fileSave=new JFileChooser();
	JFileChooser fileOpen=new JFileChooser();

	public static void main(String[] args) {
		new Main2().go();
	}
	
	void go(){
		
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));   // Ctrl + s 
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,ActionEvent.ALT_MASK));   // Alt + F4
		neW.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));    // Ctrl + n
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));  // Ctrl + w
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));   // Ctrl + o
		
		neW.addActionListener(new neWListener());
		open.addActionListener(new openListener());
		save.addActionListener(new saveListener());
		close.addActionListener(new closeListener());
		quit.addActionListener(new quitListener());
		
		file.setMnemonic('f');		//opens file menu when user presses Alt + f
		file.add(neW);
		file.add(open);
		file.add(save);
		file.add(close);
		file.add(quit);		
		
		for(int i=0;i<34;i++)
			langs[i]=new JCheckBoxMenuItem(CHECKMENUCODES[i]);
		
		langs[0].setSelected(true);
		
		lang.setMnemonic('l');		// open up language menu when user presses Alt + l
		
		for(int i=0;i<34;i++)
		{
			langs[i].addActionListener(new langListener(i));
			lang.add(langs[i]);		// lang is the Language menu
		}
		
		myMenu.add(file);
		myMenu.add(lang);
		editor.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);		
		editor.setFont(new Font("Courier New", Font.PLAIN, 13));
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.add(jTabbedPane);
		jTabbedPane.add("Untitled"+(jTabbedPane.getTabCount()+1),scroller);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(myMenu);
		frame.addWindowListener(new myWindowListener());
		frame.setSize(1100,900);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
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
        	RSyntaxTextArea editorCur = (RSyntaxTextArea)(cp[0]); 
        	editorCur.setSyntaxEditingStyle(STYLECODES[codeNo]);
		}
	}
	
	class neWListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			RSyntaxTextArea editorCur=new RSyntaxTextArea();
			RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
			editorCur.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JAVA);			
			editorCur.setFont(new Font("Courier New", Font.PLAIN, 13));
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
			RSyntaxTextArea editorCur=new RSyntaxTextArea();
			RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
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
        	RSyntaxTextArea editorCur = (RSyntaxTextArea)(cp[0]); 
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

}
