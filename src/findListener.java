import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.fife.ui.rtextarea.*;
public class findListener implements ActionListener
{
	Main frame=null;
	boolean wasFoundAfterCaret;
	public findListener (Main main)
	{
		frame = main;
	}
	public void actionPerformed(ActionEvent e)
	{
		Component[] cp = ((JViewport)((JScrollPane)((frame.jTabbedPane.getSelectedComponent()))).getComponent(0)).getComponents();            
    	final RSyntaxTextAreaExt editorCur = (RSyntaxTextAreaExt)(cp[0]);
		final JFrame findFrame = new JFrame ("Find");
		findFrame.setSize(500, 200);
		findFrame.setLocation(400, 400);
		final JTextField text = new JTextField();
		final JTextField replaceText = new JTextField();
		replaceText.setEnabled(false);
		text.setColumns(30);
		replaceText.setColumns(27);
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JPanel textBoxPanel = new JPanel();
		JLabel text2find = new JLabel ("Find:");
		textBoxPanel.add(text2find, BorderLayout.CENTER);
		textBoxPanel.add(text, BorderLayout.CENTER);
		JPanel replaceBoxPanel = new JPanel();
		JLabel text2replace = new JLabel("Replace:");
		replaceBoxPanel.add(text2replace, BorderLayout.CENTER);
		replaceBoxPanel.add(replaceText, BorderLayout.CENTER);
		//textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		JButton find = new JButton("Find");
		final JButton replace = new JButton("Replace");
		JButton replaceall = new JButton("Replace All");
		JButton exit = new JButton("Cancel");
		textPanel.add(textBoxPanel);
		textPanel.add(replaceBoxPanel);
		JPanel buttons = new JPanel();
		JPanel options = new JPanel();
		final JRadioButton forward = new JRadioButton("Forward");
		JRadioButton backward = new JRadioButton("Backward");
		forward.setSelected(true);
		JLabel direc = new JLabel ("Choose direction");
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		options.add(direc);
		options.add(forward);
		options.add(backward);
		ButtonGroup dir = new ButtonGroup();
		dir.add(forward);
		dir.add(backward);
		JRadioButton fulltext = new JRadioButton("All");
		final JRadioButton selected = new JRadioButton("Selected");
		fulltext.setSelected(true);
		ButtonGroup scope = new ButtonGroup();
		scope.add(fulltext);
		scope.add(selected);
		JLabel scopeLabel = new JLabel("Scope");
		options.add(scopeLabel);
		options.add(fulltext);
		options.add(selected);
		final JCheckBox caseS = new JCheckBox ("Case sensitive");
		final JCheckBox regex = new JCheckBox ("RegEx");
		final JCheckBox ww = new JCheckBox ("Whole word");
		options.add(caseS);
		options.add(regex);
		options.add(ww);
		buttons.add(find);
		buttons.add(replace);
		buttons.add(replaceall);
		buttons.add(exit);
		replace.setEnabled(false);
		findFrame.add(textPanel, BorderLayout.CENTER);
		findFrame.add(buttons, BorderLayout.SOUTH);
		findFrame.add(options, BorderLayout.EAST);
		findFrame.setVisible(true);
		final SearchContext context = new SearchContext();
		find.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean direction = forward.isSelected();
				context.setSearchForward(direction);
			    context.setWholeWord(ww.isSelected());
			    context.setRegularExpression(regex.isSelected());
			    context.setMatchCase(caseS.isSelected());
			    context.setSearchSelectionOnly(selected.isSelected());
				String query = text.getText();
				context.setSearchFor(query);
				boolean found = SearchEngine.find(editorCur, context);
				if (found)
				{
					editorCur.requestFocus();
					replace.setEnabled(true);
					replaceText.setEnabled(true);
				}
				else
				{
						 int choice = JOptionPane.showConfirmDialog(findFrame,"Text not found, start searching from beginning?", "Search again?", JOptionPane.YES_NO_OPTION);
						 if (choice == JOptionPane.YES_OPTION)
						 {
							 editorCur.setCaretPosition(0);
							 actionPerformed(e);
						 }
				}
			}
		});
		replace.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String s = replaceText.getText();
				if (s != null && s.length() != 0 && editorCur.getSelectedText()!= null && editorCur.getSelectedText().length()!=0)
				{
					editorCur.replaceSelection(s);
				}
				else if (s==null || s.length() == 0)
				{
					JOptionPane.showMessageDialog(findFrame, "Please enter text to replace");
				}
				else if (editorCur.getSelectedText()==null || editorCur.getSelectedText().length() == 0)
				{
					JOptionPane.showMessageDialog(findFrame, "No text selected in Editor");
				}
			}
		});
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				findFrame.dispose();
			}
		});
		replaceall.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				boolean direction = forward.isSelected();
				context.setSearchForward(direction);
			    context.setWholeWord(ww.isSelected());
			    context.setRegularExpression(regex.isSelected());
			    context.setMatchCase(caseS.isSelected());
			    context.setSearchSelectionOnly(selected.isSelected());
				String s = JOptionPane.showInputDialog("Enter phrase to be replaced with");
				if (s != null && s.length() > 0)
				{
					context.setSearchFor(text.getText());
					context.setReplaceWith(s);
					int caretLoc = editorCur.getCaretPosition();
					editorCur.setCaretPosition(0);
					int found = SearchEngine.replaceAll(editorCur, context);
					if (found == 0)
					{
						JOptionPane.showMessageDialog(findFrame, "No entries changed.");
					}
					else
					{
						JOptionPane.showMessageDialog(findFrame, found+" entry(-ies) changed.");
					}
					int max = editorCur.getText().length();
					if (max >= caretLoc)
						editorCur.setCaretPosition(caretLoc);
					else
						editorCur.setCaretPosition(max-1);
				}
			}
		});
	}
}
