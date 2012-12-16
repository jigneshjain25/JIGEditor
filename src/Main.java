import java.awt.AWTKeyStroke;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Main implements Constants{
	
	int tabCnt=1;			//Counts total tabs created	

	JTabbedPane jTabbedPane=new JTabbedPane();
	RSyntaxTextAreaExt editor=new RSyntaxTextAreaExt();
	RTextScrollPane scroller = new RTextScrollPane(editor);

	JFrame frame=new JFrame("JIGEditor");

	JMenuBar myMenu=new JMenuBar();

	JMenu file=new JMenu("File");	
	JMenuItem neW=new JMenuItem("New");
	JMenuItem open=new JMenuItem("Open");
	JMenuItem save=new JMenuItem("Save");
	JMenuItem saveAs=new JMenuItem("Save As");
	JMenuItem close=new JMenuItem("Close");
	JMenuItem quit=new JMenuItem("Quit");

	JMenu edit=new JMenu("Edit");
	JMenuItem undo = new JMenuItem("Undo");
	JMenuItem redo = new JMenuItem("Redo");	
	JMenuItem Copy=new JMenuItem("Copy");
	JMenuItem cut=new JMenuItem("Cut");
	JMenuItem paste=new JMenuItem("Paste");
	JMenuItem FontMenu = new JMenuItem ("Font");	
	JMenuItem preferences = new JMenuItem ("Preferences");

	JMenu lang=new JMenu("Language");
	JCheckBoxMenuItem[] langs=new JCheckBoxMenuItem[34];
	JMenuItem[] langItems = new JMenuItem[34];
	JMenu pref = new JMenu ("Preferences");
	JMenu defCode = new JMenu ("Default Code");	
	
	JMenu search = new JMenu("Search");
	JMenuItem find = new JMenuItem("Find");
	JMenuItem replace = new JMenuItem("Replace");
	JMenuItem replaceAll = new JMenuItem("ReplaceAll");

	JFileChooser fileSave=new JFileChooser();
	JFileChooser fileOpen=new JFileChooser();
			
	static java.awt.Font curFont = new Font("Courier New", Font.PLAIN, 13);

	public static void main(String[] args) {
		//setting nimbus look if available
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, fall back to cross-platform
		    try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception ex) {
		        
		    }
		}
		
		Main o=new Main();
		RSyntaxTextAreaExt.obj=o;
		o.go();
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
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK));	//Ctrl + z
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,ActionEvent.CTRL_MASK));	//Ctrl + y

		neW.addActionListener(new neWListener(this));
		open.addActionListener(new openListener(this));
		save.addActionListener(new saveListener(this));
		saveAs.addActionListener(new saveAsListener(this));
		close.addActionListener(new closeListener(this));
		quit.addActionListener(new quitListener(this));
		Copy.addActionListener(new CopyListener(this));
		cut.addActionListener(new cutListener(this));
		paste.addActionListener(new pasteListener(this));
		FontMenu.addActionListener(new FontListener(this));
		undo.addActionListener(new undoListener(this));
		redo.addActionListener(new redoListener(this));

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
		edit.add(undo);
		edit.add(redo);
		edit.addSeparator();
		edit.add(Copy);
		edit.add(cut);
		edit.add(paste);
		edit.addSeparator();
		edit.add(FontMenu);
		
		search.add(find);
		search.add(replace);
		search.add(replaceAll);

		
		
		lang.setMnemonic('l');		// open up language menu when user presses Alt + l
		for(int i=0;i<34;i++)
		{
			langs[i]=new JCheckBoxMenuItem(CHECKMENUCODES[i]);
			langItems[i] = new JMenuItem(CHECKMENUCODES[i]);
		}
		
		langs[0].setSelected(true);		

		for(int i=0;i<34;i++)
		{
			langs[i].addActionListener(new langListener(this,i));
			lang.add(langs[i]);// lang is the Language menu
			langItems[i].addActionListener(new defaultCodeListener(this,i));
			defCode.add(langItems[i]);
		}
		
		pref.add(defCode);
		
		myMenu.add(file);
		myMenu.add(edit);
		myMenu.add(lang);
		myMenu.add(search);
		myMenu.add(pref);
		setupTabTraversalKeys(jTabbedPane);

		editor.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);		
		editor.setFont(new Font("Courier New", Font.PLAIN, 13));

		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.add(jTabbedPane);
		ButtonTabComponent btc = new ButtonTabComponent(jTabbedPane);
		jTabbedPane.add("Untitled"+(tabCnt++),scroller);
		jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount()-1, btc);		

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(myMenu);
		frame.addWindowListener(new myWindowListener(this));
		frame.setSize(900,800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		editor.requestFocusInWindow();
	}
	
	
	
	 private void setupTabTraversalKeys(JTabbedPane tabbedPane)
	  {
	    KeyStroke ctrlTab = KeyStroke.getKeyStroke("ctrl TAB");
	    KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke("ctrl shift TAB");

	    // Remove ctrl-tab from normal focus traversal
	    Set<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
	    forwardKeys.remove(ctrlTab);
	    tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

	    // Remove ctrl-shift-tab from normal focus traversal
	    Set<AWTKeyStroke> backwardKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
	    backwardKeys.remove(ctrlShiftTab);
	    tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

	    // Add keys to the tab's input map
	    InputMap inputMap = tabbedPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	    inputMap.put(ctrlTab, "navigateNext");
	    inputMap.put(ctrlShiftTab, "navigatePrevious");	    
	  }
}
