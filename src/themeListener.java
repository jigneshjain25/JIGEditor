import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;

public class themeListener implements ActionListener
{
	Main frame;
	String[] FIELDS = {"NULL", "COMMENT_EOL", "COMMENT_MULTILINE", "COMMENT_DOCUMENTATION", "COMMENT_KEYWORD", "COMMENT_MARKUP", "RESERVED_WORD", "RESERVED_WORD_2", "FUNCTION", "LITERAL_BOOLEAN", "LITERAL_NUMBER_DECIMAL_INT", "LITERAL_NUMBER_FLOAT", "LITERAL_NUMBER_HEXADECIMAL", "LITERAL_STRING_DOUBLE_QUOT", "LITERAL_CHAR", "LITERAL_BACKQUOTE", "DATA_TYPE", "VARIABLE", "REGEX", "ANNOTATION", "IDENTIFIER", "WHITESPACE", "SEPARATOR", "OPERATOR", "PREPROCESSOR", "MARKUP_TAG_DELIMITER", "MARKUP_TAG_NAME", "MARKUP_TAG_ATTRIBUTE", "MARKUP_TAG_ATTRIBUTE_VALUE", "MARKUP_PROCESSING_INSTRUCTION", "MARKUP_CDATA", "ERROR_IDENTIFIER", "ERROR_NUMBER_FORMAT", "ERROR_STRING_DOUBLE", "ERROR_CHAR", "NUM_TOKEN_TYPES"};
	String[] fontSizes = {
			"6","7","8","9","10","11","12","13","14","15","16","17","18",
			"20","22","24","26","28",
			"32","36","40",
			"48","56","64","72"
		};
	JComboBox jb, fontCombo, fontSize;
	JFrame themeFrame;
	int choice;
	JTextField redbg, greenbg, bluebg, redfg, bluefg, greenfg;
	JLabel redL, greenL, blueL, underL, bg, fg;
	JCheckBox underline;
	SyntaxScheme curScheme, newScheme;
	Style style;
	RSyntaxTextAreaExt[] editors;
	JButton Font, set, close;
	java.awt.Font font;
	Color bgColor, fgColor;
	boolean underbool;
	boolean isColor (int r, int g, int b)
	{
		if (r <= 255 && r>=0 && g <= 255 && g>= 0 && b<=255 && b>=0)
			return true;
		else
			return false;
	}
	public themeListener(Main main)
	{
		frame = main;
		int count = frame.jTabbedPane.getTabCount();
        editors = new RSyntaxTextAreaExt [count];
        for(int i=0;i<frame.jTabbedPane.getTabCount();i++)
        {
			Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getComponentAt(i)))).getComponent(0)).getComponents();
			editors[i] = (RSyntaxTextAreaExt)(cp[0]); 
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		themeFrame = new JFrame("Theme selection");
		jb = new JComboBox(FIELDS);
		redbg = new JTextField(6);
		greenbg = new JTextField(6);
		bluebg = new JTextField(6);
		redfg = new JTextField(6);
		greenfg = new JTextField(6);
		bluefg = new JTextField(6);
		underline = new JCheckBox("Underline");
		redL = new JLabel ("Red");
		greenL = new JLabel("Green");
		blueL = new JLabel("Blue");
		underL = new JLabel("Underline");
		bg = new JLabel("Background colors");
		fg = new JLabel("Foreground colors");
		Font = new JButton("Font");
		set = new JButton("Set");
		close = new JButton("Close");
		newScheme = new SyntaxScheme(true);
		go();
		Font.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFontChooser fontChooser = new JFontChooser();
				fontChooser.setSelectedFontFamily(font.getFontName());
				fontChooser.setSelectedFontSize(font.getSize());
				fontChooser.setSelectedFontStyle(font.getStyle());
				int result = fontChooser.showDialog(frame.frame);
				if(result == JFontChooser.CANCEL_OPTION)return;
				font = fontChooser.getSelectedFont();
			}
		});
		set.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				int rb, bb, gb, rf, bf, gf;
				rb = Integer.parseInt(redbg.getText());
				bb = Integer.parseInt(bluebg.getText());
				gb = Integer.parseInt(greenbg.getText());
				rf = Integer.parseInt(redfg.getText());
				bf = Integer.parseInt(bluefg.getText());
				gf = Integer.parseInt(greenfg.getText());

				if (isColor (rb, bb, gb) && isColor(rf, bf, gf))
				{
					style = new Style(fgColor, bgColor, font, underbool);
					newScheme.setStyle(jb.getSelectedIndex(), style);
					for (int i=0; i<editors.length; i++)
					{
						editors[i].setSyntaxScheme(newScheme);
					}
					frame.scheme = newScheme;
				}
				else
				{
					JOptionPane.showMessageDialog(themeFrame, "Invalid color value, fields should be within 0 and 255 (inclusive)");
				}
			}
		});
		jb.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				style = editors[0].getSyntaxScheme().getStyle(jb.getSelectedIndex());
				bgColor = style.background;
				redbg.setText(bgColor.getRed()+"");
				greenbg.setText(bgColor.getGreen()+"");
				bluebg.setText(bgColor.getBlue()+"");
				fgColor = style.foreground;
				redfg.setText(fgColor.getRed()+"");
				greenfg.setText(fgColor.getGreen()+"");
				bluefg.setText(fgColor.getBlue()+"");
				font = style.font;
				underline.setSelected(style.underline);
				underbool = underline.isSelected();
			}
		});
		underline.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				underbool = underline.isSelected();
			}
		});
        close.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		themeFrame.dispose();
        	}
        });
	}
	public void go()
	{

	}
}
