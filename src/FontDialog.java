import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class FontDialog extends JDialog implements ActionListener{		
	
	JComboBox fontCombo=null;
	JComboBox fontSize=null;
	
	JTextArea sample=null;
	
	JRadioButton plain=null;
	JRadioButton bold=null;
	JRadioButton italic=null;
	JRadioButton bni=null;
	
	JButton ok=null;
	JButton cancel=null;
	JButton preview=null;	
	
	JTabbedPane jTabbedPane = null;
	
	String[] fontSizes = {
			"6","7","8","9","10","11","12","13","14","15","16","17","18",
			"20","22","24","26","28",
			"32","36","40",
			"48","56","64","72"
		};
	FontDialog(JFrame frame,String name, Boolean modal,JTabbedPane pane){
		super(frame,name,modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jTabbedPane = pane;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
        String[] fontNames = ge.getAvailableFontFamilyNames();    
        fontCombo = new JComboBox(fontNames);
        
        String fn=Main.curFont.getFontName();        
        for(int i=0;i<fontNames.length;i++){        	
        	if(fontNames[i].equals(fn)){
        		fontCombo.setSelectedIndex(i);
        		break;
        	}
        }        
                
        setLocation(300, 300);
        //setSize(400, 200);
        plain = new JRadioButton ("Plain");
        bold = new JRadioButton ("Bold");
        italic = new JRadioButton ("Italic");
        bni = new JRadioButton ("B&I");
                
        int stl = Main.curFont.getStyle();
        
        JLabel fs = new JLabel ("Font:");
        JLabel ss = new JLabel ("Size:");
        JLabel sst = new JLabel ("Style:\n");
        //fontsize.setToolTipText("Font Size");
        final JPanel style1 = new JPanel();
        fontSize = new JComboBox(fontSizes);
        
        int size = Main.curFont.getSize();        
        for(int i=0;i<fontSizes.length;i++){                	
        	if(fontSizes[i].equals(""+size)){
        		fontSize.setSelectedIndex(i);
        		break;
        	}
        }    
        
        style1.setLayout(new BoxLayout(style1, BoxLayout.X_AXIS));
        final JPanel fontName = new JPanel();
        fontName.setLayout(new BoxLayout(fontName, BoxLayout.Y_AXIS));
        final JPanel fontspec = new JPanel();
        fontspec.setLayout(new BoxLayout(fontspec, BoxLayout.Y_AXIS));
        final JPanel buttons = new JPanel ();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        cancel = new JButton("Cancel");
        ok = new JButton ("OK");        
        sample = new JTextArea ("The Quick Brown Fox Jumps Over The Lazy Dog",3,40);
        sample.setEditable(false);
        JScrollPane jsp = new JScrollPane(sample);        
        JPanel sampleText = new JPanel ();
        ButtonGroup group = new ButtonGroup();
        preview = new JButton("Preview");
        sample.setFont(Main.curFont);
        fontName.add(fs);
        fontName.add(fontCombo);
        fontspec.add(ss);
        fontspec.add(fontSize);
        
        group.add(plain);        
        group.add(bold);
        group.add(italic);
        group.add(bni);   
        
        switch(stl){
        case Font.BOLD: bold.setSelected(true);break;
        case Font.PLAIN: plain.setSelected(true);break;
        case Font.ITALIC: italic.setSelected(true);break;
        default : bni.setSelected(true);break;
        }
        
        style1.add(plain);
        style1.add(bold);
        style1.add(italic);
        style1.add(bni);
        
        buttons.add(preview);
        buttons.add(ok);
        buttons.add(cancel);
        
        sampleText.add(jsp);
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel1.add(fontName);
        panel1.add(fontspec);
        panel2.add(sst);
        panel2.add(style1);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panel1);
        panel.add(panel2);
        add(panel, BorderLayout.NORTH);
        add(sampleText, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        //add(fontspec, BorderLayout.CENTER);        
        pack();
        preview.addActionListener(this);
        ok.addActionListener(this);
        cancel.addActionListener(this);        
        setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Success");
		if(preview==e.getSource()){
			int style = Main.curFont.getStyle();
			if (plain.isSelected())
			style = java.awt.Font.PLAIN;
			else if (bold.isSelected())
				style = java.awt.Font.BOLD;
			else if (italic.isSelected())
				style = java.awt.Font.ITALIC;
			else if (bni.isSelected())
				style = java.awt.Font.BOLD | java.awt.Font.ITALIC;
			String sizeoffont = (String)fontSize.getSelectedItem();
			int size = Main.curFont.getSize();
			try
			{
				size = Integer.parseInt(sizeoffont);
			}
			catch (Exception e1)
			{
				size = Main.curFont.getSize();
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
			if (sample.getWidth() > getWidth())
				setSize(sample.getWidth(), getHeight());
			}
		else if(ok==e.getSource()){
			repaint();
			int style = Main.curFont.getStyle();
			if (plain.isSelected())
				style = java.awt.Font.PLAIN;
			else if (bold.isSelected())
				style = java.awt.Font.BOLD;
			else if (italic.isSelected())
				style = java.awt.Font.ITALIC;
			else if (bni.isSelected())
				style = java.awt.Font.BOLD | java.awt.Font.ITALIC;
			String sizeoffont = (String)fontSize.getSelectedItem();
			int size = Main.curFont.getSize();
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
			Main.curFont = font;
			for(int i=0;i<jTabbedPane.getTabCount();i++){
				Component[] cp = ((JViewport)((JScrollPane)((jTabbedPane.getComponentAt(i)))).getComponent(0)).getComponents();
				RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]); 
				editorCur.setFont(font);
			}
			pack();			
			dispose();
			}
		else if(cancel==e.getSource()){
			dispose();
			}
		}
	
}
