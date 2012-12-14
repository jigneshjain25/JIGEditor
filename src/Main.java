import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
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
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.GoToMatchingBracketAction;
import org.fife.ui.rtextarea.RTextAreaEditorKit;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.RecordableTextAction;

public class Main implements Constants{
//testing github -- code sharing	
	
	boolean fileSaved=false;
	
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
		
	JComboBox fontCombo;

	JFileChooser fileSave=new JFileChooser();
	JFileChooser fileOpen=new JFileChooser();
	
	java.awt.Font curFont = new Font("Courier New", Font.PLAIN, 13);
	String[] fontSizes = new String[48];

	public static void main(String[] args) {
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
		undo.addActionListener(new undoListener());
		redo.addActionListener(new redoListener());

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
		frame.addWindowListener(new myWindowListener());
		frame.setSize(1100,900);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		editor.requestFocusInWindow();
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
        	editorCur.syntaxCode = codeNo;
        	editorCur.setSyntaxEditingStyle(STYLECODES[codeNo]);
		}
	}

	class neWListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			RSyntaxTextAreaExt editorCur=new RSyntaxTextAreaExt();
			RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
			editorCur.setSyntaxEditingStyle(STYLECODES[editorCur.syntaxCode]);			
			editorCur.setFont(new Font("Courier New", Font.PLAIN, 13));
			scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			ButtonTabComponent btc = new ButtonTabComponent(jTabbedPane);
			jTabbedPane.add("Untitled"+(tabCnt++),scrollerCur); 
			jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount()-1, btc);
        	jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount()-1);		
        	editorCur.requestFocusInWindow();
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
        	if(editorCur.file==null){
			int val = fileSave.showSaveDialog(frame);
			if(val==fileSave.APPROVE_OPTION)				
				if(fileSave.getSelectedFile()!=null)
					saveFile(fileSave.getSelectedFile());
        	}
			else{
        		if(editorCur.canUndo())        			        		
        			saveFile(new File(editorCur.file.getAbsolutePath()));        				        		
        	}
		}
	}
	class saveAsListener implements ActionListener{
		public void actionPerformed(ActionEvent e){			
			int val = fileSave.showSaveDialog(frame);
			if(val==fileSave.APPROVE_OPTION)				
				if(fileSave.getSelectedFile()!=null)
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
			//editorCur.filePath = file.getAbsolutePath();	//Saves file path for saving without dialog box
			editorCur.setSyntaxEditingStyle(STYLECODES[editorCur.syntaxCode]);
			editorCur.setFont(new Font("Courier New", Font.PLAIN, 13));
			scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			ButtonTabComponent btc = new ButtonTabComponent(jTabbedPane);
			jTabbedPane.add(file.getName(),scrollerCur); 
			jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount()-1, btc);
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
		editorCur.requestFocusInWindow();
	}	
	void saveFile(File file){
		try{
			jTabbedPane.setTitleAt(jTabbedPane.getSelectedIndex(), file.getName());
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
        	editorCur.file = file;				//Saves file path for saving without dialog box
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
        	editorCur.paste();
	}
}
	class FontListener implements ActionListener {

		public void actionPerformed(ActionEvent e)
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
	        String[] fontNames = ge.getAvailableFontFamilyNames();
	        fontCombo = new JComboBox(fontNames);
	        final JFrame fontFrame = new JFrame("Font Selection");
	        //fontFrame.setAlwaysOnTop(true);
	        fontFrame.setLocation(300, 300);
	        final JRadioButton plain = new JRadioButton ("Plain");
	        final JRadioButton bold = new JRadioButton ("Bold");
	        final JRadioButton italic = new JRadioButton ("Italic");
	        final JRadioButton bni = new JRadioButton ("B&I");
	        //final JTextField fontsize = new JTextField ();
	        JLabel fs = new JLabel ("Select Font");
	        JLabel ss = new JLabel ("Select Font Size");
	        JLabel sst = new JLabel ("Select Font Style");
	        //fontsize.setToolTipText("Font Size");
	        final JPanel style1 = new JPanel();
	        final JComboBox fontSize = new JComboBox(fontSizes);
	        style1.setLayout(new BoxLayout(style1, BoxLayout.Y_AXIS));
	        final JPanel fontspec = new JPanel();
	        fontspec.setLayout(new BoxLayout(fontspec, BoxLayout.Y_AXIS));
	        final JPanel buttons = new JPanel ();
	        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	        JButton cancel = new JButton("Cancel");
	        JButton ok = new JButton ("OK");
	        final JLabel sample = new JLabel ("The Quick Brown Fox Jumps Over The Lazy Dog");
	        JPanel sampleText = new JPanel ();
	        ButtonGroup group = new ButtonGroup();
	        final JButton preview = new JButton("Preview");
	        sample.setFont(curFont);
	        fontspec.add(fs);
	        fontspec.add(fontCombo);
	        fontspec.add(ss);
	        fontspec.add(fontSize);
	        group.add(plain);
	        group.add(bold);
	        group.add(italic);
	        group.add(bni);
	        style1.add(sst);
	        style1.add(plain);
	        style1.add(bold);
	        style1.add(italic);
	        style1.add(bni);
	        buttons.add(preview);
	        buttons.add(ok);
	        buttons.add(cancel);
	        sampleText.add(sample);
	        fontFrame.add(style1, BorderLayout.EAST);
	        fontFrame.add(sampleText, BorderLayout.NORTH);
	        fontFrame.add(buttons, BorderLayout.SOUTH);
	        fontFrame.add(fontspec, BorderLayout.CENTER);
	        fontFrame.pack();
	        fontFrame.setVisible(true);
	        preview.addActionListener(new ActionListener()
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
					String sizeoffont = (String)fontSize.getSelectedItem();
					int size = curFont.getSize();
					try
					{
						size = Integer.parseInt(sizeoffont);
					}
					catch (Exception e1)
					{
						size = curFont.getSize();
					}
					Font font_dec = null;
					try 
					{
						String name = (String)fontCombo.getSelectedItem();
						font_dec= java.awt.Font.decode(name);
					}
					finally {}
					Font font = new Font (font_dec.getFontName(), style, size);
					sample.setFont(font);
					if (sample.getWidth() > fontFrame.getWidth())
						fontFrame.setSize(sample.getWidth(), fontFrame.getHeight());
					}
	        	});
	        ok.addActionListener(new ActionListener ()
	        {
				public void actionPerformed(ActionEvent e)
				{
					fontFrame.repaint();
					int style = curFont.getStyle();
					if (plain.isSelected())
						style = java.awt.Font.PLAIN;
					else if (bold.isSelected())
						style = java.awt.Font.BOLD;
					else if (italic.isSelected())
						style = java.awt.Font.ITALIC;
					else if (bni.isSelected())
						style = java.awt.Font.BOLD | java.awt.Font.ITALIC;
					String sizeoffont = (String)fontSize.getSelectedItem();
					int size = curFont.getSize();
					try
					{
						size = Integer.parseInt(sizeoffont);
					}
					finally
					{
					}
					String name = (String)fontCombo.getSelectedItem();
					Font font_dec = java.awt.Font.decode(name);
					Font font = new Font (font_dec.getFontName(), style, size);
					Component[] cp = jTabbedPane.getComponents();
					for (int i=0; i<cp.length; i++)
					{
						if (i != 1)
						{
							RTextScrollPane rt = (RTextScrollPane)(cp[i]);
							JViewport jv = (JViewport)(rt.getComponent(0));
							RSyntaxTextAreaExt rs = (RSyntaxTextAreaExt)(jv.getComponent(0));
							rs.setFont(font);
						}
					}
					fontFrame.pack();
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
	class undoListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);
        	editorCur.undoLastAction();
		}
		
	}
	class redoListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
        	RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);  
        	editorCur.redoLastAction();
		}
		
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
