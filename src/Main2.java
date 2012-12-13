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
	boolean fileSaved=false;
	JTabbedPane jTabbedPane=new JTabbedPane();
	RSyntaxTextArea editor=new RSyntaxTextArea();
	RTextScrollPane scroller = new RTextScrollPane(editor);
	JFrame frame=new JFrame("jgEDIT");
	JMenuBar myMenu=new JMenuBar();
	JMenu file=new JMenu("File");
	JMenuItem neW=new JMenuItem("New");
	JMenuItem open=new JMenuItem("Open");
	JMenuItem save=new JMenuItem("Save");
	JMenuItem close=new JMenuItem("Close");
	JMenuItem quit=new JMenuItem("Quit");
	JFileChooser fileSave=new JFileChooser();
	JFileChooser fileOpen=new JFileChooser();
	JMenu lang=new JMenu("Language");
	JCheckBoxMenuItem[] langs=new JCheckBoxMenuItem[34];
	String language = "NONE";
	public enum Style {NONE("text/plain") , ACTIONSCRIPT("text/actionscript"),
		ASSEMBLER_X86("text/asm"),BBCODE("text/bbcode"),C("text/c"),CLOJURE("text/clojure"),
		CPLUSPLUS("text/cpp"),CSHARP("text/cs"),CSS("text/css"),DELPHI("text/delphi"),
		DTD("text/dtd"),FORTRAN("text/fortran"),GROOVY("text/groovy"),HTML("text/html"),
		JAVA("text/java"),JAVASCRIPT("text/javascript"),JSP("text/jsp"),LATEX("text/latex"),
		LISP("text/lisp"),LUA("text/lua"),MAKEFILE("text/makefile"),MXML("text/mxml"),
		PERL("text/perl"),PHP("text/php"),PROPERTIES_FILE("text/properties"),PYTHON("text/python"),
		RUBY("text/ruby"),SAS("text/sas"),SCALA("text/scala"),SQL("text/sql"),TCL("text/tcl"),
		UNIX_SHELL("text/unix"),WINDOWS_BATCH("text/bat"),XML("text/xml");
		
		private final String val;
		
		private Style(String text){
			val=text;
		}
		
		public String toString(){
			return val;
		}
	};
	
	public static void main(String[] args) {
		new Main2().go();
	}
	void go(){
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,ActionEvent.ALT_MASK));
		neW.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		
		neW.addActionListener(new neWListener());
		open.addActionListener(new openListener());
		save.addActionListener(new saveListener());
		close.addActionListener(new closeListener());
		quit.addActionListener(new quitListener());
		file.setMnemonic('f');
		file.add(neW);
		file.add(open);
		file.add(save);
		file.add(close);
		file.add(quit);		
		langs[0]=new JCheckBoxMenuItem("NONE");
		langs[1]=new JCheckBoxMenuItem("ACTIONSCRIPT");
		langs[2]=new JCheckBoxMenuItem("ASSEMBLER_X86");
		langs[3]=new JCheckBoxMenuItem("BBCODE");
		langs[4]=new JCheckBoxMenuItem("C");
		langs[5]=new JCheckBoxMenuItem("CLOJURE");
		langs[6]=new JCheckBoxMenuItem("CPLUSPLUS");
		langs[7]=new JCheckBoxMenuItem("CSHARP");
		langs[8]=new JCheckBoxMenuItem("CSS");
		langs[9]=new JCheckBoxMenuItem("DELPHI");
		langs[10]=new JCheckBoxMenuItem("DTD");
		langs[11]=new JCheckBoxMenuItem("FORTRAN");
		langs[12]=new JCheckBoxMenuItem("GROOVY");
		langs[13]=new JCheckBoxMenuItem("HTML");
		langs[14]=new JCheckBoxMenuItem("JAVA");
		langs[15]=new JCheckBoxMenuItem("JAVASCRIPT");
		langs[16]=new JCheckBoxMenuItem("JSP");
		langs[17]=new JCheckBoxMenuItem("LATEX");
		langs[18]=new JCheckBoxMenuItem("LISP");
		langs[19]=new JCheckBoxMenuItem("LUA");
		langs[20]=new JCheckBoxMenuItem("MAKEFILE");
		langs[21]=new JCheckBoxMenuItem("MXML");		
		langs[22]=new JCheckBoxMenuItem("PERL");
		langs[23]=new JCheckBoxMenuItem("PHP");
		langs[24]=new JCheckBoxMenuItem("PROPERTIES_FILE");
		langs[25]=new JCheckBoxMenuItem("PYTHON");
		langs[26]=new JCheckBoxMenuItem("RUBY");
		langs[27]=new JCheckBoxMenuItem("SAS");
		langs[28]=new JCheckBoxMenuItem("SCALA");
		langs[29]=new JCheckBoxMenuItem("SQL");
		langs[30]=new JCheckBoxMenuItem("TCL");
		langs[31]=new JCheckBoxMenuItem("UNIX_SHELL");
		langs[32]=new JCheckBoxMenuItem("WINDOWS_BATCH");
		langs[33]=new JCheckBoxMenuItem("XML");
		langs[0].setSelected(true);
		lang.setMnemonic('l');
		for(int i=0;i<34;i++)
		{
			langs[i].addActionListener(new langListener());
			lang.add(langs[i]);
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
		frame.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
			System.out.println("sucess");
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		frame.setSize(1100,900);
		frame.setLocation(85,70);
		frame.setVisible(true);		
	}
	
	class langListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			language = e.getActionCommand();			
			for(int i=0;i<34;i++)
			{
				if(langs[i].getActionCommand().equals(language)) continue;
				langs[i].setSelected(false);
			}
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextArea editorCur = (RSyntaxTextArea)(cp[0]); 
        	System.out.println(Style.valueOf(language).toString());
        	editorCur.setSyntaxEditingStyle(Style.valueOf(language).toString());
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

}