import java.awt.AWTKeyStroke;
import java.awt.Component;
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
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.w3c.dom.Document;

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
	JMenuItem print=new JMenuItem("Print");
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
	JMenu security = new JMenu("Security");
	JMenuItem encrypt = new JMenuItem("Encrypt");
	JMenuItem decrypt = new JMenuItem("Decrypt");
	JCheckBoxMenuItem wordWrap = new JCheckBoxMenuItem("Word Wrap",false);
	JCheckBoxMenuItem codeFold = new JCheckBoxMenuItem("Code Folding",true);
	JCheckBoxMenuItem lineNumber = new JCheckBoxMenuItem("Line Number",true);
	JMenu tabSize = new JMenu("Tab Width");
	JMenuItem theme = new JMenuItem("Theme");
	JCheckBoxMenuItem[] tabSizes=new JCheckBoxMenuItem[34];
	
	JMenu search = new JMenu("Search");
	JMenuItem find = new JMenuItem("Find and Replace");
	
	JMenu export = new JMenu("Export");
	JMenuItem pasteBin = new JMenuItem("Upload to PasteBin");
	JMenuItem email = new JMenuItem("Email me");

	JFileChooser fileSave=new JFileChooser();
	JFileChooser fileOpen=new JFileChooser();
	
	static java.awt.Font curFont = new Font("Courier New", Font.PLAIN, 13);

	int curTabWidth = 8;
	
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
		SplashWindow s= new SplashWindow();
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
		find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));	//Ctrl + h
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));	//Ctrl + p

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
		wordWrap.addActionListener(new prefListener(this));
		codeFold.addActionListener(new prefListener(this));
		lineNumber.addActionListener(new prefListener(this));
		encrypt.addActionListener(new SecurityListener(this));
		decrypt.addActionListener(new SecurityListener(this));
		find.addActionListener(new findListener(this));
		print.addActionListener(new printListener(this));
		pasteBin.addActionListener(new pastebinListener(this));
		email.addActionListener(new emailListener(this));
		theme.addActionListener(new themeListener(this));
		
		file.setMnemonic('f');		//opens file menu when user presses Alt + f
		file.add(neW);
		file.add(open);
		file.addSeparator();
		file.add(save);
		file.add(saveAs);
		file.add(close);
		file.addSeparator();
		file.add(print);
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
		
		search.setMnemonic('s');
		search.add(find);

		export.setMnemonic('x');
		export.add(pasteBin);
		export.add(email);
		
		undo.setEnabled(false);
		redo.setEnabled(false);
		
		lang.setMnemonic('l');		// open up language menu when user presses Alt + l
		for(int i=0;i<34;i++)
		{
			langs[i]=new JCheckBoxMenuItem(CHECKMENUCODES[i]);	//lang check boxes
			langItems[i] = new JMenuItem(CHECKMENUCODES[i]);	//default code menu items
		}
		
		langs[0].setSelected(true);		

		for(int i=0;i<34;i++)
		{
			langs[i].addActionListener(new langListener(this,i));
			lang.add(langs[i]);// lang is the Language menu
			langItems[i].addActionListener(new defaultCodeListener(this,i));
			defCode.add(langItems[i]);
		}
		
		for(int i=0;i<16;i++)
		{
			tabSizes[i] = new JCheckBoxMenuItem(TABSIZES[i]);
			tabSizes[i].addActionListener(new TabSizeListener(this,i+1));
			tabSize.add(tabSizes[i]);								
		}
		tabSizes[5].setSelected(true);
		
		pref.add(defCode);
		pref.addSeparator();
		pref.add(tabSize);
		pref.add(codeFold);
		pref.add(lineNumber);
		pref.add(wordWrap);
		pref.add(theme);
		pref.addSeparator();
		pref.add(security);
		
		security.add(encrypt);
		security.add(decrypt);
		
		myMenu.add(file);
		myMenu.add(edit);
		myMenu.add(lang);
		myMenu.add(search);
		myMenu.add(pref);
		myMenu.add(export);
		
		setupTabTraversalKeys(jTabbedPane);		
		jTabbedPane.addFocusListener(new tabFocusListener(this));
		
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(jTabbedPane);
		ButtonTabComponent btc = new ButtonTabComponent(jTabbedPane);
		jTabbedPane.add("Untitled"+(tabCnt++),scroller);
		jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount()-1, btc);		

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setJMenuBar(myMenu);
		frame.addWindowListener(new myWindowListener(this));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setSize(900,800);
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

