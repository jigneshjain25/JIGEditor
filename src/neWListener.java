import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fife.ui.rtextarea.RTextScrollPane;

public class neWListener implements ActionListener,Constants{
	
	Main frame=null;
	
	neWListener(Main main){
		frame = main;
	}
	
		public void actionPerformed(ActionEvent e) {
			final RSyntaxTextAreaExt editorCur=new RSyntaxTextAreaExt();
			final JFrame langFrame = new JFrame ("Select Language");
			final JComboBox langSelect = new JComboBox(CHECKMENUCODES);
			JPanel buttons = new JPanel();
			buttons.setLayout(new BorderLayout());
			JButton ok = new JButton("OK");
			buttons.add(ok, BorderLayout.CENTER);
			langFrame.add(langSelect);
			langFrame.add(buttons, BorderLayout.SOUTH);
			langFrame.setVisible(true);
			langFrame.setSize(150, 80);
			langFrame.setLocation(300, 300);
			ok.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					editorCur.setSyntaxEditingStyle(STYLECODES[langSelect.getSelectedIndex()]);
					editorCur.StyleCodeNo=langSelect.getSelectedIndex();
					File file = new File("Source/"+CHECKMENUCODES[langSelect.getSelectedIndex()]+".txt");
					if (file.exists())
					{
						try{	
							FileReader fileReader=new FileReader(file);
							BufferedReader reader=new BufferedReader(fileReader);
							String line=null;
							while((line=reader.readLine())!=null)
								editorCur.append(line+"\n");
							
						}catch(Exception ex){
						System.out.println("ERROR OPENING THE FILE");
						}
					}
					langFrame.dispose();
				}
			});
			
			editorCur.setFont(Main.curFont);
			RTextScrollPane scrollerCur = new RTextScrollPane(editorCur);
			scrollerCur.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			ButtonTabComponent btc = new ButtonTabComponent(frame.jTabbedPane);
			frame.jTabbedPane.add("Untitled"+(frame.tabCnt++),scrollerCur); 
			frame.jTabbedPane.setTabComponentAt(frame.jTabbedPane.getTabCount()-1, btc);
        	frame.jTabbedPane.setSelectedIndex(frame.jTabbedPane.getTabCount()-1);		
        	editorCur.requestFocusInWindow();
		}
	}